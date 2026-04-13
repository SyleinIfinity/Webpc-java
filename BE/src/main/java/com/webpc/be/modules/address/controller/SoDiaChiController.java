package com.webpc.be.modules.address.controller;

import com.webpc.be.common.response.MessageResponse;
import com.webpc.be.security.AuthorizationService;
import com.webpc.be.modules.address.dto.request.CreateSoDiaChiRequest;
import com.webpc.be.modules.address.dto.request.UpdateSoDiaChiRequest;
import com.webpc.be.modules.address.service.SoDiaChiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/SoDiaChi")
@RequiredArgsConstructor
public class SoDiaChiController {

    private final SoDiaChiService soDiaChiService;
    private final AuthorizationService authorizationService;

    @GetMapping("provinces")
    public ResponseEntity<?> getProvinces() {
        return ResponseEntity.ok(soDiaChiService.getProvinces());
    }

    @GetMapping("districts/{provinceId}")
    public ResponseEntity<?> getDistricts(@PathVariable String provinceId) {
        return ResponseEntity.ok(soDiaChiService.getDistricts(provinceId));
    }

    @GetMapping("wards/{districtId}")
    public ResponseEntity<?> getWards(@PathVariable String districtId) {
        return ResponseEntity.ok(soDiaChiService.getWards(districtId));
    }

    @GetMapping("khachhang/{maKhachHang}")
    public ResponseEntity<?> getByCustomer(@PathVariable Integer maKhachHang, Authentication authentication) {
        authorizationService.requireCustomerAccess(authentication, maKhachHang);
        return ResponseEntity.ok(soDiaChiService.getByKhachHangId(maKhachHang));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id, Authentication authentication) {
        var item = soDiaChiService.getById(id);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        authorizationService.requireCustomerAccess(authentication, item.maKhachHang());
        return ResponseEntity.ok(item);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateSoDiaChiRequest request, Authentication authentication) {
        authorizationService.requireCustomerAccess(authentication, request.maKhachHang());
        return ResponseEntity.status(HttpStatus.CREATED).body(soDiaChiService.create(request));
    }

    @PatchMapping("{id}")
    public ResponseEntity<MessageResponse> update(
        @PathVariable Integer id,
        @RequestBody UpdateSoDiaChiRequest request,
        Authentication authentication
    ) {
        var item = soDiaChiService.getById(id);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        authorizationService.requireCustomerAccess(authentication, item.maKhachHang());
        if (!soDiaChiService.update(id, request)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new MessageResponse("Cap nhat thanh cong."));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable Integer id, Authentication authentication) {
        var item = soDiaChiService.getById(id);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        authorizationService.requireCustomerAccess(authentication, item.maKhachHang());
        if (!soDiaChiService.delete(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new MessageResponse("Xoa thanh cong."));
    }
}
