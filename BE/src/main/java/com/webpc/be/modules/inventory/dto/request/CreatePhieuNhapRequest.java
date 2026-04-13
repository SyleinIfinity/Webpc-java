package com.webpc.be.modules.inventory.dto.request;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePhieuNhapRequest {

    @NotNull
    private Integer maNhanVienNhap;

    private String ghiChu;

    @NotNull
    private List<ChiTietPhieuNhapItem> chiTiet;

    @Getter
    @Setter
    public static class ChiTietPhieuNhapItem {
        @NotNull
        private Integer maSanPham;

        @NotNull
        private Integer soLuongNhap;

        @NotNull
        private BigDecimal giaNhap;
    }
}
