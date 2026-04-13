package com.webpc.fenhanvien.model.admin;

import lombok.Data;

@Data
public class NhanVienResponse {
    private Integer maNhanVien;
    private String maCodeNhanVien;
    private String hoTen;
    private String soDienThoai;
    private Integer maVaiTro;
    private String tenVaiTro;
    private Integer maTaiKhoan;
    private String tenDangNhap;
    private String email;
    private String trangThaiTaiKhoan;
}
