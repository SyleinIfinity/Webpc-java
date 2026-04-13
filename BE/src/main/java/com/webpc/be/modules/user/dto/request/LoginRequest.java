package com.webpc.be.modules.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank(message = "Ten dang nhap khong duoc de trong") String tenDangNhap,
    @NotBlank(message = "Mat khau khong duoc de trong") String matKhau
) {
}
