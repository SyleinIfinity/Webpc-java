package com.webpc.be.modules.user.controller;

import com.webpc.be.common.response.MessageResponse;
import com.webpc.be.security.AuthorizationService;
import com.webpc.be.modules.user.dto.request.LoginRequest;
import com.webpc.be.modules.user.dto.request.UpdateStatusRequest;
import com.webpc.be.modules.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/TaiKhoan")
@RequiredArgsConstructor
public class TaiKhoanController {

    private final UserService userService;
    private final AuthorizationService authorizationService;

    @PostMapping("login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        var result = userService.login(request);
        if (result == null) {
            return ResponseEntity.status(401).body(new MessageResponse("Ten dang nhap hoac mat khau khong dung."));
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(userService.getAllTaiKhoans());
    }

    @PatchMapping("{id}/change-password")
    public ResponseEntity<MessageResponse> changePassword(
        @PathVariable Integer id,
        @RequestBody String newPassword,
        Authentication authentication
    ) {
        authorizationService.requireAccountAccess(authentication, id);
        if (newPassword == null || newPassword.isBlank()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Mat khau moi khong duoc de trong."));
        }
        if (!userService.changePassword(id, newPassword)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new MessageResponse("Doi mat khau thanh cong."));
    }

    @PatchMapping("update-avatar/{id}")
    public ResponseEntity<?> updateAvatar(
        @PathVariable Integer id,
        @RequestParam("file") MultipartFile file,
        Authentication authentication
    ) throws Exception {
        authorizationService.requireAccountAccess(authentication, id);
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Vui long chon file anh hop le."));
        }
        String newUrl = userService.updateAvatar(id, file);
        return ResponseEntity.ok(java.util.Map.of(
            "message", "Cap nhat anh dai dien thanh cong.",
            "url", newUrl
        ));
    }

    @PatchMapping("update-status/{id}")
    public ResponseEntity<MessageResponse> updateStatus(@PathVariable Integer id, @RequestBody UpdateStatusRequest request) {
        userService.updateStatus(id, request.trangThai());
        return ResponseEntity.ok(new MessageResponse("Da cap nhat trang thai thanh: " + request.trangThai()));
    }
}
