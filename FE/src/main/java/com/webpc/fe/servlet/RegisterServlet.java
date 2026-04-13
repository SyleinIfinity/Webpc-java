package com.webpc.fe.servlet;

import com.webpc.fe.common.ApiException;
import com.webpc.fe.common.BaseServlet;
import com.webpc.fe.model.auth.RegisterRequest;
import com.webpc.fe.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/Register", "/Register/Index"})
public class RegisterServlet extends BaseServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("pageTitle", "Đăng ký tài khoản");
        request.setAttribute("activeNav", "register");
        render(request, response, "register/index.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String hoTen = trim(request.getParameter("HoTen"));
        String soDienThoai = trim(request.getParameter("SoDienThoai"));
        String email = trim(request.getParameter("Email"));
        String tenDangNhap = trim(request.getParameter("TenDangNhap"));
        String matKhau = trim(request.getParameter("MatKhau"));
        String xacNhanMatKhau = trim(request.getParameter("XacNhanMatKhau"));

        List<String> errors = new ArrayList<>();
        if (hoTen.isBlank()) {
            errors.add("Họ tên không được để trống.");
        }
        if (!soDienThoai.matches("^[0-9]{10,11}$")) {
            errors.add("Số điện thoại phải gồm 10-11 chữ số.");
        }
        if (email.isBlank() || !email.contains("@")) {
            errors.add("Email không hợp lệ.");
        }
        if (tenDangNhap.length() < 4) {
            errors.add("Tên đăng nhập phải từ 4 ký tự.");
        }
        if (matKhau.length() < 6) {
            errors.add("Mật khẩu phải từ 6 ký tự.");
        }
        if (!matKhau.equals(xacNhanMatKhau)) {
            errors.add("Xác nhận mật khẩu không khớp.");
        }

        request.setAttribute("hoTen", hoTen);
        request.setAttribute("soDienThoai", soDienThoai);
        request.setAttribute("email", email);
        request.setAttribute("tenDangNhap", tenDangNhap);

        if (!errors.isEmpty()) {
            request.setAttribute("pageTitle", "Đăng ký tài khoản");
            request.setAttribute("errors", errors);
            render(request, response, "register/index.jsp");
            return;
        }

        try {
            authService.register(new RegisterRequest(hoTen, soDienThoai, email, tenDangNhap, matKhau));
            flashSuccess(request, "Đăng ký thành công. Bạn có thể đăng nhập ngay.");
            redirect(request, response, "/Login");
        } catch (ApiException ex) {
            request.setAttribute("pageTitle", "Đăng ký tài khoản");
            request.setAttribute("errors", List.of(ex.getMessage()));
            render(request, response, "register/index.jsp");
        }
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
