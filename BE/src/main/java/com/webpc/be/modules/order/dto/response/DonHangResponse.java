package com.webpc.be.modules.order.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record DonHangResponse(
    Integer maDonHang,
    String maCodeDonHang,
    Integer maKhachHang,
    LocalDateTime ngayDat,
    String trangThai,
    String phuongThucThanhToan,
    String nguoiNhan,
    String soDienThoaiGiao,
    String diaChiGiaoHang,
    BigDecimal phiVanChuyen,
    BigDecimal tongTien,
    List<ChiTietDonHangResponse> chiTiet,
    List<GiaoDichResponse> giaoDichs
) {

    public record ChiTietDonHangResponse(
        Integer maSanPham,
        String tenSanPham,
        String hinhAnh,
        Integer soLuong,
        BigDecimal donGiaLucMua,
        BigDecimal thanhTien
    ) {
    }

    public record GiaoDichResponse(
        Integer maGiaoDich,
        LocalDateTime ngayGiaoDich,
        BigDecimal soTien,
        String trangThai,
        String phuongThuc
    ) {
    }
}

