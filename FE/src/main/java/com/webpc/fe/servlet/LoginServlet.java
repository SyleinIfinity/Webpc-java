package com.webpc.fe.servlet;

import com.webpc.fe.common.ApiException;
import com.webpc.fe.common.BaseServlet;
import com.webpc.fe.common.SessionKeys;
import com.webpc.fe.model.auth.UserLoginResponse;
import com.webpc.fe.service.AuthService;
import com.webpc.fe.service.CustomerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/Login", "/Login/Index", "/Login/Logout"})
public class LoginServlet extends BaseServlet {

    private final AuthService authService = new AuthService();
    private final CustomerService customerService = new CustomerService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/Login/Logout".equals(path)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            flashInfo(request, "Bạn đã đăng xuất.");
            redirect(request, response, "/Home");
            return;
        }

        if (currentUser(request) != null) {
            redirect(request, response, "/Home");
            return;
        }

        request.setAttribute("pageTitle", "Đăng nhập");
        request.setAttribute("openLoginModal", true);
        render(request, response, "login/index.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String tenDangNhap = trim(request.getParameter("TenDangNhap"));
        String matKhau = trim(request.getParameter("MatKhau"));
        String returnUrl = trim(request.getParameter("returnUrl"));

        if (tenDangNhap.isBlank() || matKhau.isBlank()) {
            request.setAttribute("pageTitle", "Đăng nhập");
            request.setAttribute("openLoginModal", true);
            request.setAttribute("loginError", "Vui lòng nhập tên đăng nhập và mật khẩu.");
            request.setAttribute("returnUrl", returnUrl);
            render(request, response, "login/index.jsp");
            return;
        }

        try {
            UserLoginResponse user = authService.login(tenDangNhap, matKhau);
            if (user == null || user.getMaKhachHang() == null) {
                request.setAttribute("pageTitle", "Đăng nhập");
                request.setAttribute("openLoginModal", true);
                request.setAttribute("loginError", "Tài khoản này không thuộc khu vực khách hàng.");
                request.setAttribute("returnUrl", returnUrl);
                render(request, response, "login/index.jsp");
                return;
            }

            user = customerService.enrichLoggedInUser(user);

            HttpSession session = request.getSession(true);
            session.setAttribute(SessionKeys.USER, user);
            session.setAttribute(SessionKeys.USER_ID, user.getMaKhachHang());
            session.setAttribute(SessionKeys.USER_TOKEN, user.getToken());
            session.setAttribute(
                SessionKeys.USER_NAME,
                (user.getHoTen() != null && !user.getHoTen().isBlank()) ? user.getHoTen() : user.getTenDangNhap()
            );

            flashSuccess(request, "Đăng nhập thành công.");

            if (isSafeReturnUrl(returnUrl)) {
                response.sendRedirect(request.getContextPath() + returnUrl);
                return;
            }
            redirect(request, response, "/Home");
        } catch (ApiException ex) {
            request.setAttribute("pageTitle", "Đăng nhập");
            request.setAttribute("openLoginModal", true);
            request.setAttribute("loginError", ex.getMessage());
            request.setAttribute("returnUrl", returnUrl);
            render(request, response, "login/index.jsp");
        }
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }

    private boolean isSafeReturnUrl(String returnUrl) {
        if (returnUrl == null || returnUrl.isBlank()) {
            return false;
        }
        if (!returnUrl.startsWith("/")) {
            return false;
        }
        if (returnUrl.startsWith("//") || returnUrl.startsWith("/WEB-INF/")) {
            return false;
        }
        return !"/Login".equals(returnUrl) && !"/Login/Index".equals(returnUrl);
    }
}
