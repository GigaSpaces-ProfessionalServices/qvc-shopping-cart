package com.qvc.shoppingcart.service;

import org.openspaces.remoting.Routing;

import java.util.List;

public interface ICartService {

  String getCart(@Routing int cartId);

  boolean isCartExist(int cartId);

  boolean createCart(@Routing int cartId, String cartJson);

  boolean updateCart(@Routing int cartId, String cartJson);

  void updatePaymentData(@Routing int cartId, String paymentJson);

  // Broadcast operations
  void mergeCarts(List<Integer> cartIds);
}
