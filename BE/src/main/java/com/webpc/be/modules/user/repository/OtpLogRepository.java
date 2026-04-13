package com.webpc.be.modules.user.repository;

import com.webpc.be.modules.user.entity.OtpLog;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpLogRepository extends JpaRepository<OtpLog, Integer> {

    Optional<OtpLog> findFirstByEmailOrderByThoiGianTaoDesc(String email);

    java.util.List<OtpLog> findAllByEmailAndTrangThai(String email, String trangThai);

    Optional<OtpLog> findFirstByEmailAndMaOtpAndTrangThaiOrderByThoiGianTaoDesc(
        String email,
        String maOtp,
        String trangThai
    );
}
