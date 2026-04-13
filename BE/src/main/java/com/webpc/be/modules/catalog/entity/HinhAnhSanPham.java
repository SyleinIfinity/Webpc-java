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
@Table(name = "hinh_anh_san_pham")
public class HinhAnhSanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_hinh_anh")
    private Integer id;

    @Column(name = "ma_san_pham")
    private Integer maSanPham;

    @Column(name = "url_hinh_anh")
    private String urlHinhAnh;

    @Column(name = "public_id")
    private String publicId;

    @Column(name = "la_anh_dai_dien")
    private boolean laAnhDaiDien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_san_pham", insertable = false, updatable = false)
    private SanPham sanPham;
}
