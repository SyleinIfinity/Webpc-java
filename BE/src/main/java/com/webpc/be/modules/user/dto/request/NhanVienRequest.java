package com.webpc.be.modules.user.dto.request;

public record NhanVienRequest(
    String hoTen,
    String soDienThoai,
    Integer maVaiTro,
    String tenDangNhap,
    String matKhau,
    String email
) {
}
