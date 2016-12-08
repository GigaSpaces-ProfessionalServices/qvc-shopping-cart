package com.qvc.shoppingcart.common;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceDynamicProperties;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceRouting;
import com.gigaspaces.document.DocumentProperties;

import java.util.UUID;

@SpaceClass
public class LineItem {

  public static final String NAME = "name";
  public static final String QUANTITY = "count";

  private String id;

  private long cartId;

  private DocumentProperties data;

  public LineItem(long cartId, DocumentProperties data) {
    this.id = UUID.randomUUID().toString();
    this.cartId = cartId;
    this.data = data;
  }

  public LineItem() {
  }

  @SpaceId(autoGenerate = true)
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @SpaceRouting
  public long getCartId() {
    return cartId;
  }

  public void setCartId(long cartId) {
    this.cartId = cartId;
  }

  @SpaceDynamicProperties
  public DocumentProperties getData() {
    return data;
  }

  public void setData(DocumentProperties data) {
    this.data = data;
  }

}
