package com.webpc.fe.servlet;

import com.webpc.fe.common.BaseServlet;
import com.webpc.fe.model.auth.InfoUserViewModel;
import com.webpc.fe.model.auth.KhachHangResponse;
import com.webpc.fe.model.auth.UserLoginResponse;
import com.webpc.fe.service.CustomerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/InfoUser", "/InfoUser/Index"})
public class InfoUserServlet extends BaseServlet {

    private final CustomerService customerService = new CustomerService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!requireLogin(request, response)) {
            return;
        }

        UserLoginResponse currentUser = currentUser(request);
        KhachHangResponse customer = customerService.getCustomerById(currentUser.getMaKhachHang(), currentToken(request));

        InfoUserViewModel model = new InfoUserViewModel();
        model.setMaKhachHang(customer.getMaKhachHang());
        model.setHoTen(customer.getHoTen());
        model.setSoDienThoai(customer.getSoDienThoai());
        model.setEmail(customer.getEmail());
        model.setTenDangNhap(customer.getTenDangNhap());

        request.setAttribute("pageTitle", "Thông tin cá nhân");
        request.setAttribute("activeNav", "info-user");
        request.setAttribute("userProfile", model);
        render(request, response, "info/user.jsp");
    }
}
