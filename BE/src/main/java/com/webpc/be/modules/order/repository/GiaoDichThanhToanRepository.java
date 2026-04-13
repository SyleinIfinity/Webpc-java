package com.webpc.be.modules.order.repository;

import com.webpc.be.modules.order.entity.GiaoDichThanhToan;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GiaoDichThanhToanRepository extends JpaRepository<GiaoDichThanhToan, Integer> {

    List<GiaoDichThanhToan> findByMaDonHangOrderByMaGiaoDichAsc(Integer maDonHang);
}

