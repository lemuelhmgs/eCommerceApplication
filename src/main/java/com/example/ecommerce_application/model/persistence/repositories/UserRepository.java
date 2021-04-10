package com.example.ecommerce_application.model.persistence.repositories;

import com.example.ecommerce_application.model.persistence.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
