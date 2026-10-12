package com.rey.modelquery.sample.springboot;

import static com.rey.modelquery.test.QueryAssertions.assertThatQuery;
import static org.assertj.core.api.Assertions.assertThat;

import com.rey.modelquery.core.KeysetSlice;
import com.rey.modelquery.core.KeysetSpec;
import com.rey.modelquery.core.ModelQuery;
import com.rey.modelquery.sample.springboot.h2.OrderRepository;
import com.rey.modelquery.sample.springboot.h2.OrderSearchService;
import com.rey.modelquery.sample.springboot.h2.OrderWithItems;
import com.rey.modelquery.sample.springboot.h2.QItemView;
import com.rey.modelquery.sample.springboot.h2.QOrderWithItems;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

/** Tests the order keyset page query without a datasource or Spring context. */
class OrderSearchServiceTest {

    private final List<ModelQuery<?, ?, OrderWithItems>> capturedQueries = new ArrayList<>();
    private final List<KeysetSpec> capturedKeysets = new ArrayList<>();

    private final OrderSearchService service = new OrderSearchService((OrderRepository) Proxy.newProxyInstance(
            OrderSearchServiceTest.class.getClassLoader(), new Class<?>[] {OrderRepository.class},
            (proxy, method, args) -> {
                if ("findKeysetPage".equals(method.getName())) {
                    capturedQueries.add((ModelQuery<?, ?, OrderWithItems>) args[0]);
                    capturedKeysets.add((KeysetSpec) args[1]);
                    return KeysetSlice.of(List.of(), ((KeysetSpec) args[1]).size(), Optional.empty(),
                            Optional.empty());
                }
                throw new UnsupportedOperationException(method.toString());
            }));

    @Test
    void order_pages_are_keyset_queries_with_items_loaded() {
        service.page(Optional.empty(), Optional.empty(), 3);

        assertThat(capturedKeysets).singleElement().satisfies(keyset -> {
            assertThat(keyset.direction()).isEqualTo(KeysetSpec.Direction.FIRST);
            assertThat(keyset.size()).isEqualTo(3);
        });
        assertThatQuery(capturedQueries.get(0))
                .isOrderedBy(QOrderWithItems.ID.asc())
                .fetchSelectionContains(QOrderWithItems.ID, QOrderWithItems.STATUS)
                .child(QOrderWithItems.ITEMS)
                .selectionContains(QItemView.ID, QItemView.SKU, QItemView.QUANTITY)
                .hasMaxPerParent(10);
    }
}
