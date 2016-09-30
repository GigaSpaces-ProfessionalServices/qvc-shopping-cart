package com.qvc.shoppingcart.rest;

import com.gigaspaces.document.SpaceDocument;
import com.qvc.shoppingcart.common.Cart;
import com.qvc.shoppingcart.service.ICartService;
import org.openspaces.remoting.ExecutorProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@RestController
public class CartController {

  @ExecutorProxy
  private ICartService cartService;

  @RequestMapping(value = "/cart/{id}", method = RequestMethod.GET)
  public String getCart(
          @PathVariable("id") int id,
          HttpServletResponse response
  ) throws IOException
  {
    System.out.println("Fetching cart with id " + id);

    PrintWriter pw = response.getWriter();

    String cartJson = cartService.getCart(id);
    pw.print(cartJson);

//    pw.printf("Cart with id [%d]", id);

    pw.flush();

//    Cart cart = cartService.getCart(id);
//    if (cart == null) {
//      System.out.println("Cart with id " + id + " not found");
//      return new ResponseEntity<Cart>(HttpStatus.NOT_FOUND);
//    }

    pw.close();

    return null;
  }

  @RequestMapping(value = "/cart/{id}", method = RequestMethod.POST)
  public String createCart(
          @PathVariable("id") int id,
          @RequestBody String cartJson,
          HttpServletResponse response
  ) throws IOException
  {
    System.out.printf("cartJson => %s\n", cartJson);

    boolean success = cartService.createCart(id, cartJson);

    PrintWriter pw = response.getWriter();

    if (success) {
      pw.printf("Cart created:\n\n%s", cartJson);
    } else {
      pw.printf("Cart not created:\n\n%s", cartJson);
    }
    pw.flush();

    pw.close();

//    int cartId = getCartId(cartJson);
//    System.out.println("Creating cart " + cartId);
//
//    if (cartService.isCartExist(cartId)) {
//      System.out.println("A cart with id " + cartId + " already exists");
//      return new ResponseEntity<Void>(HttpStatus.CONFLICT);
//    }
//
//    SpaceDocument cartPayload = createPayload(cartJson);
//
//    cartService.createCart(cartId, cartPayload);

    return null;
  }

  // TODO
  private SpaceDocument createPayload(String cartJson) {
    return null;
  }

  // TODO
  private int getCartId(String cartJson) {
    return 0;
  }

  @RequestMapping(value = "/cart/{id}", method = RequestMethod.PUT)
  public String updateCart(
          @PathVariable("id") int id,
          @RequestBody String cartJson,
          HttpServletResponse response
  ) throws IOException
  {

    System.out.println("Updating cart " + id);

    System.out.printf("cartJson => %s\n", cartJson);

    PrintWriter pw = response.getWriter();

    pw.println(cartJson);
    pw.flush();

    pw.close();

//    Cart currentCart = cartService.getCart(id);
//
//    if (currentCart==null) {
//      System.out.println("Cart with id " + id + " not found");
//      return new ResponseEntity<Cart>(HttpStatus.NOT_FOUND);
//    }
//
//    SpaceDocument cartPayload = createPayload(currentCart);
//
//    cartService.updateCart(id, cartPayload);

    return null;
  }

  // TODO
  private SpaceDocument createPayload(Cart currentCart) {
    return null;
  }
}
