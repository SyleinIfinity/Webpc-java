package com.webpc.fenhanvien.servlet;

import com.webpc.fenhanvien.common.ApiException;
import com.webpc.fenhanvien.common.BaseServlet;
import com.webpc.fenhanvien.service.CatalogAdminService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/Admin/ThongSo")
public class AdminThongSoServlet extends BaseServlet {

    private final CatalogAdminService catalogAdminService = new CatalogAdminService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("pageTitle", "Thông số kỹ thuật");
        request.setAttribute("menuArea", "admin");
        request.setAttribute("activeMenu", "thongso");
        try {
            request.setAttribute("products", catalogAdminService.getProducts(currentToken(request)));
            Integer productId = parseNullableInt(request.getParameter("productId"));
            request.setAttribute("selectedProductId", productId);
            if (productId != null && productId > 0) {
                request.setAttribute("items", catalogAdminService.getSpecifications(productId, currentToken(request)));
            }
        } catch (ApiException ex) {
            request.setAttribute("pageError", ex.getMessage());
        }
        render(request, response, "admin/thongso.jsp");
    }

    private Integer parseNullableInt(String value) {
        try {
            return value == null || value.isBlank() ? null : Integer.parseInt(value);
        } catch (Exception ex) {
            return null;
        }
    }
}
