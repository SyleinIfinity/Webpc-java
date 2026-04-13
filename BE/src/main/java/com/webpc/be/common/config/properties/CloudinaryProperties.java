package com.webpc.be.common.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.cloudinary")
public class CloudinaryProperties {

    private String cloudName;
    private String apiKey;
    private String apiSecret;
    private Folder folder = new Folder();

    public boolean isConfigured() {
        return StringUtils.hasText(cloudName)
            && StringUtils.hasText(apiKey)
            && StringUtils.hasText(apiSecret);
    }

    @Getter
    @Setter
    public static class Folder {

        private String avatar = "WEBPC/Avatars";
        private String product = "WEBPC/Products";
    }
}
