package com.webpc.fenhanvien.servlet;

import com.webpc.fenhanvien.common.ApiException;
import com.webpc.fenhanvien.common.BaseServlet;
import com.webpc.fenhanvien.model.admin.PhieuNhapResponse;
import com.webpc.fenhanvien.service.InventoryAdminService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;

@WebServlet("/Admin/PhieuNhap")
public class AdminPhieuNhapServlet extends BaseServlet {

    private final InventoryAdminService inventoryAdminService = new InventoryAdminService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("pageTitle", "Quản lý phiếu nhập");
        request.setAttribute("menuArea", "admin");
        request.setAttribute("activeMenu", "phieunhap");
        try {
            var phieuNhaps = inventoryAdminService.getPhieuNhaps(currentToken(request));
            request.setAttribute(
                "items",
                phieuNhaps.stream()
                    .sorted(Comparator.comparing(PhieuNhapResponse::getNgayNhap, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                    .toList()
            );
        } catch (ApiException ex) {
            request.setAttribute("pageError", ex.getMessage());
        }
        render(request, response, "admin/phieunhap.jsp");
    }
}
