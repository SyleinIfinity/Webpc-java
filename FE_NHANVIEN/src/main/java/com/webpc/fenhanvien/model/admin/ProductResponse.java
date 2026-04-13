package com.webpc.fenhanvien.model.admin;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class ProductResponse {
    private Integer maSanPham;
    private String tenSanPham;
    private BigDecimal giaBan;
    private BigDecimal giaKhuyenMai;
    private boolean trangThai;
    private Integer soLuongTon;
    private String moTa;
    private String tenDanhMuc;
    private List<ImageDto> danhSachAnh;
}
