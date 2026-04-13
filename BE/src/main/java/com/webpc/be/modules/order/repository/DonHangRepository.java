package com.webpc.be.modules.order.repository;

import com.webpc.be.modules.order.dto.response.DonHangListResponse;
import com.webpc.be.modules.order.entity.DonHang;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DonHangRepository extends JpaRepository<DonHang, Integer> {

    @Query("""
        select new com.webpc.be.modules.order.dto.response.DonHangListResponse(
            d.maDonHang,
            d.maCodeDonHang,
            d.maKhachHang,
            kh.hoTen,
            d.ngayDat,
            d.tongTien,
            d.trangThai,
            d.phuongThucThanhToan
        )
        from DonHang d
        left join d.khachHang kh
        order by d.maDonHang desc
        """)
    List<DonHangListResponse> findAllForList();
}

