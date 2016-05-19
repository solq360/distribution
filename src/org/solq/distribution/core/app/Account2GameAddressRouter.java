package org.solq.distribution.core.app;

import org.solq.distribution.core.Address;
import org.solq.distribution.core.IRouter;

/**
 * 创建账号路由器接口
 * 
 * @author solq
 */
public class Account2GameAddressRouter implements IRouter<String, Address> {

    @Override
    public Address convert(String name) {
	//名称是否合法 -> N小时平均人数上限 判断 -> 总账号数上限判断  -> 缓存上限判断 -> 当前宽带上限判断
	
	return null;
    }
}
