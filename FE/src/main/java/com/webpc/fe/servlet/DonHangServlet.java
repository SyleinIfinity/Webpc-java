package com.webpc.fe.servlet;

import com.webpc.fe.common.BaseServlet;
import com.webpc.fe.service.OrderSessionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {
    "/DonHang",
    "/DonHang/Index",
    "/DonHang/Detail",
    "/DonHang/ConfirmReceived",
    "/DonHang/CancelOrder"
})
public class DonHangServlet extends BaseServlet {

    private final OrderSessionService orderService = new OrderSessionService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/DonHang/Detail".equals(path)) {
            showDetail(request, response);
            return;
        }
        showIndex(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String path = request.getServletPath();
        switch (path) {
            case "/DonHang/ConfirmReceived" -> confirmReceived(request, response);
            case "/DonHang/CancelOrder" -> cancelOrder(request, response);
            default -> response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void showIndex(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!requireLogin(request, response)) {
            return;
        }

        Integer maKhachHang = currentUser(request).getMaKhachHang();
        request.setAttribute("pageTitle", "Lịch sử đơn hàng");
        request.setAttribute("activeNav", "orders");
        request.setAttribute("orders", orderService.getOrdersByCustomer(request.getSession(), maKhachHang));
        render(request, response, "donhang/index.jsp");
    }

    private void showDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!requireLogin(request, response)) {
            return;
        }

        Integer orderId = parseNullableInt(request.getParameter("id"));
        if (orderId == null || orderId <= 0) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Integer maKhachHang = currentUser(request).getMaKhachHang();
        var order = orderService.getOrderById(request.getSession(), orderId, maKhachHang);
        if (order == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        request.setAttribute("pageTitle", "Chi tiết đơn hàng");
        request.setAttribute("activeNav", "orders");
        request.setAttribute("order", order);
        render(request, response, "donhang/detail.jsp");
    }

    private void confirmReceived(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!requireLogin(request, response)) {
            return;
        }

        Integer orderId = parseNullableInt(request.getParameter("id"));
        Integer maKhachHang = currentUser(request).getMaKhachHang();
        boolean ok = orderService.confirmReceived(request.getSession(), orderId, maKhachHang);
        if (ok) {
            flashSuccess(request, "Cảm ơn bạn! Đơn hàng đã hoàn thành.");
        } else {
            flashError(request, "Không thể xác nhận nhận hàng ở trạng thái hiện tại.");
        }
        redirect(request, response, "/DonHang/Detail?id=" + orderId);
    }

    private void cancelOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!requireLogin(request, response)) {
            return;
        }

        Integer orderId = parseNullableInt(request.getParameter("id"));
        Integer maKhachHang = currentUser(request).getMaKhachHang();
        boolean ok = orderService.cancelOrder(request.getSession(), orderId, maKhachHang);
        if (ok) {
            flashSuccess(request, "Đã hủy đơn hàng thành công.");
        } else {
            flashError(request, "Không thể hủy đơn hàng này.");
        }
        redirect(request, response, "/DonHang/Detail?id=" + orderId);
    }
}
