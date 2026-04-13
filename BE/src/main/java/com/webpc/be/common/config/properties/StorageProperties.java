package com.webpc.be.common.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.storage")
public class StorageProperties {

    private String uploadDir = "./uploads";
    private String publicBaseUrl = "http://localhost:8081";
}
