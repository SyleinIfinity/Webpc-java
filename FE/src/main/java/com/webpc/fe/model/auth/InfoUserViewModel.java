package com.webpc.fe.model.auth;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InfoUserViewModel {

    private Integer maKhachHang;
    private String hoTen;
    private String soDienThoai;
    private String email;
    private String tenDangNhap;
    private String gioiTinh;
    private String avatarUrl = "https://via.placeholder.com/150";
}
