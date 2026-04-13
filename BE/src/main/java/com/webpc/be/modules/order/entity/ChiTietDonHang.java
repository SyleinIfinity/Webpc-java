package com.webpc.be.modules.order.entity;

import com.webpc.be.modules.catalog.entity.SanPham;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "chi_tiet_don_hang")
public class ChiTietDonHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_chi_tiet_don_hang")
    private Integer maChiTietDonHang;

    @Column(name = "ma_don_hang")
    private Integer maDonHang;

    @Column(name = "ma_san_pham")
    private Integer maSanPham;

    @Column(name = "so_luong")
    private Integer soLuong;

    @Column(name = "don_gia_luc_mua")
    private BigDecimal donGiaLucMua;

    @Column(name = "thanh_tien")
    private BigDecimal thanhTien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_don_hang", insertable = false, updatable = false)
    private DonHang donHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_san_pham", insertable = false, updatable = false)
    private SanPham sanPham;
}

