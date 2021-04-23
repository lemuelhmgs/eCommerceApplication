package com.example.ecommerce_application.controllers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.example.ecommerce_application.TestUtils;
import com.example.ecommerce_application.model.persistence.User;
import com.example.ecommerce_application.model.persistence.repositories.CartRepository;
import com.example.ecommerce_application.model.persistence.repositories.UserRepository;
import com.example.ecommerce_application.model.requests.CreateUserRequest;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



public class UserControllerTest {

  private UserController userController;

  private UserRepository userRepo = mock(UserRepository.class);

  private CartRepository cartRepo = mock(CartRepository.class);

  private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);
  private User user = new User();


  @Before
  public void setUp(){
    userController = new UserController();
    TestUtils.injectObject(userController, "userRepository", userRepo);
    TestUtils.injectObject(userController, "cartRepository", cartRepo);
    TestUtils.injectObject(userController, "bCryptPasswordEncoder", encoder);
    user.setUsername("test");
    user.setId(1);
    when(userRepo.findById(1L)).thenReturn(Optional.of(user));
    when(userRepo.findByUsername("test")).thenReturn(user);



  }

  @Test
  public void create_user_happy_path() {

    when(encoder.encode("myPass")).thenReturn("myHash");
    CreateUserRequest createUserRequest = new CreateUserRequest();
    createUserRequest.setUsername("test");
    createUserRequest.setPassword("1234567890");
    createUserRequest.setConfirmPassword("1234567890");

    final ResponseEntity<User> responseEntity = userController.createUser(createUserRequest);

    assertNotNull(responseEntity);
    assertEquals(200, responseEntity.getStatusCodeValue());

    User user = responseEntity.getBody();
    assertNotNull(user);
    assertEquals(0, user.getId());
  }

  @Test
  public void verifyBadPasswordNotAllowed(){
    when(encoder.encode("myPass")).thenReturn("myHash");
    CreateUserRequest createUserRequest = new CreateUserRequest();
    createUserRequest.setUsername("test");
    createUserRequest.setPassword("123456");
    createUserRequest.setConfirmPassword("123456");

    final ResponseEntity<User> responseEntity = userController.createUser(createUserRequest);

    assertNotNull(responseEntity);
    assertEquals(400, responseEntity.getStatusCodeValue());


  }

  @Test
  public void findByUserNameHappyPath(){
    ResponseEntity<User> userResponseEntity = userController.findByUserName("test");
    assertEquals(200, userResponseEntity.getStatusCodeValue());
  }


  @Test
  public void findByUserNameForNonExistenceUser(){
    ResponseEntity<User> userResponseEntity = userController.findByUserName("me");
    assertEquals(404, userResponseEntity.getStatusCodeValue());
  }

  @Test
  public void findByIdHappyPath(){
    ResponseEntity<User> userResponseEntity = userController.findById(1L);
    assertEquals(200, userResponseEntity.getStatusCodeValue());
  }

  @Test
  public void findByIdForNonExistenceUser(){
    ResponseEntity<User> userResponseEntity = userController.findById(10L);
    assertEquals(404, userResponseEntity.getStatusCodeValue());
  }

}
