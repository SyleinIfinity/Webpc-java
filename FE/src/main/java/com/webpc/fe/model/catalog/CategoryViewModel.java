package com.webpc.fe.model.catalog;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryViewModel {

    private Integer maDanhMuc;
    private String tenDanhMuc;
    private String moTa;
    private Integer maDanhMucCha;
    private String tenDanhMucCha;
    private int soLuongDanhMucCon;
}
