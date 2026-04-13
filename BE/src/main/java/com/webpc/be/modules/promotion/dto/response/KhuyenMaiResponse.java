package com.webpc.be.modules.promotion.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record KhuyenMaiResponse(
    Integer maKhuyenMai,
    String maCodeKM,
    String tenChuongTrinh,
    BigDecimal giaTriGiam,
    String loaiGiam,
    BigDecimal donHangToiThieu,
    BigDecimal giamToiDa,
    LocalDateTime ngayBatDau,
    LocalDateTime ngayKetThuc,
    Integer soLuongConLai
) {
}

