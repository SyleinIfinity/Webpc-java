package com.webpc.be.modules.order.entity;

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
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "giao_dich_thanh_toan")
public class GiaoDichThanhToan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_giao_dich")
    private Integer maGiaoDich;

    @Column(name = "ma_don_hang")
    private Integer maDonHang;

    @Column(name = "ma_giao_dich_momo")
    private String maGiaoDichMomo;

    @Column(name = "phuong_thuc")
    private String phuongThuc;

    @Column(name = "so_tien")
    private BigDecimal soTien;

    @Column(name = "trang_thai")
    private String trangThai;

    @Column(name = "noi_dung_loi")
    private String noiDungLoi;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_don_hang", insertable = false, updatable = false)
    private DonHang donHang;
}

