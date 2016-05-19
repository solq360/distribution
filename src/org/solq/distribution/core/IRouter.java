package org.solq.distribution.core;

/**
 * 路由器接口
 * @author solq
 * */
public interface IRouter<I,O> {
    public O convert(I o);
}
