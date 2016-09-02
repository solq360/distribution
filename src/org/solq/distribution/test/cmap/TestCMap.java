package org.solq.distribution.test.cmap;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.solq.distribution.test.Tool;

import net.openhft.chronicle.map.ChronicleMap;

public class TestCMap {

    @Test
    public void init() throws IOException {
	int count = Tool.count;

	ChronicleMap<String, TestEntity> cityPostalCodes = ChronicleMap.of(String.class, TestEntity.class).averageKey("Amsterdam").entries(count).averageKeySize(8).averageValueSize(1024)
		.createPersistedTo(new File("cmap"));
	long start = System.currentTimeMillis();

	while (count-- > 0) {
	    cityPostalCodes.put(count + "a", TestEntity.of(count + "a,", "b"));
	}
	Tool.printlnTime("用时:", start);

	System.out.println(cityPostalCodes.size());
    }
}
