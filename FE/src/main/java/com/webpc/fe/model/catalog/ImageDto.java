package com.webpc.fe.model.catalog;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageDto {

    private Integer id;
    private String url;
    private boolean laAnhDaiDien;
    private String publicId;
}
