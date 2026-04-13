package com.webpc.be.modules.user.controller;

import com.webpc.be.common.response.MessageResponse;
import com.webpc.be.security.AuthorizationService;
import com.webpc.be.modules.user.dto.request.KhachHangRequest;
import com.webpc.be.modules.user.dto.request.UpdateKhachHangRequest;
import com.webpc.be.modules.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/KhachHang")
@RequiredArgsConstructor
public class KhachHangController {

    private final UserService userService;
    private final AuthorizationService authorizationService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(userService.getAllKhachHangs());
    }

    @PostMapping
    public ResponseEntity<MessageResponse> create(@Valid @RequestBody KhachHangRequest request) {
        return ResponseEntity.ok(new MessageResponse(userService.createKhachHang(request)));
    }

    @PutMapping("{id}")
    public ResponseEntity<MessageResponse> update(
        @PathVariable Integer id,
        @Valid @RequestBody UpdateKhachHangRequest request,
        Authentication authentication
    ) {
        authorizationService.requireCustomerAccess(authentication, id);
        if (!userService.updateKhachHang(id, request)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new MessageResponse("Cap nhat thong tin khach hang thanh cong."));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id, Authentication authentication) {
        authorizationService.requireCustomerAccess(authentication, id);
        return ResponseEntity.ok(userService.getCustomerById(id));
    }
}
