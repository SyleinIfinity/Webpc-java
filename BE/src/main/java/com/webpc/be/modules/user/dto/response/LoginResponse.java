package com.webpc.be.modules.user.dto.response;

public record LoginResponse(
    Integer maNhanVien,
    Integer maKhachHang,
    String hoTen,
    String tenDangNhap,
    int maVaiTro,
    String tenVaiTro,
    String token
) {
}
