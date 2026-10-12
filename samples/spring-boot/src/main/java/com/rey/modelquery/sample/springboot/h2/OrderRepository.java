package com.rey.modelquery.sample.springboot.h2;

import com.rey.modelquery.spring.data.ModelQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data plus model-query reads for h2 orders. */
public interface OrderRepository extends JpaRepository<OrderEntity, Long>, ModelQueryRepository<OrderEntity> {}
