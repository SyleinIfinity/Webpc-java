package com.webpc.be.modules.order.entity;

import com.webpc.be.modules.user.entity.KhachHang;
import com.webpc.be.modules.user.entity.NhanVien;
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
@Table(name = "don_hang")
public class DonHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_don_hang")
    private Integer maDonHang;

    @Column(name = "ma_code_don_hang")
    private String maCodeDonHang;

    @Column(name = "ma_khach_hang")
    private Integer maKhachHang;

    @Column(name = "ma_nhan_vien_duyet")
    private Integer maNhanVienDuyet;

    @Column(name = "ngay_dat")
    private LocalDateTime ngayDat;

    @Column(name = "tong_tien")
    private BigDecimal tongTien;

    @Column(name = "trang_thai")
    private String trangThai;

    @Column(name = "phuong_thuc_thanh_toan")
    private String phuongThucThanhToan;

    @Column(name = "dia_chi_giao_hang")
    private String diaChiGiaoHang;

    @Column(name = "so_dien_thoai_giao")
    private String soDienThoaiGiao;

    @Column(name = "nguoi_nhan")
    private String nguoiNhan;

    @Column(name = "phi_van_chuyen")
    private BigDecimal phiVanChuyen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_khach_hang", insertable = false, updatable = false)
    private KhachHang khachHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_nhan_vien_duyet", insertable = false, updatable = false)
    private NhanVien nhanVienDuyet;
}

