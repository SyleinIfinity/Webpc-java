package com.webpc.be.modules.catalog.repository;

import com.webpc.be.modules.catalog.entity.HinhAnhSanPham;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HinhAnhSanPhamRepository extends JpaRepository<HinhAnhSanPham, Integer> {

    List<HinhAnhSanPham> findByMaSanPhamOrderByIdAsc(Integer maSanPham);

    void deleteByPublicIdIn(List<String> publicIds);

    void deleteByMaSanPham(Integer maSanPham);
}
