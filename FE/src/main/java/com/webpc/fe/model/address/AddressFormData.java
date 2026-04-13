package com.webpc.fe.model.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressFormData {

    @JsonProperty("MaSoDiaChi")
    private Integer maSoDiaChi;

    @JsonProperty("MaKhachHang")
    private Integer maKhachHang;

    @JsonProperty("TenNguoiNhan")
    private String tenNguoiNhan;

    @JsonProperty("SoDienThoai")
    private String soDienThoai;

    @JsonProperty("MaTinh")
    private String maTinh;

    @JsonProperty("MaHuyen")
    private String maHuyen;

    @JsonProperty("MaXa")
    private String maXa;

    @JsonProperty("TinhThanh")
    private String tinhThanh;

    @JsonProperty("QuanHuyen")
    private String quanHuyen;

    @JsonProperty("PhuongXa")
    private String phuongXa;

    @JsonProperty("DiaChiCuThe")
    private String diaChiCuThe;

    @JsonProperty("IsDefault")
    private boolean isDefault;
}
