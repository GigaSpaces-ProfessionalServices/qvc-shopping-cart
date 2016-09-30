package com.qvc.shoppingcart.space;

import com.gigaspaces.document.DocumentProperties;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qvc.shoppingcart.common.Cart;
import com.qvc.shoppingcart.service.ICartService;
import org.openspaces.core.GigaSpace;
import org.openspaces.remoting.RemotingService;
import org.openspaces.remoting.Routing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

// This lives within each partition
@RemotingService
@Transactional
public class CartService implements ICartService {

  @Autowired
  GigaSpace gigaSpace;

  public String getCart(@Routing int cartId) {
    System.out.printf("GETTING CART [%d]\n", cartId);
    Cart cart = gigaSpace.readById(Cart.class, cartId);
    Integer id = cart.getId();
    String user = cart.getUser();
    Gson gson = new Gson();
    String itemsJson = gson.toJson(cart.getPayload().get("items"));
    StringBuilder sb = new StringBuilder("{");
    sb.append("'id':").append(id).append(",").append("'user':").append(user).append(",").append("'items':").append(itemsJson);
    sb.append("}");
    String cartJson = sb.toString();
    System.out.printf("cartJson = %s\n", cartJson);
    return cartJson;
  }

  @Override
  public boolean isCartExist(int cartId) {
    return false;
  }

  @Override
  public void mergeCarts(List<Integer> cartIds) {
  }

  public boolean updateCart(@Routing int cartId, String cartJson) {
//    examine the contents of cartPayload
//		gigaSpace.write(shipping, );
//		gigaSpace.write(address);
//		gigaSpace.write(mailing...);
    System.out.printf("Cart [%d] modified using: %s\n", cartId, cartJson);
    return true;
  }

  public boolean createCart(int id, String payloadJson) {
    Cart cart = new Cart();
    cart.setId(id);
    DocumentProperties dpPayload = new DocumentProperties();
    JsonParser parser = new JsonParser();
    JsonObject jsonObject = (JsonObject) parser.parse(payloadJson);
    String user = jsonObject.get("user").getAsString();
    cart.setUser(user);
    JsonArray itemArray = jsonObject.getAsJsonArray("items");
    List<DocumentProperties> dpItemList = new LinkedList<>();
    for (JsonElement item : itemArray) {
      JsonObject itemObject = item.getAsJsonObject();
      String name = itemObject.get("name").getAsString();
      Integer count = itemObject.get("count").getAsInt();
      DocumentProperties dpItem = new DocumentProperties();
      dpItem.put("name", name);
      dpItem.put("count", count);
      dpItemList.add(dpItem);
    }
    dpPayload.put("items", dpItemList);
    cart.setPayload(dpPayload);
    gigaSpace.write(cart);
    System.out.printf("Cart [%d] created using: %s\n", id, payloadJson);
    return true;
  }

}
