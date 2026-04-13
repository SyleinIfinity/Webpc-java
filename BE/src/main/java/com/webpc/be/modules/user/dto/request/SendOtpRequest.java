package com.webpc.be.modules.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SendOtpRequest(
    @NotBlank(message = "Email khong duoc de trong")
    @Email(message = "Dinh dang email khong hop le")
    String email
) {
}
