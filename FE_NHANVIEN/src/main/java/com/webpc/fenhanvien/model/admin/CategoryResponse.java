package com.webpc.fenhanvien.model.admin;

import lombok.Data;

@Data
public class CategoryResponse {
    private Integer maDanhMuc;
    private String tenDanhMuc;
    private String moTa;
    private Integer maDanhMucCha;
    private String tenDanhMucCha;
    private Integer soLuongDanhMucCon;
}
