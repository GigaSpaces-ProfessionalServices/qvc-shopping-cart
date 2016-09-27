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

  public String toJson() {
    StringBuilder sb = new StringBuilder("{");
    sb.append("user:'").append(user).append("',");
    sb.append("id:").append(id).append(",");
    String payloadJson = getJsonFromDocumentProperties(payload);
    sb.append("payload:").append(payloadJson);
    sb.append("}");
    return sb.toString();
  }

  // TODO
  private String getJsonFromDocumentProperties(DocumentProperties payload) {
    return null;
  }
}
