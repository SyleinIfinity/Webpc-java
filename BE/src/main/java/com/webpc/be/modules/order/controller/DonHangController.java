package com.webpc.be.modules.order.controller;

import com.webpc.be.common.response.MessageResponse;
import com.webpc.be.modules.order.dto.request.RejectOrderRequest;
import com.webpc.be.modules.order.service.DonHangService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/DonHang")
@RequiredArgsConstructor
public class DonHangController {

    private final DonHangService donHangService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(donHangService.getAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        var result = donHangService.getById(id);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Khong tim thay don hang"));
        }
        return ResponseEntity.ok(result);
    }

    @PutMapping("approve/{id}")
    public ResponseEntity<MessageResponse> approve(@PathVariable Integer id, Authentication authentication) {
        donHangService.approve(id, authentication);
        return ResponseEntity.ok(new MessageResponse("Da duyet don hang thanh cong."));
    }

    @PutMapping("reject/{id}")
    public ResponseEntity<MessageResponse> reject(
        @PathVariable Integer id,
        @Valid @RequestBody RejectOrderRequest request,
        Authentication authentication
    ) {
        donHangService.reject(id, request, authentication);
        return ResponseEntity.ok(new MessageResponse("Da huy don hang thanh cong."));
    }
}

