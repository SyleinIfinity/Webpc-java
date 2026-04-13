package com.webpc.be.modules.catalog.dto.request;

import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UpdateProductRequest {

    private String tenSanPham;
    private BigDecimal giaBan;
    private BigDecimal giaKhuyenMai;
    private Integer soLuongTon;
    private String moTa;
    private Boolean trangThai;
    private Integer maDanhMuc;
    private List<MultipartFile> hinhAnhs;
    private List<String> publicIdsToDelete;
    private Integer mainImageIndex;
    private Integer mainImageId;
}
