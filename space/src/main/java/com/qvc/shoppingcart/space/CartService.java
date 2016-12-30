package com.qvc.shoppingcart.space;

import com.gigaspaces.document.DocumentProperties;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qvc.shoppingcart.common.Address;
import com.qvc.shoppingcart.common.Cart;
import com.qvc.shoppingcart.common.Cost;
import com.qvc.shoppingcart.common.LineItem;
import com.qvc.shoppingcart.common.PaymentData;
import com.qvc.shoppingcart.service.ICartService;
import com.qvc.shoppingcart.service.ICartTrackerService;
import org.openspaces.core.GigaSpace;
import org.openspaces.remoting.RemotingService;
import org.openspaces.remoting.Routing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// This lives within each partition
@RemotingService
@Transactional
public class CartService implements ICartService {

  @Autowired
  GigaSpace gigaSpace;

  @Autowired
  ICartTrackerService cartTrackerService;

  public String getCart(@Routing long cartId) {
    System.out.printf("GETTING CART [%d]\n", cartId);
    Cart cart = gigaSpace.readById(Cart.class, cartId);
    Long id = cart.getId();
    String user = cart.getUser();
    Gson gson = new Gson();
    List<LineItem> lineItems = cart.getLineItems();
    String itemsJson = gson.toJson(lineItems);
    StringBuilder sb = new StringBuilder("{");
    sb.append("'id':").append(id).append(",").append("'user':").append(user).append(",").append("'items':").append(itemsJson);
    sb.append("}");
    String cartJson = sb.toString();
    System.out.printf("cartJson = %s\n", cartJson);
    return cartJson;
  }

  @Override
  public boolean isCartExist(long cartId) {
    return false;
  }

  public boolean updateCart(@Routing long cartId, String cartJson) {
//    examine the contents of cartPayload
//		gigaSpace.write(shipping, );
//		gigaSpace.write(address);
//		gigaSpace.write(mailing...);
    System.out.printf("Cart [%d] modified using: %s\n", cartId, cartJson);
    return true;
  }

  public void updatePaymentData(long cartId, String paymentJson) {
    PaymentData paymentData = new PaymentData(paymentJson);
    Cart cart = gigaSpace.readById(Cart.class, cartId);
    cart.setPaymentData(paymentData);
    gigaSpace.write(cart);
  }

  public boolean createCart(@Routing long id, String itemListJson) {
    Cart cart = new Cart();
    cart.setId(id);
    JsonParser parser = new JsonParser();
    JsonObject jsonObject = (JsonObject) parser.parse(itemListJson);
    String user = jsonObject.get("user").getAsString();
    cart.setUser(user);
    JsonObject address = jsonObject.getAsJsonObject("address");
    String street = address.get(Address.STREET).getAsString();
    String city = address.get(Address.CITY).getAsString();
    String country = address.get(Address.COUNTRY).getAsString();
    Address shippingAddress = new Address(street, city, country);
    cart.setShippingAddress(shippingAddress);
    JsonObject cost = jsonObject.getAsJsonObject("prize");
    String prizeName = cost.get("name").getAsString();
    Integer amount = cost.get("amount").getAsInt();
    BigDecimal prizeAmount = new BigDecimal(amount);
    DocumentProperties documentProperties = new DocumentProperties();
    documentProperties.setProperty("creationDate", new Date());
    documentProperties.setProperty("user", "sudip");
    Cost prize = new Cost(prizeName, prizeAmount);
    prize.setProperties(documentProperties);
    cart.setPrize(prize);
    JsonArray itemArray = jsonObject.getAsJsonArray("items");
    for (JsonElement item : itemArray) {
      JsonObject itemObject = item.getAsJsonObject();
      String name = itemObject.get(LineItem.NAME).getAsString();
      Integer count = itemObject.get(LineItem.QUANTITY).getAsInt();
      DocumentProperties dpLineItem = new DocumentProperties();
      dpLineItem.put(LineItem.NAME, name);
      dpLineItem.put(LineItem.QUANTITY, count);
      LineItem lineItem = new LineItem(id, dpLineItem);
      List<Cost> discounts = new ArrayList<>();
      JsonArray discountElements = itemObject.getAsJsonArray("discounts");
      for (JsonElement discountElement : discountElements) {
        JsonObject discountObject = discountElement.getAsJsonObject();
        String discountName = discountObject.get("name").getAsString();
        Integer amountOfDiscount = discountObject.get("amount").getAsInt();
        BigDecimal discountAmount = new BigDecimal(amountOfDiscount);
        DocumentProperties documentPropertiesOfDiscount = new DocumentProperties();
        documentPropertiesOfDiscount.setProperty("creationDate", new Date());
        documentPropertiesOfDiscount.setProperty("user", "sudip");
        Cost discount = new Cost(discountName, discountAmount);
        discounts.add(discount);
      }
      lineItem.setDiscounts(discounts);
      cart.addLineItem(lineItem);
      System.out.printf("added lineItem with id [%s]\n", lineItem.getId());
    }
    gigaSpace.write(cart);
    System.out.printf("Cart [%d] created using: %s\n", id, itemListJson);
    return true;
  }

  @Override
  public void scan() {
    cartTrackerService.scan();
  }

  @Override
  public void touch(String lineItemId) {
    cartTrackerService.touch(lineItemId);
  }
}
