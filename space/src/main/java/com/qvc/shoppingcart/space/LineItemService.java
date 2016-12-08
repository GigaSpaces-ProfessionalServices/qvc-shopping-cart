package com.qvc.shoppingcart.space;

import com.gigaspaces.document.DocumentProperties;
import com.j_spaces.core.LeaseContext;
import com.j_spaces.core.client.SQLQuery;
import com.qvc.shoppingcart.common.Cart;
import com.qvc.shoppingcart.common.LineItem;
import com.qvc.shoppingcart.common.LineItemTracker;
import com.qvc.shoppingcart.service.ILineItemService;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.transaction.manager.DistributedJiniTxManagerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * Created by sudip on 11/22/2016.
 */
@Component
public class LineItemService implements ILineItemService {

  public static final long LIFETIME = 1000L * 180; /* 1000L*60*60*24 = 1 day */

//  public static final long LIFETIME = 86400000; /* 1000L*60*60*24 = 1 day */

  @Autowired
  GigaSpace gigaSpace;

  @Override
  public boolean touch(String lineItemId) {
    System.out.printf("LineItemService => lineItemId = [%s]\n", lineItemId);
    LineItemTracker lineItemTracker = gigaSpace.readById(LineItemTracker.class, lineItemId);
    long expirationTimestamp = System.currentTimeMillis() + LIFETIME;
    System.out.printf("New Expiration Timestamp = [%d]\n", expirationTimestamp);
    lineItemTracker.setExpirationTimestamp(expirationTimestamp);
    gigaSpace.write(lineItemTracker);
    return false;
  }

  @Override
  public void scan() {
    long currentTimestamp = System.currentTimeMillis();
    String queryStr = "expirationTimestamp < " + Long.toString(currentTimestamp);
    System.out.printf("queryStr = [%s]\n", queryStr);
    try {
      PlatformTransactionManager ptm = new DistributedJiniTxManagerConfigurer().transactionManager();
      DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
      definition.setPropagationBehavior(Propagation.REQUIRES_NEW.ordinal());
      TransactionStatus status = ptm.getTransaction(definition);
      try {
        SQLQuery<LineItemTracker> query = new SQLQuery<>(LineItemTracker.class, queryStr);
        LineItemTracker[] lineItemTrackers = gigaSpace.takeMultiple(query);
        System.out.printf("scan query returned %d matches\n", lineItemTrackers.length);
        for (LineItemTracker lineItemTracker : lineItemTrackers) {
          String lineItemId = lineItemTracker.getLineItemId();
          LineItem lineItem = gigaSpace.takeById(LineItem.class, lineItemId);
          long cartId = lineItem.getCartId();
          Cart cart = gigaSpace.takeById(Cart.class, cartId);
          cart.dropLineItem(lineItemId);
          gigaSpace.write(cart);
        }
        ptm.commit(status);
      } catch (Exception e) {
        ptm.rollback(status);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public String create(long cartId, DocumentProperties data) {
    String lineItemId = null;
    LineItem lineItem = new LineItem(cartId, data);
    long expirationTimestamp = System.currentTimeMillis() + LIFETIME;
    System.out.printf("expirationTimestamp = %d", expirationTimestamp);
    LineItemTracker lineItemTracker = new LineItemTracker(lineItem.getId(), expirationTimestamp);
    try {
      PlatformTransactionManager ptm = new DistributedJiniTxManagerConfigurer().transactionManager();
      DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
      definition.setPropagationBehavior(Propagation.REQUIRES_NEW.ordinal());
      TransactionStatus status = ptm.getTransaction(definition);
      System.out.println("creating line items ...");
      try {
        LeaseContext<LineItem> lc = gigaSpace.write(lineItem);
        lineItemId = lc.getUID();
        System.out.printf("created line item with id ", lineItemId);
        LeaseContext<LineItemTracker> litLease = gigaSpace.write(lineItemTracker);
        String litId = litLease.getUID();
        System.out.printf("created line item tracker with id %s\n", litId);
        ptm.commit(status);
        System.out.println("created line items.");
      } catch (Exception e) {
        System.out.println("error occurred creating lineitems: " + e.getMessage());
        ptm.rollback(status);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return lineItemId;
  }

  @Override
  public void remove(String lineItemId) {
    try {
      PlatformTransactionManager ptm = new DistributedJiniTxManagerConfigurer().transactionManager();
      DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
      definition.setPropagationBehavior(Propagation.REQUIRES_NEW.ordinal());
      TransactionStatus status = ptm.getTransaction(definition);
      try {
        LineItem lineItem = gigaSpace.takeById(LineItem.class, lineItemId);
        long cartId = lineItem.getCartId();
        Cart cart = gigaSpace.takeById(Cart.class, cartId);
        cart.dropLineItem(lineItemId);
        gigaSpace.write(cart);
        ptm.commit(status);
      } catch (Exception e) {
        ptm.rollback(status);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
