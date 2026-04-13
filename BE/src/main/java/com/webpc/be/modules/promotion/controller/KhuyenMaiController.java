package com.webpc.be.modules.promotion.controller;

import com.webpc.be.common.response.MessageResponse;
import com.webpc.be.modules.promotion.dto.request.CreateKhuyenMaiRequest;
import com.webpc.be.modules.promotion.dto.request.UpdateKhuyenMaiRequest;
import com.webpc.be.modules.promotion.service.KhuyenMaiService;
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
@RequestMapping("api/KhuyenMai")
@RequiredArgsConstructor
public class KhuyenMaiController {

    private final KhuyenMaiService khuyenMaiService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(khuyenMaiService.getAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        var result = khuyenMaiService.getById(id);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Khong tim thay chuong trinh khuyen mai."));
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateKhuyenMaiRequest request) {
        var created = khuyenMaiService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("{id}")
    public ResponseEntity<MessageResponse> update(@PathVariable Integer id, @Valid @RequestBody UpdateKhuyenMaiRequest request) {
        if (!khuyenMaiService.update(id, request)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Khong tim thay khuyen mai de cap nhat."));
        }
        return ResponseEntity.ok(new MessageResponse("Cap nhat thanh cong."));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable Integer id) {
        if (!khuyenMaiService.delete(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Khong tim thay khuyen mai de xoa."));
        }
        return ResponseEntity.ok(new MessageResponse("Xoa thanh cong."));
    }
}

