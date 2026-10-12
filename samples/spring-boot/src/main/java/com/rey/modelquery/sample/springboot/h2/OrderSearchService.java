package com.rey.modelquery.sample.springboot.h2;

import com.rey.modelquery.core.FetchPlan;
import com.rey.modelquery.core.KeysetSlice;
import com.rey.modelquery.core.KeysetSpec;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/** Builds keyset pages of orders for the h2 sample API. */
@Service
public class OrderSearchService {

    private final OrderRepository orders;

    public OrderSearchService(OrderRepository orders) {
        this.orders = orders;
    }

    /** One keyset page of orders and the cursors that reach its neighbours. */
    public record OrderPage(List<OrderWithItems> orders, String next, String previous) {}

    /** A keyset page ordered by order id, with the order lines fetched for each order in the page. */
    public OrderPage page(Optional<String> after, Optional<String> before, int size) {
        var plan = FetchPlan.of(QOrderWithItems.ALL)
                .child(QOrderWithItems.ITEMS, FetchPlan.of(QItemView.ALL), c -> c.maxPerParent(10));
        var query = QOrderWithItems.query()
                .fetch(plan)
                .orderBy(QOrderWithItems.ID.asc())
                .keyset()
                .build();
        KeysetSpec keyset = after.map(cursor -> KeysetSpec.after(cursor, size))
                .or(() -> before.map(cursor -> KeysetSpec.before(cursor, size)))
                .orElseGet(() -> KeysetSpec.first(size));
        KeysetSlice<OrderWithItems> slice = orders.findKeysetPage(query, keyset, Sort.unsorted());
        return new OrderPage(slice.content(), slice.nextCursor().orElse(null), slice.previousCursor().orElse(null));
    }
}
