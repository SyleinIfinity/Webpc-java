package com.webpc.fe.model.cart;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartItemViewModel {

    private Integer cartItemId;
    private Integer productId;
    private String productName;
    private String productImage;
    private BigDecimal originalPrice = BigDecimal.ZERO;
    private BigDecimal promotionPrice = BigDecimal.ZERO;
    private int quantity = 1;

    public BigDecimal getPrice() {
        if (promotionPrice != null && promotionPrice.compareTo(BigDecimal.ZERO) > 0) {
            return promotionPrice;
        }
        return originalPrice == null ? BigDecimal.ZERO : originalPrice;
    }

    public BigDecimal getTotal() {
        return getPrice().multiply(BigDecimal.valueOf(Math.max(quantity, 0)));
    }
}
