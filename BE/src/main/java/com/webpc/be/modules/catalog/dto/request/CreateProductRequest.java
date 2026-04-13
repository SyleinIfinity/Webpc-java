package com.webpc.be.modules.catalog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CreateProductRequest {

    @NotBlank(message = "Ten san pham khong duoc de trong")
    private String tenSanPham;

    private BigDecimal giaBan;
    private Integer soLuongTon;
    private String moTa;

    @NotNull
    private Integer maDanhMuc;

    private List<MultipartFile> hinhAnhs;
    private BigDecimal giaKhuyenMai;
    private Boolean trangThai = true;
    private Integer mainImageIndex;
}
