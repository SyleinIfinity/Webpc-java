package com.webpc.fe.model.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GiaoDichViewModel {

    private Integer maGiaoDich;
    private LocalDateTime ngayGiaoDich;
    private BigDecimal soTien = BigDecimal.ZERO;
    private String trangThai;
    private String phuongThuc;

    public String getNgayGiaoDichDisplay() {
        if (ngayGiaoDich == null) {
            return "";
        }
        return ngayGiaoDich.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}
