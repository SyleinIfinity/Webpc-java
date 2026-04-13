package com.webpc.be.modules.user.controller;

import com.webpc.be.common.response.MessageResponse;
import com.webpc.be.modules.user.dto.request.NhanVienRequest;
import com.webpc.be.modules.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/NhanVien")
@RequiredArgsConstructor
public class NhanVienController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(userService.getAllNhanViens());
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        var data = userService.getNhanVienById(id);
        if (data == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(data);
    }

    @PostMapping
    public ResponseEntity<MessageResponse> create(@Valid @RequestBody NhanVienRequest request) {
        return ResponseEntity.ok(new MessageResponse(userService.createNhanVien(request)));
    }

    @PutMapping("{id}")
    public ResponseEntity<MessageResponse> update(@PathVariable Integer id, @Valid @RequestBody NhanVienRequest request) {
        if (!userService.updateNhanVien(id, request)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new MessageResponse("Cap nhat thong tin nhan vien thanh cong."));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable Integer id) {
        if (!userService.deleteNhanVien(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new MessageResponse("Da xoa nhan vien va tai khoan lien quan."));
    }
}
