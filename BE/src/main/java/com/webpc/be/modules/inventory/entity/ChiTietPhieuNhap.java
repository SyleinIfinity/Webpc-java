package com.webpc.be.modules.inventory.entity;

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
@Table(name = "chi_tiet_phieu_nhap")
public class ChiTietPhieuNhap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_chi_tiet_phieu_nhap")
    private Integer maChiTietPhieuNhap;

    @Column(name = "ma_phieu_nhap")
    private Integer maPhieuNhap;

    @Column(name = "ma_san_pham")
    private Integer maSanPham;

    @Column(name = "so_luong_nhap")
    private Integer soLuongNhap;

    @Column(name = "gia_nhap")
    private BigDecimal giaNhap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_phieu_nhap", insertable = false, updatable = false)
    private PhieuNhap phieuNhap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_san_pham", insertable = false, updatable = false)
    private SanPham sanPham;
}
