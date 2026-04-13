package com.webpc.fe.servlet;

import com.fasterxml.jackson.databind.JsonNode;
import com.webpc.fe.common.BaseServlet;
import com.webpc.fe.service.CartSessionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {
    "/Cart",
    "/Cart/Index",
    "/Cart/AddToCart",
    "/Cart/UpdateQuantity",
    "/Cart/AddBuildPCToCart",
    "/Cart/Remove/*"
})
public class CartServlet extends BaseServlet {

    private final CartSessionService cartSessionService = new CartSessionService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/Cart/Remove".equals(path)) {
            handleRemove(request, response);
            return;
        }

        if (!requireLogin(request, response)) {
            return;
        }

        request.setAttribute("pageTitle", "Giỏ hàng");
        request.setAttribute("activeNav", "cart");
        request.setAttribute("cart", cartSessionService.getCart(request.getSession()));
        render(request, response, "cart/index.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String path = request.getServletPath();
        switch (path) {
            case "/Cart/AddToCart" -> handleAddToCart(request, response);
            case "/Cart/UpdateQuantity" -> handleUpdateQuantity(request, response);
            case "/Cart/AddBuildPCToCart" -> handleAddBuildPcToCart(request, response);
            default -> response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleAddToCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!requireLogin(request, response)) {
            return;
        }

        Integer productId = parseNullableInt(request.getParameter("productId"));
        int quantity = parseInt(request.getParameter("quantity"), 1);
        String type = request.getParameter("type");

        if (productId == null || productId <= 0) {
            flashError(request, "Mã sản phẩm không hợp lệ.");
            redirect(request, response, "/Product");
            return;
        }

        var item = cartSessionService.addProductById(request.getSession(), productId, quantity);
        flashSuccess(request, "Đã thêm sản phẩm vào giỏ hàng.");

        if ("buy_now".equalsIgnoreCase(type)) {
            redirect(request, response, "/ThanhToan/Checkout?selectedIds=" + item.getCartItemId());
            return;
        }

        String referer = request.getHeader("Referer");
        if (referer != null && referer.contains(request.getContextPath() + "/Product/Detail")) {
            redirect(request, response, "/Product/Detail?id=" + productId);
            return;
        }
        redirect(request, response, "/Cart");
    }

    private void handleRemove(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!requireLogin(request, response)) {
            return;
        }

        Integer productId = parseProductIdFromRemovePath(request);
        if (productId == null || productId <= 0) {
            flashError(request, "Không thể xác định sản phẩm cần xóa.");
            redirect(request, response, "/Cart");
            return;
        }

        boolean removed = cartSessionService.removeProduct(request.getSession(), productId);
        if (removed) {
            flashSuccess(request, "Đã xóa sản phẩm khỏi giỏ hàng.");
        } else {
            flashError(request, "Không tìm thấy sản phẩm trong giỏ hàng.");
        }
        redirect(request, response, "/Cart");
    }

    private void handleUpdateQuantity(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (currentUser(request) == null) {
            writeJson(response, Map.of(
                "success", false,
                "requireLogin", true,
                "message", "Phiên đăng nhập hết hạn."
            ));
            return;
        }

        JsonNode body = OBJECT_MAPPER.readTree(request.getInputStream());
        int productId = body.path("productId").asInt(0);
        int quantity = body.path("quantity").asInt(0);

        boolean updated = cartSessionService.updateQuantity(request.getSession(), productId, quantity);
        if (!updated) {
            writeJson(response, Map.of("success", false, "message", "Không thể cập nhật số lượng."));
            return;
        }
        writeJson(response, Map.of("success", true));
    }

    private void handleAddBuildPcToCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (currentUser(request) == null) {
            writeJson(response, Map.of(
                "success", false,
                "url", request.getContextPath() + "/Login"
            ));
            return;
        }

        JsonNode body = OBJECT_MAPPER.readTree(request.getInputStream());
        List<Integer> productIds = new ArrayList<>();
        for (JsonNode item : body.path("productIds")) {
            int value = item.asInt(0);
            if (value > 0) {
                productIds.add(value);
            }
        }

        if (productIds.isEmpty()) {
            writeJson(response, Map.of("success", false, "message", "Danh sách sản phẩm trống."));
            return;
        }

        List<Integer> cartItemIds = new ArrayList<>();
        for (Integer productId : productIds) {
            var added = cartSessionService.addProductById(request.getSession(), productId, 1);
            cartItemIds.add(added.getCartItemId());
        }

        String selectedIds = cartItemIds.stream().map(String::valueOf).reduce((a, b) -> a + "," + b).orElse("");
        writeJson(response, Map.of(
            "success", true,
            "url", request.getContextPath() + "/ThanhToan/Checkout?selectedIds=" + selectedIds
        ));
    }

    private Integer parseProductIdFromRemovePath(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            try {
                return Integer.parseInt(pathInfo.substring(1));
            } catch (NumberFormatException ignored) {
                return null;
            }
        }
        return parseNullableInt(request.getParameter("id"));
    }
}
