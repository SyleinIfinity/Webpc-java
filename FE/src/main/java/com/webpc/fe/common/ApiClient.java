package com.webpc.fe.common;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.webpc.fe.config.AppConfig;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

public class ApiClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final AppConfig appConfig;

    public ApiClient() {
        this.appConfig = AppConfig.getInstance();
        this.httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public <T> T get(String path, Class<T> responseType, String bearerToken) {
        return exchange(readRequest("GET", path, null, bearerToken), responseType);
    }

    public JsonNode getJson(String path, String bearerToken) {
        return exchange(readRequest("GET", path, null, bearerToken), JsonNode.class);
    }

    public <T> List<T> getList(String path, Class<T> elementType, String bearerToken) {
        HttpRequest request = readRequest("GET", path, null, bearerToken);
        String body = send(request);
        try {
            JavaType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, elementType);
            return objectMapper.readValue(body, listType);
        } catch (IOException ex) {
            throw new ApiException("Khong the doc danh sach JSON tu BE.", ex);
        }
    }

    public <T> T post(String path, Object payload, Class<T> responseType, String bearerToken) {
        return exchange(readRequest("POST", path, payload, bearerToken), responseType);
    }

    public JsonNode postJson(String path, Object payload, String bearerToken) {
        return exchange(readRequest("POST", path, payload, bearerToken), JsonNode.class);
    }

    public void postNoResponse(String path, Object payload, String bearerToken) {
        send(readRequest("POST", path, payload, bearerToken));
    }

    public void putNoResponse(String path, Object payload, String bearerToken) {
        send(readRequest("PUT", path, payload, bearerToken));
    }

    public void patchNoResponse(String path, Object payload, String bearerToken) {
        send(readRequest("PATCH", path, payload, bearerToken));
    }

    public void delete(String path, String bearerToken) {
        send(readRequest("DELETE", path, null, bearerToken));
    }

    private <T> T exchange(HttpRequest request, Class<T> responseType) {
        String body = send(request);
        if (responseType == String.class) {
            return responseType.cast(body);
        }
        try {
            return objectMapper.readValue(body, responseType);
        } catch (IOException ex) {
            throw new ApiException("Khong the doc JSON tra ve tu BE.", ex);
        }
    }

    private String send(HttpRequest request) {
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return response.body() == null ? "" : response.body();
            }
            throw new ApiException(response.statusCode(), extractErrorMessage(response.body()));
        } catch (IOException | InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new ApiException("Khong the ket noi FE -> BE.", ex);
        }
    }

    private HttpRequest readRequest(String method, String path, Object payload, String bearerToken) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
            .uri(buildUri(path))
            .timeout(Duration.ofSeconds(20))
            .header("Accept", "application/json");

        if (bearerToken != null && !bearerToken.isBlank()) {
            builder.header("Authorization", "Bearer " + bearerToken);
        }

        String body = null;
        if (payload != null) {
            try {
                body = objectMapper.writeValueAsString(payload);
            } catch (IOException ex) {
                throw new ApiException("Khong the tao JSON request gui toi BE.", ex);
            }
            builder.header("Content-Type", "application/json");
        }

        return switch (method) {
            case "POST" -> builder.POST(body == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(body)).build();
            case "PUT" -> builder.PUT(body == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(body)).build();
            case "PATCH" -> builder.method("PATCH", body == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(body)).build();
            case "DELETE" -> builder.DELETE().build();
            default -> builder.GET().build();
        };
    }

    private URI buildUri(String path) {
        String normalizedPath = path.startsWith("/") ? path.substring(1) : path;
        return URI.create(appConfig.getBackendApiBaseUrl() + normalizedPath);
    }

    private String extractErrorMessage(String body) {
        if (body == null || body.isBlank()) {
            return "BE tra ve loi khong ro noi dung.";
        }
        try {
            JsonNode root = objectMapper.readTree(body);
            if (root.hasNonNull("message")) {
                return root.get("message").asText();
            }
        } catch (IOException ignored) {
        }
        return body;
    }
}
