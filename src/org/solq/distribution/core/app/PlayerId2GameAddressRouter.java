package org.solq.distribution.core.app;

import org.solq.distribution.core.Address;
import org.solq.distribution.core.IRouter;

/**
 * 玩家ID路由器接口
 * 
 * @author solq
 */
public class PlayerId2GameAddressRouter implements IRouter<Long, Address> {

    @Override
    public Address convert(Long playerId) {

	return null;
    }
}
