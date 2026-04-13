package com.webpc.be.modules.catalog.controller;

import com.webpc.be.common.response.MessageResponse;
import com.webpc.be.modules.catalog.service.HinhAnhSanPhamService;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/HinhAnhSanPham")
@RequiredArgsConstructor
public class HinhAnhSanPhamController {

    private final HinhAnhSanPhamService hinhAnhSanPhamService;

    @GetMapping("product/{productId}")
    public ResponseEntity<?> getByProduct(@PathVariable Integer productId) {
        return ResponseEntity.ok(hinhAnhSanPhamService.getByProductId(productId));
    }

    @PostMapping("product/{productId}")
    public ResponseEntity<?> uploadImage(@PathVariable Integer productId, @RequestParam("file") MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("Vui long chon file anh");
        }
        return ResponseEntity.ok(hinhAnhSanPhamService.addImage(productId, file));
    }

    @PatchMapping("product/{productId}/set-main/{imageId}")
    public ResponseEntity<?> setMainImage(@PathVariable Integer productId, @PathVariable Integer imageId) {
        hinhAnhSanPhamService.setMainImage(productId, imageId);
        return ResponseEntity.ok(Map.of("message", "Da dat lam anh dai dien thanh cong."));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteImage(@PathVariable Integer id) throws IOException {
        if (!hinhAnhSanPhamService.deleteImage(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Da xoa hinh anh thanh cong");
    }

    @DeleteMapping("delete-all/{productId}")
    public ResponseEntity<?> deleteAllByProductId(@PathVariable Integer productId) throws IOException {
        if (!hinhAnhSanPhamService.deleteAllByProductId(productId)) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND)
                .body(new MessageResponse("San pham khong co hinh anh nao hoac khong ton tai."));
        }
        return ResponseEntity.ok(Map.of("message", "Da xoa toan bo hinh anh cua san pham."));
    }
}
