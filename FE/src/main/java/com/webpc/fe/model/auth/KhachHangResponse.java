package com.webpc.fe.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KhachHangResponse {

    private Integer maKhachHang;
    private String hoTen;
    private String soDienThoai;
    private String email;
    private Integer maTaiKhoan;
    private String tenDangNhap;
    private boolean coTaiKhoan;
}
