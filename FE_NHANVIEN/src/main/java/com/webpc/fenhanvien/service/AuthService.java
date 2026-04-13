package com.webpc.fenhanvien.service;

import com.webpc.fenhanvien.common.ApiClient;
import com.webpc.fenhanvien.model.auth.LoginRequest;
import com.webpc.fenhanvien.model.auth.UserLoginResponse;

public class AuthService {

    private final ApiClient apiClient = new ApiClient();

    public UserLoginResponse login(String tenDangNhap, String matKhau) {
        return apiClient.post("TaiKhoan/login", new LoginRequest(tenDangNhap, matKhau), UserLoginResponse.class, null);
    }
}
