package com.qvc.shoppingcart.service;

import com.gigaspaces.document.DocumentProperties;
import com.qvc.shoppingcart.common.LineItem;

/**
 * Created by sudip on 11/22/2016.
 */
public interface ILineItemService {

  boolean touch(String lineItemId);

  void scan();

  String create(long cartId, DocumentProperties data);

  void remove(String lineItemId);
}
