package com.webpc.fenhanvien.model.auth;

import lombok.Data;

@Data
public class UserLoginResponse {
    private Integer maNhanVien;
    private Integer maKhachHang;
    private String hoTen;
    private String tenDangNhap;
    private Integer maVaiTro;
    private String tenVaiTro;
    private String token;
}
