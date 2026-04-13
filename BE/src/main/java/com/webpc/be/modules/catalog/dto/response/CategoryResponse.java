package com.webpc.be.modules.catalog.dto.response;

public record CategoryResponse(
    Integer maDanhMuc,
    String tenDanhMuc,
    String moTa,
    Integer maDanhMucCha,
    String tenDanhMucCha,
    int soLuongDanhMucCon
) {
}
