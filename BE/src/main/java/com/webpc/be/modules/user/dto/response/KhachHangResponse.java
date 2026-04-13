package com.webpc.be.modules.user.dto.response;

public record KhachHangResponse(
    Integer maKhachHang,
    String hoTen,
    String soDienThoai,
    String email,
    Integer maTaiKhoan,
    String tenDangNhap,
    boolean coTaiKhoan
) {
}
