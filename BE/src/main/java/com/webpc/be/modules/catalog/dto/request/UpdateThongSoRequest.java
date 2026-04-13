package com.webpc.be.modules.catalog.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateThongSoRequest(
    @NotBlank String tenThongSo,
    @NotBlank String giaTri
) {
}
