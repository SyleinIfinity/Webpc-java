package com.webpc.fe.service;

import com.webpc.fe.model.auth.KhachHangResponse;
import com.webpc.fe.model.auth.UserLoginResponse;

public class CustomerService {

    private final AuthService authService = new AuthService();

    public KhachHangResponse getCustomerById(Integer maKhachHang, String token) {
        return authService.getCustomerById(maKhachHang, token);
    }

    public UserLoginResponse enrichLoggedInUser(UserLoginResponse user) {
        if (user == null || user.getMaKhachHang() == null || user.getToken() == null || user.getToken().isBlank()) {
            return user;
        }
        KhachHangResponse customer = getCustomerById(user.getMaKhachHang(), user.getToken());
        if (customer != null) {
            user.setHoTen(customer.getHoTen());
            user.setSoDienThoai(customer.getSoDienThoai());
        }
        return user;
    }
}
