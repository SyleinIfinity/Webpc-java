package com.webpc.fenhanvien.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class AppConfig {

    private static final AppConfig INSTANCE = new AppConfig();

    private final String backendApiBaseUrl;

    private AppConfig() {
        Properties properties = loadProperties();
        backendApiBaseUrl = readProperty(
            properties,
            "app.backend.api-base-url",
            "APP_BACKEND_API_BASE_URL",
            "http://localhost:8081/api/"
        );
    }

    public static AppConfig getInstance() {
        return INSTANCE;
    }

    public String getBackendApiBaseUrl() {
        return backendApiBaseUrl;
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = AppConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Khong the doc application.properties cho FE_NHANVIEN.", ex);
        }
        return properties;
    }

    private static String readProperty(Properties properties, String propertyKey, String envKey, String defaultValue) {
        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.isBlank()) {
            return envValue.trim();
        }
        String propertyValue = properties.getProperty(propertyKey);
        if (propertyValue != null && !propertyValue.isBlank()) {
            return propertyValue.trim();
        }
        return defaultValue;
    }
}
