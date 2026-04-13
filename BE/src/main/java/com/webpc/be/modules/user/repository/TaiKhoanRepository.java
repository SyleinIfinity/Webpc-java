package com.webpc.be.modules.user.repository;

import com.webpc.be.modules.user.entity.TaiKhoan;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Integer> {

    List<TaiKhoan> findAllByOrderByMaTaiKhoanAsc();

    boolean existsByTenDangNhap(String tenDangNhap);

    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = {"nhanVien", "nhanVien.vaiTro", "khachHang"})
    Optional<TaiKhoan> findByTenDangNhap(String tenDangNhap);

    Optional<TaiKhoan> findByEmail(String email);
}
