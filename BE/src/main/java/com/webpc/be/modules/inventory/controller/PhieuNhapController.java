package com.webpc.be.modules.inventory.controller;

import com.webpc.be.common.response.MessageResponse;
import com.webpc.be.modules.inventory.dto.request.CreatePhieuNhapRequest;
import com.webpc.be.modules.inventory.dto.request.UpdatePhieuNhapRequest;
import com.webpc.be.modules.inventory.service.PhieuNhapService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/PhieuNhap")
@RequiredArgsConstructor
public class PhieuNhapController {

    private final PhieuNhapService phieuNhapService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(phieuNhapService.getAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(phieuNhapService.getById(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreatePhieuNhapRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(phieuNhapService.create(request));
    }

    @PatchMapping("{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody UpdatePhieuNhapRequest request) {
        return ResponseEntity.ok(phieuNhapService.update(id, request));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable Integer id) {
        if (!phieuNhapService.delete(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new MessageResponse("Xoa phieu nhap thanh cong (Da cap nhat lai kho)"));
    }
}
