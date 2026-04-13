package com.webpc.fenhanvien.servlet;

import com.webpc.fenhanvien.common.ApiException;
import com.webpc.fenhanvien.common.BaseServlet;
import com.webpc.fenhanvien.common.SessionKeys;
import com.webpc.fenhanvien.model.auth.UserLoginResponse;
import com.webpc.fenhanvien.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/Account/Login", "/Account/Logout"})
public class AccountServlet extends BaseServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/Account/Logout".equals(path)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            flashInfo(request, "Đã đăng xuất khỏi hệ thống.");
            redirect(request, response, "/Account/Login");
            return;
        }

        Integer roleId = currentRoleId(request);
        String roleName = currentRoleName(request);
        String dashboardPath = resolveDashboardPath(roleId, roleName);
        if (dashboardPath != null) {
            redirect(request, response, dashboardPath);
            return;
        }

        request.setAttribute("pageTitle", "Đăng nhập hệ thống");
        render(request, response, "account/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tenDangNhap = trim(request.getParameter("TenDangNhap"));
        String matKhau = trim(request.getParameter("MatKhau"));

        if (tenDangNhap.isBlank() || matKhau.isBlank()) {
            request.setAttribute("pageTitle", "Đăng nhập hệ thống");
            request.setAttribute("loginError", "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.");
            render(request, response, "account/login.jsp");
            return;
        }

        try {
            UserLoginResponse user = authService.login(tenDangNhap, matKhau);
            if (user == null || user.getMaNhanVien() == null || user.getMaVaiTro() == null) {
                request.setAttribute("pageTitle", "Đăng nhập hệ thống");
                request.setAttribute("loginError", "Tài khoản này không thuộc hệ thống nhân viên.");
                render(request, response, "account/login.jsp");
                return;
            }

            String dashboardPath = resolveDashboardPath(user.getMaVaiTro(), user.getTenVaiTro());
            if (dashboardPath == null) {
                request.setAttribute("pageTitle", "Đăng nhập hệ thống");
                request.setAttribute("loginError", "Tài khoản không có quyền truy cập khu vực quản trị.");
                render(request, response, "account/login.jsp");
                return;
            }

            HttpSession session = request.getSession(true);
            session.setAttribute(SessionKeys.USER, user);
            session.setAttribute(SessionKeys.USER_ID, user.getMaNhanVien());
            session.setAttribute(SessionKeys.USER_TOKEN, user.getToken());
            session.setAttribute(SessionKeys.USER_NAME, user.getHoTen());
            session.setAttribute(SessionKeys.ROLE_ID, user.getMaVaiTro());
            session.setAttribute(SessionKeys.ROLE_NAME, user.getTenVaiTro());

            flashSuccess(request, "Đăng nhập thành công.");
            redirect(request, response, dashboardPath);
        } catch (ApiException ex) {
            request.setAttribute("pageTitle", "Đăng nhập hệ thống");
            request.setAttribute("loginError", ex.getMessage());
            render(request, response, "account/login.jsp");
        }
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
