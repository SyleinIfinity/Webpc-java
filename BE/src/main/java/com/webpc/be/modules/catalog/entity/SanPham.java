package com.webpc.be.modules.catalog.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "san_pham")
public class SanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_san_pham")
    private Integer maSanPham;

    @Column(name = "ten_san_pham")
    private String tenSanPham;

    @Column(name = "gia_ban")
    private BigDecimal giaBan;

    @Column(name = "gia_khuyen_mai")
    private BigDecimal giaKhuyenMai;

    @Column(name = "so_luong_ton")
    private Integer soLuongTon;

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "ma_danh_muc")
    private Integer maDanhMuc;

    @Column(name = "trang_thai")
    private boolean trangThai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_danh_muc", insertable = false, updatable = false)
    private DanhMuc danhMuc;

    @OneToMany(mappedBy = "sanPham")
    private List<HinhAnhSanPham> hinhAnhs = new ArrayList<>();
}
