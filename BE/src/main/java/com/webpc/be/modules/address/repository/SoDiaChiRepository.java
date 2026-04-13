package com.webpc.be.modules.address.repository;

import com.webpc.be.modules.address.entity.SoDiaChi;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SoDiaChiRepository extends JpaRepository<SoDiaChi, Integer> {

    List<SoDiaChi> findByMaKhachHangOrderByMacDinhDescMaSoDiaChiAsc(Integer maKhachHang);

    Optional<SoDiaChi> findByMaSoDiaChi(Integer maSoDiaChi);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        update SoDiaChi s
        set s.macDinh = false
        where s.maKhachHang = :maKhachHang
          and s.maSoDiaChi <> :excludeId
          and s.macDinh = true
        """)
    int resetDefaultAddress(
        @Param("maKhachHang") Integer maKhachHang,
        @Param("excludeId") Integer excludeId
    );
}
