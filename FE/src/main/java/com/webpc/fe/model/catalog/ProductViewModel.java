package com.webpc.fe.model.catalog;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductViewModel {

    private Integer maSanPham;
    private String tenSanPham;
    private BigDecimal giaBan;
    private BigDecimal giaKhuyenMai;
    private boolean trangThai;
    private Integer soLuongTon;
    private String tenDanhMuc;
    private String moTa;
    private List<ImageDto> danhSachAnh = new ArrayList<>();
    private List<ThongSoKyThuatViewModel> thongSoKyThuat = new ArrayList<>();

    public String getHinhAnhDaiDien() {
        if (danhSachAnh == null || danhSachAnh.isEmpty()) {
            return "https://via.placeholder.com/300x300?text=No+Image";
        }
        return danhSachAnh.stream()
            .filter(ImageDto::isLaAnhDaiDien)
            .findFirst()
            .orElse(danhSachAnh.get(0))
            .getUrl();
    }

    public boolean getCoKhuyenMai() {
        return giaKhuyenMai != null && giaKhuyenMai.compareTo(BigDecimal.ZERO) > 0;
    }

    public BigDecimal getGiaHienThi() {
        return getCoKhuyenMai() ? giaKhuyenMai : giaBan;
    }
}
