package com.webpc.be.modules.promotion.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdateKhuyenMaiRequest(
    @NotBlank(message = "Ma code khuyen mai khong duoc de trong")
    String maCodeKM,
    @NotBlank(message = "Ten chuong trinh khong duoc de trong")
    String tenChuongTrinh,
    @NotNull(message = "Gia tri giam khong duoc de trong")
    @PositiveOrZero(message = "Gia tri giam phai >= 0")
    BigDecimal giaTriGiam,
    @NotBlank(message = "Loai giam khong duoc de trong")
    @Pattern(regexp = "DIRECT|PERCENT", message = "Loai giam chi duoc la 'DIRECT' hoac 'PERCENT'")
    String loaiGiam,
    @NotNull(message = "Don hang toi thieu khong duoc de trong")
    @PositiveOrZero(message = "Don hang toi thieu phai >= 0")
    BigDecimal donHangToiThieu,
    @PositiveOrZero(message = "Giam toi da phai >= 0")
    BigDecimal giamToiDa,
    @NotNull(message = "Ngay bat dau khong duoc de trong")
    LocalDateTime ngayBatDau,
    @NotNull(message = "Ngay ket thuc khong duoc de trong")
    LocalDateTime ngayKetThuc,
    @NotNull(message = "So luong con lai khong duoc de trong")
    @PositiveOrZero(message = "So luong con lai phai >= 0")
    Integer soLuongConLai
) {
}

