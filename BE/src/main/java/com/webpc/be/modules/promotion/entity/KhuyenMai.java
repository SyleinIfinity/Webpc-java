package com.webpc.be.modules.promotion.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "khuyen_mai")
public class KhuyenMai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_khuyen_mai")
    private Integer maKhuyenMai;

    @Column(name = "ma_code_km")
    private String maCodeKM;

    @Column(name = "ten_chuong_trinh")
    private String tenChuongTrinh;

    @Column(name = "gia_tri_giam")
    private BigDecimal giaTriGiam;

    @Column(name = "loai_giam")
    private String loaiGiam;

    @Column(name = "don_hang_toi_thieu")
    private BigDecimal donHangToiThieu;

    @Column(name = "giam_toi_da")
    private BigDecimal giamToiDa;

    @Column(name = "ngay_bat_dau")
    private LocalDateTime ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    private LocalDateTime ngayKetThuc;

    @Column(name = "so_luong_con_lai")
    private Integer soLuongConLai;
}

