package com.webpc.be.modules.user.controller;

import com.webpc.be.common.response.MessageResponse;
import com.webpc.be.modules.user.dto.request.VaiTroRequest;
import com.webpc.be.modules.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/VaiTro")
@RequiredArgsConstructor
public class VaiTroController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(userService.getAllVaiTros());
    }

    @PostMapping
    public ResponseEntity<MessageResponse> create(@Valid @RequestBody VaiTroRequest request) {
        userService.createVaiTro(request);
        return ResponseEntity.ok(new MessageResponse("Tao vai tro thanh cong."));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable Integer id) {
        if (!userService.deleteVaiTro(id)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Khong tim thay vai tro hoac loi khi xoa."));
        }
        return ResponseEntity.ok(new MessageResponse("Xoa vai tro thanh cong."));
    }
}
