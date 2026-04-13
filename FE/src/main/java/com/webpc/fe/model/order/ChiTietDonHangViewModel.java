package com.webpc.fe.model.order;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChiTietDonHangViewModel {

    private Integer maSanPham;
    private String tenSanPham;
    private String hinhAnh;
    private int soLuong;
    private BigDecimal donGiaLucMua = BigDecimal.ZERO;
    private BigDecimal thanhTien = BigDecimal.ZERO;
}
