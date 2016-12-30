package com.qvc.shoppingcart.service;

public interface ICartTrackerService {

  boolean touch(String cartId);

  void scan();

  void remove(String lineItemId);
}
