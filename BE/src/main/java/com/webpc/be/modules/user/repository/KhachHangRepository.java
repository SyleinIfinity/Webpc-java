package com.webpc.be.modules.user.repository;

import com.webpc.be.modules.user.entity.KhachHang;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KhachHangRepository extends JpaRepository<KhachHang, Integer> {

    @EntityGraph(attributePaths = {"taiKhoan"})
    List<KhachHang> findAllByOrderByMaKhachHangAsc();

    @EntityGraph(attributePaths = {"taiKhoan"})
    Optional<KhachHang> findByMaKhachHang(Integer maKhachHang);

    Optional<KhachHang> findBySoDienThoai(String soDienThoai);
}
