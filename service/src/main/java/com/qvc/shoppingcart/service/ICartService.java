package com.qvc.shoppingcart.service;

import com.gigaspaces.document.SpaceDocument;
import com.qvc.shoppingcart.common.Cart;
import org.openspaces.remoting.Routing;

import java.util.List;

public interface ICartService {

  Cart getCart(@Routing int cartId);

  boolean isCartExist(int cartId);

  boolean createCart(@Routing int cartId, SpaceDocument cartPayload);

  boolean updateCart(@Routing int cartId, SpaceDocument cartPayload);

  // Broadcast operations
  void mergeCarts(List<Integer> cartIds);
}
