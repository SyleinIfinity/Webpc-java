package com.webpc.fenhanvien.servlet;

import com.webpc.fenhanvien.common.ApiException;
import com.webpc.fenhanvien.common.BaseServlet;
import com.webpc.fenhanvien.service.UserAdminService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/Admin/KhachHang")
public class AdminKhachHangServlet extends BaseServlet {

    private final UserAdminService userAdminService = new UserAdminService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("pageTitle", "Quản lý khách hàng");
        request.setAttribute("menuArea", "admin");
        request.setAttribute("activeMenu", "khachhang");
        try {
            request.setAttribute("items", userAdminService.getKhachHangs(currentToken(request)));
        } catch (ApiException ex) {
            request.setAttribute("pageError", ex.getMessage());
        }
        render(request, response, "admin/khachhang.jsp");
    }
}
