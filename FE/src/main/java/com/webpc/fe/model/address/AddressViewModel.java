package com.webpc.fe.model.address;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressViewModel {

    private Integer maSoDiaChi;
    private Integer maKhachHang;
    private String tenNguoiNhan;
    private String soDienThoai;
    private String diaChiCuThe;
    private String tinhThanhId;
    private String tenTinhThanh;
    private String quanHuyenId;
    private String tenQuanHuyen;
    private String phuongXaId;
    private String tenPhuongXa;
    private boolean macDinh;

    public String getFullAddress() {
        return "%s, %s, %s, %s".formatted(
            nullSafe(diaChiCuThe),
            nullSafe(tenPhuongXa),
            nullSafe(tenQuanHuyen),
            nullSafe(tenTinhThanh)
        );
    }

    private String nullSafe(String value) {
        return value == null ? "" : value;
    }
}
