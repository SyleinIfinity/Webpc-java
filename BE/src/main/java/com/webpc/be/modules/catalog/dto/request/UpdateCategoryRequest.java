package com.webpc.be.modules.catalog.dto.request;

public record UpdateCategoryRequest(
    String tenDanhMuc,
    String moTa,
    Integer maDanhMucCha
) {
}
