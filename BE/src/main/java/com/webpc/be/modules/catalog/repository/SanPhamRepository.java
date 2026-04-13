package com.webpc.be.modules.catalog.repository;

import com.webpc.be.modules.catalog.entity.SanPham;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {

    @EntityGraph(attributePaths = {"danhMuc", "hinhAnhs"})
    List<SanPham> findAllByOrderByMaSanPhamAsc();

    @EntityGraph(attributePaths = {"danhMuc", "hinhAnhs"})
    Optional<SanPham> findByMaSanPham(Integer maSanPham);

    @EntityGraph(attributePaths = {"danhMuc", "hinhAnhs"})
    List<SanPham> findByMaDanhMucOrderByMaSanPhamAsc(Integer maDanhMuc);

    boolean existsByMaDanhMuc(Integer maDanhMuc);
}
