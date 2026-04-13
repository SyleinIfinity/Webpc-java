package com.webpc.be.common.config;

import com.webpc.be.common.config.properties.StorageProperties;
import jakarta.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final StorageProperties storageProperties;

    @PostConstruct
    void ensureUploadDirectory() throws Exception {
        Files.createDirectories(resolveUploadPath());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
            .addResourceLocations(resolveUploadPath().toUri().toString());
    }

    private Path resolveUploadPath() {
        return Paths.get(storageProperties.getUploadDir()).toAbsolutePath().normalize();
    }
}
