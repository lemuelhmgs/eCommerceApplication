package com.example.ecommerce_application.controllers;

import com.example.ecommerce_application.model.persistence.Cart;
import com.example.ecommerce_application.model.persistence.Item;
import com.example.ecommerce_application.model.persistence.User;
import com.example.ecommerce_application.model.persistence.repositories.CartRepository;
import com.example.ecommerce_application.model.persistence.repositories.ItemRepository;
import com.example.ecommerce_application.model.persistence.repositories.UserRepository;
import com.example.ecommerce_application.model.requests.ModifyCartRequest;
import java.util.Optional;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {
	private static final Logger log = LoggerFactory.getLogger(CartController.class);
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@PostMapping("/addToCart")
	public ResponseEntity<Cart> addTocart(@RequestBody ModifyCartRequest request) {
		User user = userRepository.findByUsername(request.getUsername());
		if(user == null) {
			log.error("Item should be added to a user.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(!item.isPresent()) {
			log.error("No item added.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity())
			.forEach(i -> cart.addItem(item.get()));
		cartRepository.save(cart);
		log.info("Item added for ", user.getUsername());
		return ResponseEntity.ok(cart);
	}
	
	@PostMapping("/removeFromCart")
	public ResponseEntity<Cart> removeFromcart(@RequestBody ModifyCartRequest request) {
		User user = userRepository.findByUsername(request.getUsername());
		if(user == null) {
			log.error("Item can only be removed for a specific user.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(!item.isPresent()) {
			log.error("No item was selected to be removed.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity())
			.forEach(i -> cart.removeItem(item.get()));
		cartRepository.save(cart);
		log.info("Item(s) removed from cart.");
		return ResponseEntity.ok(cart);
	}
		
}
