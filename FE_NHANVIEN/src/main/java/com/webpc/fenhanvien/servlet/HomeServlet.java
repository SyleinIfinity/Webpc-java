package com.webpc.fenhanvien.servlet;

import com.webpc.fenhanvien.common.BaseServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/Home", "/Home/Index", "/home", "/home/index"})
public class HomeServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer roleId = currentRoleId(request);
        String roleName = currentRoleName(request);
        String dashboardPath = resolveDashboardPath(roleId, roleName);
        if (dashboardPath != null) {
            redirect(request, response, dashboardPath);
            return;
        }
        request.setAttribute("pageTitle", "Cổng nhân viên");
        render(request, response, "home/index.jsp");
    }
}
