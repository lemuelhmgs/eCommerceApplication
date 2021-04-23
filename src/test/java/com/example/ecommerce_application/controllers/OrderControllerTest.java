package com.example.ecommerce_application.controllers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.ecommerce_application.TestUtils;
import com.example.ecommerce_application.model.persistence.Cart;
import com.example.ecommerce_application.model.persistence.Item;
import com.example.ecommerce_application.model.persistence.User;
import com.example.ecommerce_application.model.persistence.UserOrder;
import com.example.ecommerce_application.model.persistence.repositories.OrderRepository;
import com.example.ecommerce_application.model.persistence.repositories.UserRepository;
import com.example.ecommerce_application.model.requests.ModifyCartRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

public class OrderControllerTest {
  private OrderController orderController;
  private OrderRepository orderRepository = mock(OrderRepository.class);
  private UserRepository userRepository = mock(UserRepository.class);

  private User user = new User();
  private Item item = new Item();
  private Cart cart = new Cart();
  List<Item> itemList = new ArrayList<>();


  @Before
  public void setUp() throws Exception {
    orderController = new OrderController();
    TestUtils.injectObject(orderController, "orderRepository", orderRepository);
    TestUtils.injectObject(orderController, "userRepository", userRepository);


    user.setUsername("test");
    user.setPassword("123456789");
    user.setId(1L);
    when(userRepository.findByUsername("test")).thenReturn(user);

    item.setId(1L);
    item.setName("test item");
    item.setPrice(BigDecimal.valueOf(5.78));
    itemList.add(item);

    cart.setId(1L);
    cart.setItems(itemList);
    cart.setUser(user);
    cart.setTotal(item.getPrice());
    user.setCart(cart);

  }

  @Test
  public void submit_happy_path() {
    ResponseEntity<UserOrder> responseEntity = orderController.submit("test");
    assertEquals(200, responseEntity.getStatusCodeValue());
    assertEquals(1, responseEntity.getBody().getItems().size());
  }

  @Test
  public void submit_without_user() {
    ResponseEntity<UserOrder> responseEntity = orderController.submit(null);
    assertEquals(404, responseEntity.getStatusCodeValue());
  }

  @Test
  public void getOrdersForUser_happy_path() {
    ResponseEntity<List<UserOrder>>  responseEntity = orderController.getOrdersForUser("test");
    assertEquals(200, responseEntity.getStatusCodeValue());
  }

  @Test
  public void getOrdersForUser_that_doesnot_exist() {
    ResponseEntity<List<UserOrder>>  responseEntity = orderController.getOrdersForUser("te");
    assertEquals(404, responseEntity.getStatusCodeValue());
  }


}