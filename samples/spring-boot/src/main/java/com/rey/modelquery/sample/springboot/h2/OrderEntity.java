package com.rey.modelquery.sample.springboot.h2;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** An order in the h2 shop sample. */
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    Long id;
    String status;
    BigDecimal total;
    @OneToMany(mappedBy = "order")
    List<OrderItemEntity> items = new ArrayList<>();

    protected OrderEntity() {
    }

    public OrderEntity(Long id, String status, String total) {
        this.id = id;
        this.status = status;
        this.total = new BigDecimal(total);
    }
}
