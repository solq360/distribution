package org.solq.distribution.test.cmap;

import java.io.Serializable;

public class TestEntity implements Serializable {
 
    private static final long serialVersionUID = 4257968847367822875L;
    private String id;
    private String name;

    public static TestEntity of(String id, String name) {
	TestEntity ret = new TestEntity();
	ret.id = id;
	ret.name = name;
	return ret;
    }

    public String getId() {
	return id;
    }

    void setId(String id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    void setName(String name) {
	this.name = name;
    }
}