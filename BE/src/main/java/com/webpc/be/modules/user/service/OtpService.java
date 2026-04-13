package com.webpc.be.modules.user.service;

import com.webpc.be.common.exception.BadRequestException;
import com.webpc.be.modules.user.entity.OtpLog;
import com.webpc.be.modules.user.repository.OtpLogRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpLogRepository otpLogRepository;
    private final MailService mailService;

    @Transactional
    public void sendOtp(String email) {
        otpLogRepository.findFirstByEmailOrderByThoiGianTaoDesc(email)
            .ifPresent(lastOtp -> {
                long seconds = Duration.between(lastOtp.getThoiGianTao(), LocalDateTime.now()).getSeconds();
                if (seconds < 30) {
                    throw new BadRequestException("Vui long doi " + (30 - seconds) + " giay de gui lai OTP.");
                }
            });

        otpLogRepository.findAllByEmailAndTrangThai(email, "ConHan")
            .forEach(item -> item.setTrangThai("HetHan"));

        String otpCode = "%06d".formatted(ThreadLocalRandom.current().nextInt(0, 1_000_000));
        OtpLog otpLog = new OtpLog();
        otpLog.setEmail(email);
        otpLog.setMaOtp(otpCode);
        otpLog.setThoiGianTao(LocalDateTime.now());
        otpLog.setThoiGianHetHan(LocalDateTime.now().plusMinutes(5));
        otpLog.setTrangThai("ConHan");
        otpLogRepository.save(otpLog);

        String subject = "[WEBPC] Ma xac thuc OTP cua ban";
        String body = """
            <h3>Ma OTP cua ban la: <b style='color:red; font-size: 20px;'>%s</b></h3>
            <p>Ma nay co hieu luc trong 5 phut.</p>
            """.formatted(otpCode);

        if (!mailService.sendEmail(email, subject, body)) {
            throw new BadRequestException("Mail service chua duoc cau hinh hoac gui email that bai.");
        }
    }

    @Transactional
    public void verifyOtp(String email, String otpCode) {
        OtpLog otpLog = otpLogRepository
            .findFirstByEmailAndMaOtpAndTrangThaiOrderByThoiGianTaoDesc(email, otpCode, "ConHan")
            .orElseThrow(() -> new BadRequestException("Ma OTP khong chinh xac hoac da bi vo hieu hoa."));

        if (otpLog.getThoiGianHetHan().isBefore(LocalDateTime.now())) {
            otpLog.setTrangThai("HetHan");
            otpLogRepository.save(otpLog);
            throw new BadRequestException("Ma OTP da het han.");
        }

        otpLog.setTrangThai("DaSuDung");
        otpLogRepository.save(otpLog);
    }
}
