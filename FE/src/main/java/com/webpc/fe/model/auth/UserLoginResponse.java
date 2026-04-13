package com.webpc.fe.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLoginResponse {

    private Integer maNhanVien;
    private Integer maKhachHang;
    private String hoTen;
    private String tenDangNhap;
    private Integer maVaiTro;
    private String tenVaiTro;
    private String token;
    private String soDienThoai;
}
