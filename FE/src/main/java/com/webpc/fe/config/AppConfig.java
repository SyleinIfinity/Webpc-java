package com.webpc.fe.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class AppConfig {

    private static final AppConfig INSTANCE = load();

    private final String backendApiBaseUrl;
    private final boolean cartEnabled;
    private final boolean promotionsEnabled;
    private final boolean ordersEnabled;
    private final boolean paymentEnabled;
    private final boolean chatEnabled;

    private AppConfig(Properties properties) {
        backendApiBaseUrl = normalizeBaseUrl(
            readProperty(properties, "app.backend.api-base-url", "APP_BACKEND_API_BASE_URL", "http://localhost:8081/api/")
        );
        cartEnabled = readBoolean(properties, "app.feature.cart-enabled", "APP_FEATURE_CART_ENABLED", false);
        promotionsEnabled = readBoolean(properties, "app.feature.promotions-enabled", "APP_FEATURE_PROMOTIONS_ENABLED", false);
        ordersEnabled = readBoolean(properties, "app.feature.orders-enabled", "APP_FEATURE_ORDERS_ENABLED", false);
        paymentEnabled = readBoolean(properties, "app.feature.payment-enabled", "APP_FEATURE_PAYMENT_ENABLED", false);
        chatEnabled = readBoolean(properties, "app.feature.chat-enabled", "APP_FEATURE_CHAT_ENABLED", false);
    }

    public static AppConfig getInstance() {
        return INSTANCE;
    }

    public String getBackendApiBaseUrl() {
        return backendApiBaseUrl;
    }

    public boolean isCartEnabled() {
        return cartEnabled;
    }

    public boolean isPromotionsEnabled() {
        return promotionsEnabled;
    }

    public boolean isOrdersEnabled() {
        return ordersEnabled;
    }

    public boolean isPaymentEnabled() {
        return paymentEnabled;
    }

    public boolean isChatEnabled() {
        return chatEnabled;
    }

    private static AppConfig load() {
        Properties properties = new Properties();
        try (InputStream inputStream = AppConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Khong the doc application.properties cho FE.", ex);
        }
        return new AppConfig(properties);
    }

    private static String readProperty(Properties properties, String key, String envKey, String defaultValue) {
        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }
        return properties.getProperty(key, defaultValue);
    }

    private static boolean readBoolean(Properties properties, String key, String envKey, boolean defaultValue) {
        return Boolean.parseBoolean(readProperty(properties, key, envKey, String.valueOf(defaultValue)));
    }

    private static String normalizeBaseUrl(String baseUrl) {
        String value = baseUrl == null || baseUrl.isBlank() ? "http://localhost:8081/api/" : baseUrl.trim();
        return value.endsWith("/") ? value : value + "/";
    }
}
