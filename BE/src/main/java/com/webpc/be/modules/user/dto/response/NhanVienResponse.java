package com.webpc.be.modules.user.dto.response;

public record NhanVienResponse(
    Integer maNhanVien,
    String maCodeNhanVien,
    String hoTen,
    String soDienThoai,
    Integer maVaiTro,
    String tenVaiTro,
    Integer maTaiKhoan,
    String tenDangNhap,
    String email,
    String trangThaiTaiKhoan
) {
}
