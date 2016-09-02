package org.solq.distribution.test.model;

import java.util.Map;

public class Item {
    //ID
    private long id;
    //排序，权重
    private Map<String, Integer> weights;

    public static Item of(long id, Map<String, Integer> weights) {
	Item ret = new Item();
	ret.id = id;
	ret.weights = weights;
	return ret;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (int) (id ^ (id >>> 32));
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Item other = (Item) obj;
	if (id != other.id)
	    return false;
	return true;
    }

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public Map<String, Integer> getWeights() {
	return weights;
    }

    public void setWeights(Map<String, Integer> weights) {
	this.weights = weights;
    }

}
