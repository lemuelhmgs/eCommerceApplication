package com.example.ecommerce_application.controllers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.ecommerce_application.TestUtils;
import com.example.ecommerce_application.model.persistence.Item;
import com.example.ecommerce_application.model.persistence.repositories.ItemRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.http.ResponseEntity;

public class ItemControllerTest {

  private ItemController itemController;
  private ItemRepository itemRepository = mock(ItemRepository.class);


  @Before
  public void setUp() throws Exception {
    itemController = new ItemController();
    TestUtils.injectObject(itemController, "itemRepository", itemRepository);
    List<Item>  item_1 = new ArrayList<>();
    List<Item>  testItemsForTwoItems = new ArrayList<>();

    Item item1 = new Item();
    item1.setName("test item");
    item1.setId(1L);

    Item item2 = new Item();
    item2.setName("second item");


    item_1.add(item1);
    when(itemRepository.findByName("test item")).thenReturn(item_1);

    when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));

    testItemsForTwoItems.add(item2);
    testItemsForTwoItems.add(item1);
    when(itemRepository.findAll()).thenReturn(testItemsForTwoItems);

  }

  @Test
  public void getItems() {
    ResponseEntity<List<Item>> responseEntity = itemController.getItems();
    assertEquals(200, responseEntity.getStatusCodeValue());
    assertEquals(2,responseEntity.getBody().size());

  }

  @Test
  public void getItemById() {
    ResponseEntity<Item> responseEntity = itemController.getItemById(1L);
    assertEquals(200, responseEntity.getStatusCodeValue());
  }

  @Test
  public void getItemsByName() {
    ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName("test item");
    assertEquals(200, responseEntity.getStatusCodeValue());
    assertEquals(1,responseEntity.getBody().size());
  }

  @Test
  public void getItemsByNameForNonExistenceItem() {
    ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName("My Item");
    assertEquals(404, responseEntity.getStatusCodeValue());
   // assertEquals(1,responseEntity.getBody().size());
  }


}