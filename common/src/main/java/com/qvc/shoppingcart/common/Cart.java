package com.qvc.shoppingcart.common;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceDynamicProperties;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceRouting;
import com.gigaspaces.document.DocumentProperties;

@SpaceClass
public class Cart {

  private String user;
  private Integer id;
  private DocumentProperties payload;
  private PaymentData paymentData;

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  @SpaceId
  @SpaceRouting
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @SpaceDynamicProperties
  public DocumentProperties getPayload() {
    return payload;
  }

  public void setPayload(DocumentProperties payload) {
    this.payload = payload;
  }

  public PaymentData getPaymentData() {
    return paymentData;
  }

  public void setPaymentData(PaymentData paymentData) {
    this.paymentData = paymentData;
  }

}
