package com.webpc.be.modules.catalog.controller;

import com.webpc.be.common.response.MessageResponse;
import com.webpc.be.modules.catalog.dto.request.CreateCategoryRequest;
import com.webpc.be.modules.catalog.dto.request.UpdateCategoryRequest;
import com.webpc.be.modules.catalog.service.DanhMucService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/DanhMuc")
@RequiredArgsConstructor
public class DanhMucController {

    private final DanhMucService danhMucService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(danhMucService.getAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(danhMucService.getById(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateCategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(danhMucService.create(request));
    }

    @PatchMapping("{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody UpdateCategoryRequest request) {
        return ResponseEntity.ok(danhMucService.update(id, request));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable Integer id) {
        if (!danhMucService.delete(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new MessageResponse("Xoa danh muc thanh cong"));
    }
}
