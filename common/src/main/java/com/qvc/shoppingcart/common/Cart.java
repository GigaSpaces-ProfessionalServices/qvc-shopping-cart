package com.qvc.shoppingcart.common;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceRouting;

import java.util.ArrayList;
import java.util.List;

@SpaceClass
public class Cart {

  private String user;
  private Long id;
  private List<String> lineItemIds;
  private PaymentData paymentData;
  private Address shippingAddress;

  public Cart() {
    lineItemIds = new ArrayList<>();
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  @SpaceId
  @SpaceRouting
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public PaymentData getPaymentData() {
    return paymentData;
  }

  public void setPaymentData(PaymentData paymentData) {
    this.paymentData = paymentData;
  }

  public void setLineItemIds(List<String> lineItemIds) {
    this.lineItemIds = lineItemIds;
  }

  public Address getShippingAddress() {
    return shippingAddress;
  }

  public void setShippingAddress(Address shippingAddress) {
    this.shippingAddress = shippingAddress;
  }

  public boolean dropLineItem(String lineItemId) {
    boolean rv = false;
    String idOfLineItemToBeRemoved = null;
    for (String lid : lineItemIds) {
      if (lid.equals(lineItemId)) {
        idOfLineItemToBeRemoved = lid;
        break;
      }
    }
    if (idOfLineItemToBeRemoved != null) {
      lineItemIds.remove(idOfLineItemToBeRemoved);
      rv = true;
    }
    return rv;
  }

  public void addLineItemId(String lineItemId) {
    lineItemIds.add(lineItemId);
  }

  public List<String> getLineItemIds() {
    return lineItemIds;
  }
}
