package com.webpc.be.modules.address.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LocationDataResponse(
    String id,
    String name,
    @JsonProperty("name_en") String nameEn,
    @JsonProperty("full_name") String fullName,
    @JsonProperty("full_name_en") String fullNameEn,
    String latitude,
    String longitude
) {
}
