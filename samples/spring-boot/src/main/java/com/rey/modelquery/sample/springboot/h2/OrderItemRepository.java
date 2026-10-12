package com.rey.modelquery.sample.springboot.h2;

import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data writes for h2 order lines used by the sample fixtures. */
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {}
