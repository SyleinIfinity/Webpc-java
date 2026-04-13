package com.webpc.fenhanvien.servlet;

import com.webpc.fenhanvien.common.BaseServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/Tech/Dashboard")
public class TechDashboardServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("pageTitle", "Dashboard kỹ thuật");
        request.setAttribute("menuArea", "tech");
        request.setAttribute("activeMenu", "dashboard");
        render(request, response, "tech/dashboard.jsp");
    }
}
