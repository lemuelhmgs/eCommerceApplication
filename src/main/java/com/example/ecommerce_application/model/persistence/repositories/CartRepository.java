package com.example.ecommerce_application.model.persistence.repositories;

import com.example.ecommerce_application.model.persistence.Cart;
import com.example.ecommerce_application.model.persistence.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
	Cart findByUser(User user);
}
