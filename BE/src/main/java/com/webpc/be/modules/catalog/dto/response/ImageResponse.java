package com.webpc.be.modules.catalog.dto.response;

public record ImageResponse(
    Integer id,
    String url,
    boolean laAnhDaiDien,
    String publicId
) {
}
