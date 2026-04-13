package com.webpc.be.modules.catalog.controller;

import com.webpc.be.common.response.MessageResponse;
import com.webpc.be.modules.catalog.dto.request.CreateProductRequest;
import com.webpc.be.modules.catalog.dto.request.UpdateProductRequest;
import com.webpc.be.modules.catalog.service.SanPhamService;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/SanPham")
@RequiredArgsConstructor
public class SanPhamController {

    private final SanPhamService sanPhamService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(sanPhamService.getAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        var result = sanPhamService.getById(id);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Khong tim thay san pham");
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("danhmuc/{maDanhMuc}")
    public ResponseEntity<?> getByCategoryId(@PathVariable Integer maDanhMuc) {
        var result = sanPhamService.getByCategoryId(maDanhMuc);
        if (result == null || result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Khong co san pham nao thuoc danh muc nay.");
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @ModelAttribute CreateProductRequest request) throws IOException {
        sanPhamService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Tao san pham thanh cong");
    }

    @PatchMapping("{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @ModelAttribute UpdateProductRequest request) throws IOException {
        if (!sanPhamService.update(id, request)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Khong tim thay san pham de cap nhat");
        }
        return ResponseEntity.ok("Cap nhat thanh cong");
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) throws IOException {
        if (!sanPhamService.delete(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Khong tim thay san pham de xoa");
        }
        return ResponseEntity.ok("Xoa thanh cong");
    }
}
