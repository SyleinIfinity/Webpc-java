package com.webpc.fenhanvien.model.admin;

import lombok.Data;

@Data
public class KhachHangResponse {
    private Integer maKhachHang;
    private String hoTen;
    private String soDienThoai;
    private String email;
    private Integer maTaiKhoan;
    private String tenDangNhap;
    private boolean coTaiKhoan;
}
