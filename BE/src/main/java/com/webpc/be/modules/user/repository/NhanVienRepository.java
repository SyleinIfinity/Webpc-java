package com.webpc.be.modules.user.repository;

import com.webpc.be.modules.user.entity.NhanVien;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NhanVienRepository extends JpaRepository<NhanVien, Integer> {

    @EntityGraph(attributePaths = {"taiKhoan", "vaiTro"})
    List<NhanVien> findAllByOrderByMaNhanVienAsc();

    @EntityGraph(attributePaths = {"taiKhoan", "vaiTro"})
    Optional<NhanVien> findByMaNhanVien(Integer maNhanVien);

    Optional<NhanVien> findByMaCodeNhanVien(String maCodeNhanVien);
}
