package com.webpc.be.modules.inventory.entity;

import com.webpc.be.modules.user.entity.NhanVien;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "phieu_nhap")
public class PhieuNhap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_phieu_nhap")
    private Integer maPhieuNhap;

    @Column(name = "ma_code_phieu")
    private String maCodePhieu;

    @Column(name = "ngay_nhap")
    private LocalDateTime ngayNhap;

    @Column(name = "tong_tien_nhap")
    private BigDecimal tongTienNhap;

    @Column(name = "ma_nhan_vien_nhap")
    private Integer maNhanVienNhap;

    @Column(name = "ghi_chu")
    private String ghiChu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_nhan_vien_nhap", insertable = false, updatable = false)
    private NhanVien nhanVien;

    @OneToMany(mappedBy = "phieuNhap")
    private List<ChiTietPhieuNhap> chiTietPhieuNhaps = new ArrayList<>();
}
