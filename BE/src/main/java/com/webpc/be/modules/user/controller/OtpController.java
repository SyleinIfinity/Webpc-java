package com.webpc.be.modules.user.controller;

import com.webpc.be.common.response.MessageResponse;
import com.webpc.be.modules.user.dto.request.SendOtpRequest;
import com.webpc.be.modules.user.dto.request.VerifyOtpRequest;
import com.webpc.be.modules.user.service.OtpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/Otp")
@RequiredArgsConstructor
public class OtpController {

    private final OtpService otpService;

    @PostMapping("send-otp")
    public ResponseEntity<MessageResponse> sendOtp(@Valid @RequestBody SendOtpRequest request) {
        otpService.sendOtp(request.email());
        return ResponseEntity.ok(new MessageResponse("Ma OTP da duoc gui den email cua ban."));
    }

    @PostMapping("verify-otp")
    public ResponseEntity<MessageResponse> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        otpService.verifyOtp(request.email(), request.otpCode());
        return ResponseEntity.ok(new MessageResponse("Xac thuc OTP thanh cong."));
    }
}
