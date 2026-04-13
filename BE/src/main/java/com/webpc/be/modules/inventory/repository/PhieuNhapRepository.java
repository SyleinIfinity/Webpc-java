package com.webpc.be.modules.inventory.repository;

import com.webpc.be.modules.inventory.entity.PhieuNhap;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhieuNhapRepository extends JpaRepository<PhieuNhap, Integer> {

    @EntityGraph(attributePaths = {"nhanVien", "chiTietPhieuNhaps", "chiTietPhieuNhaps.sanPham"})
    List<PhieuNhap> findAllByOrderByMaPhieuNhapAsc();

    @EntityGraph(attributePaths = {"nhanVien", "chiTietPhieuNhaps", "chiTietPhieuNhaps.sanPham"})
    Optional<PhieuNhap> findByMaPhieuNhap(Integer maPhieuNhap);
}
