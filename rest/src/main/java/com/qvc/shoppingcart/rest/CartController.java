package com.qvc.shoppingcart.rest;

import com.gigaspaces.document.SpaceDocument;
import com.qvc.shoppingcart.common.Cart;
import com.qvc.shoppingcart.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class CartController {

  @Autowired
  private ICartService cartService;

  @RequestMapping(value = "/cart/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Cart> getCart(@PathVariable("id") int id) {
    System.out.println("Fetching cart with id " + id);
    Cart cart = cartService.getCart(id);
    if (cart == null) {
      System.out.println("User with id " + id + " not found");
      return new ResponseEntity<Cart>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<Cart>(cart, HttpStatus.OK);
  }

  @RequestMapping(value = "/cart/", method = RequestMethod.POST)
  public ResponseEntity<Void> createCart(@RequestParam String cartJson) {

    int cartId = getCartId(cartJson);
    System.out.println("Creating cart " + cartId);

    if (cartService.isCartExist(cartId)) {
      System.out.println("A cart with id " + cartId + " already exists");
      return new ResponseEntity<Void>(HttpStatus.CONFLICT);
    }

    SpaceDocument cartPayload = createPayload(cartJson);

    cartService.createCart(cartId, cartPayload);

    HttpHeaders headers = new HttpHeaders();
    return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
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
  public ResponseEntity<Cart> updateCart(@PathVariable("id") int id, @RequestParam String cartJson) {
    System.out.println("Updating User " + id);

    Cart currentCart = cartService.getCart(id);

    if (currentCart==null) {
      System.out.println("Cart with id " + id + " not found");
      return new ResponseEntity<Cart>(HttpStatus.NOT_FOUND);
    }

    SpaceDocument cartPayload = createPayload(currentCart);

    cartService.updateCart(id, cartPayload);
    return new ResponseEntity<Cart>(currentCart, HttpStatus.OK);
  }

  // TODO
  private SpaceDocument createPayload(Cart currentCart) {
    return null;
  }
}
