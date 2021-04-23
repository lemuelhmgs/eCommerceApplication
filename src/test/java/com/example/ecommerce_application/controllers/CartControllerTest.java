package com.example.ecommerce_application.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.ecommerce_application.TestUtils;
import com.example.ecommerce_application.model.persistence.Cart;
import com.example.ecommerce_application.model.persistence.Item;
import com.example.ecommerce_application.model.persistence.User;
import com.example.ecommerce_application.model.persistence.repositories.CartRepository;
import com.example.ecommerce_application.model.persistence.repositories.ItemRepository;
import com.example.ecommerce_application.model.persistence.repositories.UserRepository;
import com.example.ecommerce_application.model.requests.ModifyCartRequest;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;


public class CartControllerTest {
  private CartController cartController;
  private ModifyCartRequest modifyCartRequest;
  private UserRepository userRepo = mock(UserRepository.class);
  private CartRepository cartRepo = mock(CartRepository.class);
  private ItemRepository itemRepo = mock(ItemRepository.class);

  private User user = new User();
  private Item item = new Item();
  private Cart cart = new Cart();




  @Before
  public void setUp() throws Exception {
    cartController = new CartController();
    modifyCartRequest = new ModifyCartRequest();
    TestUtils.injectObject(cartController, "cartRepository", cartRepo);
    TestUtils.injectObject(cartController, "itemRepository", itemRepo);
    TestUtils.injectObject(cartController, "userRepository", userRepo);

    user.setUsername("test");
    user.setPassword("123456789");
    user.setId(1);
    user.setCart(cart);

    item.setId(1L);
    item.setName("test item");
    item.setPrice(BigDecimal.valueOf(5.78));

    when(userRepo.findByUsername("test")).thenReturn(user);
    when(itemRepo.findById(1L)).thenReturn(Optional.of(item));



  }

  @Test
  public void addToCart_happy_path() {
    modifyCartRequest.setUsername(user.getUsername());
    modifyCartRequest.setItemId(1L);
    modifyCartRequest.setQuantity(1);

    final ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);
    assertEquals(200, responseEntity.getStatusCodeValue());
  }

  @Test
  public void addToCart_with_no_item() {
    modifyCartRequest.setUsername(user.getUsername());
    modifyCartRequest.setQuantity(1);

    final ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);
    assertEquals(404, responseEntity.getStatusCodeValue());
  }

  @Test
  public void addToCart_with_no_user() {
    modifyCartRequest.setUsername(null);
    modifyCartRequest.setItemId(1L);
    modifyCartRequest.setQuantity(1);

    final ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);
    assertEquals(404, responseEntity.getStatusCodeValue());
  }

  @Test
  public void removeFromCart_happy_path() {

    modifyCartRequest.setUsername(user.getUsername());
    modifyCartRequest.setItemId(1L);
    modifyCartRequest.setQuantity(1);

    ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);
    responseEntity = cartController.removeFromcart(modifyCartRequest);
    assertEquals(200, responseEntity.getStatusCodeValue());
    BigDecimal result = new BigDecimal("0.00");
    assertEquals(result, responseEntity.getBody().getTotal());

  }
}