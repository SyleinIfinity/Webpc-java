package com.webpc.be.common.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.location-api")
public class LocationApiProperties {

    private String baseUrl = "https://esgoo.net/api-tinhthanh";
}
