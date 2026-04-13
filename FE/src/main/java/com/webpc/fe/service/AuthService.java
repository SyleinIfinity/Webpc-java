package com.webpc.fe.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.webpc.fe.common.ApiClient;
import com.webpc.fe.model.auth.KhachHangResponse;
import com.webpc.fe.model.auth.LoginRequest;
import com.webpc.fe.model.auth.MessageResponse;
import com.webpc.fe.model.auth.RegisterRequest;
import com.webpc.fe.model.auth.UserLoginResponse;

public class AuthService {

    private final ApiClient apiClient = new ApiClient();

    public UserLoginResponse login(String tenDangNhap, String matKhau) {
        return apiClient.post("TaiKhoan/login", new LoginRequest(tenDangNhap, matKhau), UserLoginResponse.class, null);
    }

    public MessageResponse register(RegisterRequest request) {
        return apiClient.post("KhachHang", request, MessageResponse.class, null);
    }

    public KhachHangResponse getCustomerById(Integer maKhachHang, String token) {
        return apiClient.get("KhachHang/" + maKhachHang, KhachHangResponse.class, token);
    }

    public JsonNode sendOtp(JsonNode request, String token) {
        return apiClient.postJson("Otp/send-otp", request, token);
    }
}
