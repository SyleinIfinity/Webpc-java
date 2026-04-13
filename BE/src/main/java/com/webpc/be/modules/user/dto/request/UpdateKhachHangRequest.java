package com.webpc.be.modules.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateKhachHangRequest(
    @NotBlank(message = "Ho ten khong duoc de trong") String hoTen,
    @NotBlank(message = "So dien thoai khong duoc de trong") String soDienThoai,
    @Email(message = "Email khong hop le") String email
) {
}
