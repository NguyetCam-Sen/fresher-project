package com.vcncam.orderservice.repository;

import com.vcncam.orderservice.model.OrderLineItems;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderLineItemsRepository extends JpaRepository<OrderLineItems, Long> {
    Optional<List<OrderLineItems>> findByOrderId(Long orderId);

    @Transactional
    void deleteAllByOrderId(Long orderId);
}
