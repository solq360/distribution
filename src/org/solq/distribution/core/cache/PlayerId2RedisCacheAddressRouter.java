package org.solq.distribution.core.cache;

import org.solq.distribution.core.Address;
import org.solq.distribution.core.IRouter;

/**
 * 玩家ID路由器接口
 * 
 * @author solq
 */
public class PlayerId2RedisCacheAddressRouter implements IRouter<Long, Address> {

    @Override
    public Address convert(Long i) {

	return null;
    }
}
