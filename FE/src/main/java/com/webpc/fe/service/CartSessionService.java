package com.webpc.fe.service;

import com.webpc.fe.common.SessionKeys;
import com.webpc.fe.model.cart.CartItemViewModel;
import com.webpc.fe.model.cart.CartViewModel;
import com.webpc.fe.model.catalog.ProductViewModel;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CartSessionService {

    private final CatalogService catalogService = new CatalogService();

    public CartViewModel getCart(HttpSession session) {
        if (session == null) {
            return new CartViewModel();
        }
        Object value = session.getAttribute(SessionKeys.CART);
        if (value instanceof CartViewModel cart) {
            if (cart.getItems() == null) {
                cart.setItems(new ArrayList<>());
            }
            return cart;
        }
        CartViewModel cart = new CartViewModel();
        session.setAttribute(SessionKeys.CART, cart);
        return cart;
    }

    public CartItemViewModel addProductById(HttpSession session, int productId, int quantity) {
        ProductViewModel product = catalogService.getProduct(productId);
        return addProduct(session, product, quantity);
    }

    public CartItemViewModel addProduct(HttpSession session, ProductViewModel product, int quantity) {
        CartViewModel cart = getCart(session);
        int safeQuantity = Math.max(quantity, 1);

        CartItemViewModel existing = cart.getItems().stream()
            .filter(item -> item.getProductId() != null && item.getProductId().equals(product.getMaSanPham()))
            .findFirst()
            .orElse(null);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + safeQuantity);
            return existing;
        }

        CartItemViewModel item = new CartItemViewModel();
        item.setCartItemId(nextCartItemId(cart));
        item.setProductId(product.getMaSanPham());
        item.setProductName(product.getTenSanPham());
        item.setProductImage(product.getHinhAnhDaiDien());
        item.setOriginalPrice(zeroIfNull(product.getGiaBan()));
        item.setPromotionPrice(zeroIfNull(product.getGiaKhuyenMai()));
        item.setQuantity(safeQuantity);
        cart.getItems().add(item);
        return item;
    }

    public boolean removeProduct(HttpSession session, int productId) {
        CartViewModel cart = getCart(session);
        return cart.getItems().removeIf(item -> item.getProductId() != null && item.getProductId() == productId);
    }

    public boolean updateQuantity(HttpSession session, int productId, int quantity) {
        if (quantity <= 0) {
            return false;
        }
        CartViewModel cart = getCart(session);
        CartItemViewModel item = cart.getItems().stream()
            .filter(x -> x.getProductId() != null && x.getProductId() == productId)
            .findFirst()
            .orElse(null);
        if (item == null) {
            return false;
        }
        item.setQuantity(quantity);
        return true;
    }

    public List<CartItemViewModel> getSelectedItems(HttpSession session, List<Integer> cartItemIds) {
        if (cartItemIds == null || cartItemIds.isEmpty()) {
            return List.of();
        }
        CartViewModel cart = getCart(session);
        return cart.getItems().stream()
            .filter(item -> item.getCartItemId() != null && cartItemIds.contains(item.getCartItemId()))
            .toList();
    }

    public void removeCartItems(HttpSession session, List<Integer> cartItemIds) {
        if (cartItemIds == null || cartItemIds.isEmpty()) {
            return;
        }
        CartViewModel cart = getCart(session);
        cart.getItems().removeIf(item -> item.getCartItemId() != null && cartItemIds.contains(item.getCartItemId()));
    }

    private Integer nextCartItemId(CartViewModel cart) {
        return cart.getItems().stream()
            .map(CartItemViewModel::getCartItemId)
            .filter(id -> id != null)
            .max(Comparator.naturalOrder())
            .orElse(0) + 1;
    }

    private BigDecimal zeroIfNull(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
