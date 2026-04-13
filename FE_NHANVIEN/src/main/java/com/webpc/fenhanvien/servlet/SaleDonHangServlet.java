package com.webpc.fenhanvien.servlet;

import com.webpc.fenhanvien.common.ApiException;
import com.webpc.fenhanvien.common.BaseServlet;
import com.webpc.fenhanvien.service.SaleOrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/Sale/DonHang")
public class SaleDonHangServlet extends BaseServlet {

    private final SaleOrderService saleOrderService = new SaleOrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("pageTitle", "Đơn hàng");
        request.setAttribute("menuArea", "sale");
        request.setAttribute("activeMenu", "donhang");
        String token = currentToken(request);
        try {
            request.setAttribute("orders", saleOrderService.getAll(token));
        } catch (ApiException ex) {
            request.setAttribute("pageError", ex.getMessage());
        }

        Integer selectedId = parseNullableInt(request.getParameter("id"));
        if (selectedId != null) {
            try {
                request.setAttribute("selectedOrder", saleOrderService.getById(selectedId, token));
            } catch (ApiException ex) {
                request.setAttribute("pageError", ex.getMessage());
            }
        }
        render(request, response, "sale/donhang.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        Integer id = parseNullableInt(request.getParameter("id"));
        if (action == null || id == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String token = currentToken(request);
        try {
            switch (action) {
                case "approve" -> {
                    saleOrderService.approve(id, token);
                    flashSuccess(request, "Đã duyệt đơn hàng thành công.");
                }
                case "reject" -> {
                    String reason = trimToNull(request.getParameter("reason"));
                    if (reason == null) {
                        reason = "Nhan vien huy don qua trang quan ly";
                    }
                    saleOrderService.reject(id, reason, token);
                    flashSuccess(request, "Đã hủy đơn hàng thành công.");
                }
                default -> {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
            }
        } catch (ApiException ex) {
            flashError(request, ex.getMessage());
        }

        redirect(request, response, "/Sale/DonHang?id=" + id);
    }

    private Integer parseNullableInt(String value) {
        String trimmed = trimToNull(value);
        if (trimmed == null) {
            return null;
        }
        try {
            return Integer.parseInt(trimmed);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
