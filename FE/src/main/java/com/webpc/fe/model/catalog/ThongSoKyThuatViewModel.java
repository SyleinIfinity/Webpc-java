package com.webpc.fe.model.catalog;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ThongSoKyThuatViewModel {

    private Integer maThongSo;
    private Integer maSanPham;
    private String tenThongSo;
    private String giaTri;
}
