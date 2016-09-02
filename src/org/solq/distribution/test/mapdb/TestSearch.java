package org.solq.distribution.test.mapdb;

import java.lang.instrument.Instrumentation;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.solq.distribution.test.model.Item;

public class TestSearch {
    public static void premain(String args, Instrumentation inst) {

	Item obj = Item.of(123456789456L, null);
	System.out.println("Bytes used by Object:" + inst.getObjectSize(obj));
	System.out.println("Bytes used by MyObject:" + inst.getObjectSize(new Item()));
    }

    static Map<String, List<Item>> indexMapData;  
    public static void main(String[] args) {
	String searchKey = "大片";
	String filterKey = "人气";
	int page = 1;
	int pageSize = 10;
	
	Set<String> keys = indexMapData.keySet();
	String key = null;
	for (String k : keys) {
	    if (k.matches(searchKey)) {
		key = k;
		break;
		
	    }
	}
	//分类排序
	List<Item> list = indexMapData.get(key);
	//可根据业务实现多个排序器
	Comparator<Item> s = new Comparator<Item>() {
	    @Override
	    public int compare(Item o1, Item o2) {
		int a = o1.getWeights().get(filterKey);
		int b = o2.getWeights().get(filterKey);
		return a - b;
	    }

	};
	//分页切割
	Collections.sort(list, s);
	list.subList((page-1)*pageSize, pageSize);
	//重新评算权重
	for(Item i : list){
	  int value=  i.getWeights().get(filterKey)+1;//xxxxxxx
	  i.getWeights().put(filterKey, value);
	}
	 
    }
}