package com.rey.modelquery.sample.springboot.h2;

import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** The h2 order endpoints. */
@RestController
class OrderController {

    private final OrderSearchService search;

    OrderController(OrderSearchService search) {
        this.search = search;
    }

    /** A keyset page of orders: {@code after} or {@code before} names a cursor, neither is the first page. */
    @GetMapping("/orders/pages")
    OrderSearchService.OrderPage pages(@RequestParam(required = false) String after,
            @RequestParam(required = false) String before, @RequestParam(defaultValue = "20") int size) {
        return search.page(Optional.ofNullable(after), Optional.ofNullable(before), size);
    }
}
