package com.rey.modelquery.sample.springboot.h2;

import com.rey.modelquery.annotations.PrimaryKey;
import com.rey.modelquery.annotations.QueryModel;

/** A model of one line in an order page. */
@QueryModel(root = OrderItemEntity.class)
public record ItemView(@PrimaryKey Long id, String sku, Integer quantity) {}
