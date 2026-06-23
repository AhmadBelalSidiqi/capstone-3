package org.yearup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yearup.models.Order;

@Repository
interface OrderRepository extends JpaRepository<Order, Integer> {
}
