package com.rey.modelquery.sample.springboot.h2;

import com.rey.modelquery.annotations.Child;
import com.rey.modelquery.annotations.PrimaryKey;
import com.rey.modelquery.annotations.QueryModel;
import java.util.List;

/** An order page row with its bounded item list loaded by a fetch plan. */
@QueryModel(root = OrderEntity.class)
public record OrderWithItems(
        @PrimaryKey Long id,
        String status,
        @Child(foreignKey = "order.id") List<ItemView> items) {}
