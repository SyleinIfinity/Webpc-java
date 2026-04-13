package com.webpc.be.modules.catalog.repository;

import com.webpc.be.modules.catalog.entity.ThongSoKyThuat;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThongSoKyThuatRepository extends JpaRepository<ThongSoKyThuat, Integer> {

    List<ThongSoKyThuat> findByMaSanPhamOrderByMaThongSoAsc(Integer maSanPham);

    Optional<ThongSoKyThuat> findByMaThongSo(Integer maThongSo);
}
