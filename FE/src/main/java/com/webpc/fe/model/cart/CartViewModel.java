package com.webpc.fe.model.cart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartViewModel {

    private List<CartItemViewModel> items = new ArrayList<>();

    public BigDecimal getTotalAmount() {
        return items.stream()
            .map(CartItemViewModel::getTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getTotalQuantity() {
        return items.stream().mapToInt(CartItemViewModel::getQuantity).sum();
    }
}
