package com.webpc.fenhanvien.servlet;

import com.webpc.fenhanvien.common.BaseServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/Admin/Luong")
public class AdminLuongServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("pageTitle", "Lương & KPI");
        request.setAttribute("menuArea", "admin");
        request.setAttribute("activeMenu", "luong");
        request.setAttribute("pageError", "BE hiện chưa có endpoint parity cho module lương/KPI, nên FE_NHANVIEN mới dừng ở khung quản trị.");
        render(request, response, "admin/luong.jsp");
    }
}
