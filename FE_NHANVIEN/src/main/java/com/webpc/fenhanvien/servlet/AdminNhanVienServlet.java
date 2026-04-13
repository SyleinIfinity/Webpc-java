package com.webpc.fenhanvien.servlet;

import com.webpc.fenhanvien.common.ApiException;
import com.webpc.fenhanvien.common.BaseServlet;
import com.webpc.fenhanvien.service.UserAdminService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/Admin/NhanVien")
public class AdminNhanVienServlet extends BaseServlet {

    private final UserAdminService userAdminService = new UserAdminService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("pageTitle", "Quản lý nhân viên");
        request.setAttribute("menuArea", "admin");
        request.setAttribute("activeMenu", "nhanvien");
        try {
            request.setAttribute("items", userAdminService.getNhanViens(currentToken(request)));
        } catch (ApiException ex) {
            request.setAttribute("pageError", ex.getMessage());
        }
        render(request, response, "admin/nhanvien.jsp");
    }
}
