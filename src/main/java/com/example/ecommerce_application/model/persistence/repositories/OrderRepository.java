package com.example.ecommerce_application.model.persistence.repositories;

import com.example.ecommerce_application.model.persistence.User;
import com.example.ecommerce_application.model.persistence.UserOrder;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<UserOrder, Long> {
	List<UserOrder> findByUser(User user);
}
