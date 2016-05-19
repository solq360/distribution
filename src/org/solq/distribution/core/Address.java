package org.solq.distribution.core;
/**
 * 地址
 * @author solq
 * */
public class Address {
    /**
     * 定位链接
     */
    private String url;
    /**
     * 上下文
     */
    private String ctx;
    /**
     * 服务器状态
     */
    private AddressState state;

    // getter
    public String getUrl() {
	return url;
    }

    void setUrl(String url) {
	this.url = url;
    }

    public String getCtx() {
	return ctx;
    }

    void setCtx(String ctx) {
	this.ctx = ctx;
    }

    public AddressState getState() {
	return state;
    }

    void setState(AddressState state) {
	this.state = state;
    }

}
