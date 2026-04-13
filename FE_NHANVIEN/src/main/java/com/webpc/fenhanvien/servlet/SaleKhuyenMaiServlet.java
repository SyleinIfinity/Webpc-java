package com.webpc.fenhanvien.servlet;

import com.webpc.fenhanvien.common.ApiException;
import com.webpc.fenhanvien.common.BaseServlet;
import com.webpc.fenhanvien.service.SalePromotionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/Sale/KhuyenMai")
public class SaleKhuyenMaiServlet extends BaseServlet {

    private final SalePromotionService salePromotionService = new SalePromotionService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("pageTitle", "Khuyến mãi");
        request.setAttribute("menuArea", "sale");
        request.setAttribute("activeMenu", "khuyenmai");
        try {
            request.setAttribute("items", salePromotionService.getAll(currentToken(request)));
        } catch (ApiException ex) {
            request.setAttribute("pageError", ex.getMessage());
        }
        render(request, response, "sale/khuyenmai.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        Integer id = parseNullableInt(request.getParameter("maKhuyenMai"));
        if (id == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String token = currentToken(request);
        try {
            Map<String, Object> payload = buildUpdatePayload(request);
            salePromotionService.update(id, payload, token);
            flashSuccess(request, "Đã cập nhật khuyến mãi thành công.");
        } catch (ApiException ex) {
            flashError(request, ex.getMessage());
        } catch (Exception ex) {
            flashError(request, "Loi: " + ex.getMessage());
        }
        redirect(request, response, "/Sale/KhuyenMai");
    }

    private Map<String, Object> buildUpdatePayload(HttpServletRequest request) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("maCodeKM", trimToNull(request.getParameter("maCodeKM")));
        payload.put("tenChuongTrinh", trimToNull(request.getParameter("tenChuongTrinh")));
        payload.put("loaiGiam", trimToNull(request.getParameter("loaiGiam")));
        payload.put("giaTriGiam", parseNullableBigDecimal(request.getParameter("giaTriGiam")));
        payload.put("donHangToiThieu", parseNullableBigDecimal(request.getParameter("donHangToiThieu")));
        payload.put("giamToiDa", parseNullableBigDecimal(request.getParameter("giamToiDa")));
        payload.put("soLuongConLai", parseNullableInt(request.getParameter("soLuongConLai")));

        LocalDateTime ngayBatDau = parseDateAsStartOfDay(request.getParameter("ngayBatDau"));
        LocalDateTime ngayKetThuc = parseDateAsEndOfDay(request.getParameter("ngayKetThuc"));
        payload.put("ngayBatDau", ngayBatDau);
        payload.put("ngayKetThuc", ngayKetThuc);
        return payload;
    }

    private LocalDateTime parseDateAsStartOfDay(String value) {
        String trimmed = trimToNull(value);
        if (trimmed == null) {
            return null;
        }
        return LocalDate.parse(trimmed).atStartOfDay();
    }

    private LocalDateTime parseDateAsEndOfDay(String value) {
        String trimmed = trimToNull(value);
        if (trimmed == null) {
            return null;
        }
        return LocalDate.parse(trimmed).atTime(23, 59, 59);
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

    private BigDecimal parseNullableBigDecimal(String value) {
        String trimmed = trimToNull(value);
        if (trimmed == null) {
            return null;
        }
        try {
            return new BigDecimal(trimmed);
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
