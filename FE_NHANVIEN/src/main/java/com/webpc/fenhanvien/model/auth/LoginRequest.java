package com.webpc.fenhanvien.model.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {
    private String tenDangNhap;
    private String matKhau;
}
