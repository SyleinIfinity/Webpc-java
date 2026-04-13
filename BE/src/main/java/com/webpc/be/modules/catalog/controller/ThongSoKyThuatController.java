package com.webpc.be.modules.catalog.controller;

import com.webpc.be.common.response.MessageResponse;
import com.webpc.be.modules.catalog.dto.request.CreateThongSoRequest;
import com.webpc.be.modules.catalog.dto.request.UpdateThongSoRequest;
import com.webpc.be.modules.catalog.service.ThongSoKyThuatService;
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
@RequestMapping("api/ThongSoKyThuat")
@RequiredArgsConstructor
public class ThongSoKyThuatController {

    private final ThongSoKyThuatService thongSoKyThuatService;

    @GetMapping("sanpham/{maSanPham}")
    public ResponseEntity<?> getByProductId(@PathVariable Integer maSanPham) {
        return ResponseEntity.ok(thongSoKyThuatService.getByProductId(maSanPham));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateThongSoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(thongSoKyThuatService.create(request));
    }

    @PatchMapping("{id}")
    public ResponseEntity<MessageResponse> update(@PathVariable Integer id, @Valid @RequestBody UpdateThongSoRequest request) {
        if (!thongSoKyThuatService.update(id, request)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new MessageResponse("Cap nhat thanh cong."));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable Integer id) {
        if (!thongSoKyThuatService.delete(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new MessageResponse("Xoa thanh cong."));
    }
}
