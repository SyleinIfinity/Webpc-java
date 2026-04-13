package com.webpc.be.modules.catalog.dto.response;

public record ThongSoKyThuatResponse(
    Integer maThongSo,
    Integer maSanPham,
    String tenThongSo,
    String giaTri
) {
}
