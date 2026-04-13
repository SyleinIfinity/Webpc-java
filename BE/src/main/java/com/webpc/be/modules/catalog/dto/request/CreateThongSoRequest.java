package com.webpc.be.modules.catalog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateThongSoRequest(
    @NotNull Integer maSanPham,
    @NotBlank(message = "Ten thong so khong duoc de trong") String tenThongSo,
    @NotBlank(message = "Gia tri khong duoc de trong") String giaTri
) {
}
