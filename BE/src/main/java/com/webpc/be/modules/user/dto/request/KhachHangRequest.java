package com.webpc.be.modules.user.dto.request;

public record KhachHangRequest(
    String hoTen,
    String soDienThoai,
    String email,
    String tenDangNhap,
    String matKhau
) {
}
