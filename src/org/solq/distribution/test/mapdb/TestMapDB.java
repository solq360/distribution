package org.solq.distribution.test.mapdb;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import org.mapdb.serializer.GroupSerializer;
import org.solq.distribution.test.Tool;
import org.solq.distribution.test.model.Player;

public class TestMapDB {

    /** 测试数据溢出 */
    // @Before
    public void test_over_init() {
	DB db = DBMaker.fileDB("test_over.db").checksumHeaderBypass().fileMmapEnableIfSupported().make();
	HTreeMap<Long, Player> map = db.hashMap("map", Serializer.LONG, SerializerJson.of(Player.class)).expireExecutor(Executors.newScheduledThreadPool(4)).createOrOpen();
	long count = 1;
	map.put(count, Player.ofOver(count, count + "", count + ""));
	count = 2;
	map.put(count, Player.ofOver(count, count + "", count + ""));
	count = 3;
	map.put(count, Player.ofOver(count, count + "", count + ""));
	db.close();
    }

    /** 测试数据溢出 */
    @Test
    public void test_over() {
	DB db = DBMaker.fileDB("test_over.db").checksumHeaderBypass().fileMmapEnableIfSupported().make();
	HTreeMap<Long, Player> map = db.hashMap("map", Serializer.LONG, SerializerJson.of(Player.class)).expireExecutor(Executors.newScheduledThreadPool(4)).createOrOpen();
	System.out.println(map.size());

	long count = 1;
	map.put(count, Player.of(count, count + "", count + ""));

	Player p1 = map.get(1L);
	Player p2 = map.get(2L);
	System.out.println(p1.getName());
	System.out.println(p2.getName());
	db.close();
    }

    // 写入速度快，查询全部数据慢
    @Test
    public void test_htree() {
	int len =   Tool.count; 
	DB db = DBMaker.fileDB("test_htree.db").checksumHeaderBypass()
		// .concurrencyScale(4).allocateStartSize(len)
		// .executorEnable()
		// .fileLockDisable()
		//.fileMmapEnable().fileMmapEnableIfSupported().fileMmapPreclearDisable().cleanerHackEnable()
		.make();
	// db.getStore().fileLoad();
	HTreeMap<Long, Player> map = db.hashMap("map", Serializer.LONG, SerializerJson.of(Player.class)).expireExecutor(Executors.newScheduledThreadPool(4)).createOrOpen();
	long start = System.currentTimeMillis();
	Tool.printlnTime("len :" + map.size() + "用时:", start);
	
 	
	if (true) {
	    long count = 0;
	    start = System.currentTimeMillis();
	    while (count++ < len) {
		map.put(count, Player.of(count, count + "", count + ""));
	    }
	    Tool.printlnTime("用时:", start);
	    while (count-- > 0) {
		if (count % 5000 == 0) {
		    System.out.println(map.get((Object) count).getName());
		}
	    }
	}

	db.close();
    }

    @Test
    public void test_btree_get() {
	DB db = DBMaker.fileDB("test_btree.db").checksumHeaderBypass().fileMmapEnableIfSupported().readOnly().make();
	BTreeMap<Long, Player> map = db.treeMap("map", Serializer.LONG, (GroupSerializer<Player>) SerializerJson.of(Player.class)).createOrOpen();
	long count = 50000;
	while (count-- > 0) {
	    System.out.println(map.get((Object) count).getName());
	}
	db.close();
    }

    @Test
    // 测试btree 编码,写入速度慢，查询全部数据快
    public void test_btree() {
	int len = Tool.count;

	DB db = DBMaker.fileDB("test_btree.db").checksumHeaderBypass().fileMmapEnableIfSupported().allocateStartSize(len).make();
	BTreeMap<Long, Player> map = db.treeMap("map", Serializer.LONG, (GroupSerializer<Player>) SerializerJson.of(Player.class)).createOrOpen();
	long count = 0;
	long start = System.currentTimeMillis();
	while (count++ < len) {
	    map.put(count, Player.of(count, count + "", count + ""));
	    if (count % 5000 == 0) {
		Tool.printlnTime("len :" + map.size() + "用时:", start);
		start = System.currentTimeMillis();
	    }
	}

	while (count-- > 0) {
	    if (count % 5000 == 0) {
		System.out.println(map.get((Object) count).getName());
	    }
	}

	db.close();
    }

    @Test
    public void shardMap() {
	HTreeMap<String, String> aMap = DBMaker.memoryShardedHashMap(8).valueSerializer(Serializer.STRING).keySerializer(Serializer.STRING).create();
	aMap.put("aaaaaaaaaaaaa", "bbbbbbbbbbbbb");
	System.out.println(aMap.size());
    }

    @Test
    public void test_memory() {
	DB db = DBMaker.memoryDB().make();
	HTreeMap<Long, Player> map = db.hashMap("map", Serializer.LONG, SerializerJson.of(Player.class)).expireExecutor(Executors.newScheduledThreadPool(4)).createOrOpen();
	long start = System.currentTimeMillis();
	Tool.printlnTime("len :" + map.size() + "用时:", start);
	long count = 0;
	start = System.currentTimeMillis();
	long len = 50000;
	while (count++ < len) {
	    map.put(count, Player.of(count, count + "", count + ""));
	    if (count % 5000 == 0) {
		Tool.printlnTime("len :" + map.size() + "用时:", start);
		start = System.currentTimeMillis();
	    }
	}
	while (count-- > 0) {
	    if (count % 5000 == 0) {
		System.out.println(map.get((Object) count).getName());
	    }
	}
	db.close();
    }

    @Test
    public void cacheSync() throws InterruptedException {

	DB dbDisk = DBMaker.fileDB("writeBack.db").checksumHeaderBypass().make();
	HTreeMap<String, String> onDisk = dbDisk.hashMap("onDisk", Serializer.STRING, Serializer.STRING).createOrOpen();

	DB dbMemory = DBMaker.memoryDB().make();
	HTreeMap<String, String> inMemory = dbMemory.hashMap("cache", Serializer.STRING, Serializer.STRING).expireAfterUpdate(1, TimeUnit.SECONDS).expireAfterGet(1, TimeUnit.SECONDS)
		.expireAfterCreate(1, TimeUnit.SECONDS).expireMaxSize(1).expireExecutorPeriod(500).expireOverflow(onDisk)
		// good idea is to enable background expiration
		.expireExecutor(Executors.newScheduledThreadPool(2)).create();

	inMemory.put("key", "map");
	inMemory.put("key1", "map2");

	Thread.sleep(5000);
	inMemory.put("key2", "map2");

	System.out.println(inMemory.size());
	Set<Entry<String, String>> es = inMemory.entrySet();
	for (Entry<String, String> entry : es) {
	    System.out.println("=========== : " + entry.getKey());
	}

	System.out.println("disk : " + onDisk.get("key"));
	System.out.println("cache : " + inMemory.get("key"));
	System.out.println(inMemory.size());

	for (Entry<String, String> entry : es) {
	    System.out.println("=========== : " + entry.getKey());
	}

	dbMemory.close();
	dbDisk.close();
    }

    @Test
    public void test_info() {
	DB db = DBMaker.fileDB("test_htree.db").checksumHeaderBypass().fileMmapEnableIfSupported().readOnly().make();
	for (Entry<String, Object> entry : db.getAll().entrySet()) {
	    System.out.println(entry.getKey());
	}
	db.close();
    }

}
