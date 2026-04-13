package com.webpc.be.modules.catalog.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "thong_so_ky_thuat")
public class ThongSoKyThuat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_thong_so")
    private Integer maThongSo;

    @Column(name = "ma_san_pham")
    private Integer maSanPham;

    @Column(name = "ten_thong_so")
    private String tenThongSo;

    @Column(name = "gia_tri")
    private String giaTri;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_san_pham", insertable = false, updatable = false)
    private SanPham sanPham;
}
