package com.webpc.fe.model.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequest {

    private String hoTen;
    private String soDienThoai;
    private String email;
    private String tenDangNhap;
    private String matKhau;
}
