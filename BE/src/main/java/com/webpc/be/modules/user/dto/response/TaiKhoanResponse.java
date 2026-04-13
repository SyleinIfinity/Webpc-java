package com.webpc.be.modules.user.dto.response;

import java.time.LocalDateTime;

public record TaiKhoanResponse(
    Integer maTaiKhoan,
    String tenDangNhap,
    String email,
    String trangThai,
    LocalDateTime ngayTao
) {
}
