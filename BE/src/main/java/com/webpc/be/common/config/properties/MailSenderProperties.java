package com.webpc.be.common.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.mail")
public class MailSenderProperties {

    private String fromAddress;
    private String fromName = "WEBPC Support";
}
