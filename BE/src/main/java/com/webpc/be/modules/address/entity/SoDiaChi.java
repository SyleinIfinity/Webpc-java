package com.webpc.be.modules.address.entity;

import com.webpc.be.modules.user.entity.KhachHang;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "so_dia_chi")
public class SoDiaChi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_so_dia_chi")
    private Integer maSoDiaChi;

    @Column(name = "ma_khach_hang")
    private Integer maKhachHang;

    @Column(name = "ten_nguoi_nhan")
    private String tenNguoiNhan;

    @Column(name = "so_dien_thoai")
    private String soDienThoai;

    @Column(name = "dia_chi_cu_the")
    private String diaChiCuThe;

    @Column(name = "tinh_thanh_id")
    private String tinhThanhId;

    @Column(name = "quan_huyen_id")
    private String quanHuyenId;

    @Column(name = "phuong_xa_id")
    private String phuongXaId;

    @Column(name = "ten_tinh_thanh")
    private String tenTinhThanh;

    @Column(name = "ten_quan_huyen")
    private String tenQuanHuyen;

    @Column(name = "ten_phuong_xa")
    private String tenPhuongXa;

    @Column(name = "mac_dinh")
    private boolean macDinh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_khach_hang", insertable = false, updatable = false)
    private KhachHang khachHang;
}
