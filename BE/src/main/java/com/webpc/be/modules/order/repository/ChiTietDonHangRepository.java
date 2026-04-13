package com.webpc.be.modules.order.repository;

import com.webpc.be.modules.order.entity.ChiTietDonHang;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChiTietDonHangRepository extends JpaRepository<ChiTietDonHang, Integer> {

    @Query("""
        select ct
        from ChiTietDonHang ct
        left join fetch ct.sanPham sp
        where ct.maDonHang = :maDonHang
        order by ct.maChiTietDonHang asc
        """)
    List<ChiTietDonHang> findByMaDonHangFetchSanPham(@Param("maDonHang") Integer maDonHang);
}

