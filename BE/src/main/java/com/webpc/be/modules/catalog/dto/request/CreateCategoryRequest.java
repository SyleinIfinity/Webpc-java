package com.webpc.be.modules.catalog.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateCategoryRequest(
    @NotBlank(message = "Ten danh muc khong duoc de trong") String tenDanhMuc,
    String moTa,
    Integer maDanhMucCha
) {
}
