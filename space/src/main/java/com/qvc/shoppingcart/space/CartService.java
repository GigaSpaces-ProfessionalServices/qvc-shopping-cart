package com.qvc.shoppingcart.space;

import com.gigaspaces.document.SpaceDocument;
import com.qvc.shoppingcart.common.Cart;
import com.qvc.shoppingcart.service.ICartService;
import org.openspaces.core.GigaSpace;
import org.openspaces.remoting.RemotingService;
import org.openspaces.remoting.Routing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// This lives within each partition
@RemotingService
@Transactional
public class CartService implements ICartService {

	@Autowired
	GigaSpace gigaSpace;

    public Cart getCart(@Routing int cartId) {
    	// bla bla 
		//return gigaSpace.readById(cartId);
		return null;
    }

	@Override
	public boolean isCartExist(int cartId) {
		return false;
	}

	@Override
	public boolean createCart(@Routing int cartId, SpaceDocument cartPayload) {
		Cart c = createMyCart(cartPayload);
		c.setId(cartId);
		Integer leaseTime = 10000; // getLeaseByRegion(cartPayload.getRegion());
		gigaSpace.write(c, leaseTime);
		return true;
	}

	@Override
	public void mergeCarts(List<Integer> cartIds) {

	}


	public Integer createCart(SpaceDocument cartPayload) {
		// Convert cart payload
		Cart c = createMyCart(cartPayload);
		Integer leaseTime = 10000; // getLeaseByRegion(cartPayload.getRegion());
		gigaSpace.write(c, leaseTime);
    return c.getId();
	}

	private Cart createMyCart(SpaceDocument cartPayload) {
		return null;
	}

	public boolean updateCart(@Routing int cartId, SpaceDocument cartPayload) {
		// examine the contents of cartPayload
		
//		gigaSpace.write(shipping, );
//
//		gigaSpace.write(address);
//
//		gigaSpace.write(mailing...);

		return true;
		
	}
}
