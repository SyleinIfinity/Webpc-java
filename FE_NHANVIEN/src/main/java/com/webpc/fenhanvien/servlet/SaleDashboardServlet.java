package com.webpc.fenhanvien.servlet;

import com.webpc.fenhanvien.common.BaseServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/Sale/Dashboard")
public class SaleDashboardServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("pageTitle", "Dashboard nhân viên kinh doanh");
        request.setAttribute("menuArea", "sale");
        request.setAttribute("activeMenu", "dashboard");
        render(request, response, "sale/dashboard.jsp");
    }
}
