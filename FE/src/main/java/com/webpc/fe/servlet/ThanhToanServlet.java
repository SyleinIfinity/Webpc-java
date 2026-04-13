package com.webpc.fe.servlet;

import com.webpc.fe.common.BaseServlet;
import com.webpc.fe.model.cart.CartViewModel;
import com.webpc.fe.model.payment.ApiStatusResponse;
import com.webpc.fe.model.payment.CheckoutViewModel;
import com.webpc.fe.model.promotion.KhuyenMaiKhachHangResponse;
import com.webpc.fe.service.AddressService;
import com.webpc.fe.service.CartSessionService;
import com.webpc.fe.service.OrderSessionService;
import com.webpc.fe.service.PromotionSessionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {
    "/ThanhToan/Checkout",
    "/ThanhToan/PlaceOrder",
    "/ThanhToan/Payment",
    "/ThanhToan/CheckStatus",
    "/ThanhToan/ConfirmPayment",
    "/ThanhToan/Success"
})
public class ThanhToanServlet extends BaseServlet {

    private static final BigDecimal SHIPPING_FEE = new BigDecimal("30000");

    private final CartSessionService cartService = new CartSessionService();
    private final OrderSessionService orderService = new OrderSessionService();
    private final PromotionSessionService promotionService = new PromotionSessionService();
    private final AddressService addressService = new AddressService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        switch (path) {
            case "/ThanhToan/Checkout" -> showCheckout(request, response);
            case "/ThanhToan/Payment" -> showPayment(request, response);
            case "/ThanhToan/CheckStatus" -> checkStatus(request, response);
            case "/ThanhToan/Success" -> showSuccess(request, response);
            default -> response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String path = request.getServletPath();
        switch (path) {
            case "/ThanhToan/PlaceOrder" -> placeOrder(request, response);
            case "/ThanhToan/ConfirmPayment" -> confirmPayment(request, response);
            default -> response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void showCheckout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!requireLogin(request, response)) {
            return;
        }

        String selectedIdsString = valueOrEmpty(request.getParameter("selectedIds"));
        List<Integer> selectedIds = parseIds(selectedIdsString);
        if (selectedIds.isEmpty()) {
            flashError(request, "Vui lòng chọn sản phẩm để thanh toán.");
            redirect(request, response, "/Cart");
            return;
        }

        CartViewModel rawCart = cartService.getCart(request.getSession());
        var selectedItems = cartService.getSelectedItems(request.getSession(), selectedIds);
        if (selectedItems.isEmpty()) {
            flashError(request, "Không tìm thấy sản phẩm đã chọn trong giỏ hàng.");
            redirect(request, response, "/Cart");
            return;
        }

        CheckoutViewModel model = new CheckoutViewModel();
        model.setSelectedIdsString(selectedIdsString);
        model.setCart(new CartViewModel());
        model.getCart().setItems(new ArrayList<>(selectedItems));

        BigDecimal subtotal = selectedItems.stream()
            .map(item -> item.getTotal())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.setTamTinh(subtotal);
        model.setPhiVanChuyen(SHIPPING_FEE);
        model.setTongThanhToan(subtotal.add(SHIPPING_FEE));

        try {
            var addresses = addressService.getAddressesByCustomer(currentUser(request).getMaKhachHang(), currentToken(request));
            model.setAddresses(addresses);
            if (!addresses.isEmpty()) {
                var first = addresses.get(0);
                model.setNguoiNhan(first.getTenNguoiNhan());
                model.setSoDienThoai(first.getSoDienThoai());
                model.setDiaChiGiaoHang(first.getFullAddress());
            }
        } catch (Exception ignored) {
            model.setAddresses(List.of());
        }

        model.setDanhSachKhuyenMai(promotionService.getApplicablePromotions(request.getSession(), subtotal));
        request.setAttribute("rawCart", rawCart);
        request.setAttribute("checkoutModel", model);
        request.setAttribute("pageTitle", "Thanh toán");
        request.setAttribute("activeNav", "checkout");
        render(request, response, "thanhtoan/checkout.jsp");
    }

    private void placeOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!requireLogin(request, response)) {
            return;
        }

        String selectedIdsString = valueOrEmpty(request.getParameter("selectedIdsString"));
        List<Integer> selectedIds = parseIds(selectedIdsString);
        if (selectedIds.isEmpty()) {
            flashError(request, "Thiếu danh sách sản phẩm thanh toán.");
            redirect(request, response, "/Cart");
            return;
        }

        var selectedItems = cartService.getSelectedItems(request.getSession(), selectedIds);
        if (selectedItems.isEmpty()) {
            flashError(request, "Không tìm thấy sản phẩm trong giỏ hàng.");
            redirect(request, response, "/Cart");
            return;
        }

        String nguoiNhan = valueOrEmpty(request.getParameter("nguoiNhan"));
        String soDienThoai = valueOrEmpty(request.getParameter("soDienThoai"));
        String diaChiGiaoHang = valueOrEmpty(request.getParameter("diaChiGiaoHang"));
        String phuongThucThanhToan = valueOrEmpty(request.getParameter("phuongThucThanhToan"));
        if (phuongThucThanhToan.isBlank()) {
            phuongThucThanhToan = "COD";
        }

        if (nguoiNhan.isBlank() || soDienThoai.isBlank() || diaChiGiaoHang.isBlank()) {
            flashError(request, "Vui lòng nhập đầy đủ thông tin nhận hàng.");
            redirect(request, response, "/ThanhToan/Checkout?selectedIds=" + encode(selectedIdsString));
            return;
        }

        String maCodeVoucher = valueOrEmpty(request.getParameter("MaCodeVoucher"));
        KhuyenMaiKhachHangResponse selectedVoucher = promotionService.getByCode(request.getSession(), maCodeVoucher);

        var order = orderService.createOrder(
            request.getSession(),
            currentUser(request).getMaKhachHang(),
            nguoiNhan,
            soDienThoai,
            diaChiGiaoHang,
            phuongThucThanhToan,
            selectedItems,
            selectedVoucher
        );

        cartService.removeCartItems(request.getSession(), selectedIds);
        flashSuccess(request, "Đặt hàng thành công.");

        if ("VietQR".equalsIgnoreCase(phuongThucThanhToan)) {
            redirect(request, response, "/ThanhToan/Payment?id=" + order.getMaDonHang());
            return;
        }
        redirect(request, response, "/ThanhToan/Success?id=" + order.getMaDonHang());
    }

    private void showPayment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!requireLogin(request, response)) {
            return;
        }

        Integer orderId = parseNullableInt(request.getParameter("id"));
        var order = orderService.getOrderById(request.getSession(), orderId, currentUser(request).getMaKhachHang());
        if (order == null) {
            flashError(request, "Không tìm thấy đơn hàng cần thanh toán.");
            redirect(request, response, "/DonHang");
            return;
        }

        String qrPayload = "WEBPC|ORDER|" + order.getMaCodeDonHang() + "|" + order.getTongTien();
        String qrImage = "https://api.qrserver.com/v1/create-qr-code/?size=320x320&data=" + encode(qrPayload);

        request.setAttribute("pageTitle", "Thanh toán đơn hàng");
        request.setAttribute("activeNav", "checkout");
        request.setAttribute("order", order);
        request.setAttribute("qrImage", qrImage);
        render(request, response, "thanhtoan/payment.jsp");
    }

    private void confirmPayment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!requireLogin(request, response)) {
            return;
        }
        Integer orderId = parseNullableInt(request.getParameter("orderId"));
        boolean ok = orderService.markPaid(request.getSession(), orderId, currentUser(request).getMaKhachHang());
        if (ok) {
            flashSuccess(request, "Đã ghi nhận thanh toán thành công.");
        } else {
            flashError(request, "Không thể xác nhận thanh toán.");
        }
        redirect(request, response, "/ThanhToan/Payment?id=" + orderId);
    }

    private void checkStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (currentUser(request) == null) {
            writeJson(response, Map.of("status", "ERROR", "message", "Not authenticated"));
            return;
        }
        Integer orderId = parseNullableInt(request.getParameter("orderId"));
        if (orderId == null) {
            orderId = parseNullableInt(request.getParameter("id"));
        }
        String status = orderService.paymentStatus(request.getSession(), orderId, currentUser(request).getMaKhachHang());

        ApiStatusResponse payload = new ApiStatusResponse();
        payload.setStatus(status);
        payload.setMessage("OK");
        payload.setShouldRedirect("PAID".equals(status));
        writeJson(response, payload);
    }

    private void showSuccess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!requireLogin(request, response)) {
            return;
        }
        Integer orderId = parseNullableInt(request.getParameter("id"));
        request.setAttribute("pageTitle", "Đặt hàng thành công");
        request.setAttribute("activeNav", "orders");
        request.setAttribute("orderId", orderId);
        render(request, response, "thanhtoan/success.jsp");
    }

    private String valueOrEmpty(String value) {
        return value == null ? "" : value.trim();
    }

    private List<Integer> parseIds(String selectedIds) {
        if (selectedIds == null || selectedIds.isBlank()) {
            return List.of();
        }
        List<Integer> result = new ArrayList<>();
        String[] chunks = selectedIds.split(",");
        for (String chunk : chunks) {
            Integer value = parseNullableInt(chunk.trim());
            if (value != null && value > 0) {
                result.add(value);
            }
        }
        return result;
    }

    private String encode(String value) {
        return URLEncoder.encode(value == null ? "" : value, StandardCharsets.UTF_8);
    }
}
