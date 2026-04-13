package com.webpc.fenhanvien.model.admin;

import lombok.Data;

@Data
public class ImageDto {
    private Integer id;
    private String url;
    private String publicId;
    private Boolean laAnhDaiDien;
}
