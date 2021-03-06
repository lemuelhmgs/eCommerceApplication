package com.example.ecommerce_application.controllers;

import com.example.ecommerce_application.model.persistence.Cart;
import com.example.ecommerce_application.model.persistence.User;
import com.example.ecommerce_application.model.persistence.repositories.CartRepository;
import com.example.ecommerce_application.model.persistence.repositories.UserRepository;
import com.example.ecommerce_application.model.requests.CreateUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		log.info("Getting  user information.");
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		log.info("Creating user for ", user.getUsername());
		Cart cart = new Cart();
		cartRepository.save(cart);
		log.info("Saving cart for ", cart.getUser());
		user.setCart(cart);

		if(createUserRequest.getPassword().length() < 7 ||
						!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword()))		{
			log.error("Password requirement not met.");
			return  ResponseEntity.badRequest().build();
		}

		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));

		userRepository.save(user);
		log.info("Created user successfully.");
		return ResponseEntity.ok(user);
	}
	
}
