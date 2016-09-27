package com.qvc.shoppingcart.space;

import com.qvc.shoppingcart.common.Cart;
import org.openspaces.events.EventDriven;
import org.openspaces.events.EventTemplate;
import org.openspaces.events.adapter.SpaceDataEvent;
import org.openspaces.events.notify.Notify;
import org.openspaces.events.notify.NotifyType;

// lease expiry manager
@EventDriven
@Notify
@NotifyType(leaseExpire = true)
public class CartLeaseManager {

    @EventTemplate
    Cart expiredCartsCriteria() {
        Cart c = new Cart();
		return c;
    }

    @SpaceDataEvent
    public void eventListener(Cart c) {
        // Do something with expired carts
    }

}
