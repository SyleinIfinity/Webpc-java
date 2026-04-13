package com.webpc.fenhanvien.common;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.webpc.fenhanvien.config.AppConfig;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ApiClient {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    private final HttpClient httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(10))
        .build();

    public <T> T get(String path, Class<T> responseType, String bearerToken) {
        return send(buildRequest(path, "GET", null, bearerToken), responseType);
    }

    public <T> List<T> getList(String path, Class<T> elementType, String bearerToken) {
        HttpRequest request = buildRequest(path, "GET", null, bearerToken);
        String body = sendRaw(request);
        try {
            JavaType listType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, elementType);
            return OBJECT_MAPPER.readValue(body, listType);
        } catch (IOException ex) {
            throw new ApiException("Khong the doc danh sach JSON tu BE.", ex);
        }
    }

    public <T> T post(String path, Object payload, Class<T> responseType, String bearerToken) {
        return send(buildRequest(path, "POST", payload, bearerToken), responseType);
    }

    public <T> T put(String path, Object payload, Class<T> responseType, String bearerToken) {
        return send(buildRequest(path, "PUT", payload, bearerToken), responseType);
    }

    public <T> T put(String path, Class<T> responseType, String bearerToken) {
        return send(buildRequest(path, "PUT", null, bearerToken), responseType);
    }

    public <T> T patch(String path, Object payload, Class<T> responseType, String bearerToken) {
        return send(buildRequest(path, "PATCH", payload, bearerToken), responseType);
    }

    public <T> T delete(String path, Class<T> responseType, String bearerToken) {
        return send(buildRequest(path, "DELETE", null, bearerToken), responseType);
    }

    public String deleteRaw(String path, String bearerToken) {
        return sendRaw(buildRequest(path, "DELETE", null, bearerToken));
    }

    public String postMultipart(String path, Map<String, List<String>> fields, List<MultipartFilePart> files, String bearerToken) {
        return sendMultipart(path, "POST", fields, files, bearerToken);
    }

    public String patchMultipart(String path, Map<String, List<String>> fields, List<MultipartFilePart> files, String bearerToken) {
        return sendMultipart(path, "PATCH", fields, files, bearerToken);
    }

    public record MultipartFilePart(String name, String filename, String contentType, byte[] content) {
    }

    private <T> T send(HttpRequest request, Class<T> responseType) {
        String body = sendRaw(request);
        try {
            return OBJECT_MAPPER.readValue(body, responseType);
        } catch (IOException ex) {
            throw new ApiException("Khong the doc JSON tra ve tu BE.", ex);
        }
    }

    private String sendRaw(HttpRequest request) {
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return response.body();
            }
            throw new ApiException(extractErrorMessage(response.body()));
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new ApiException("Yeu cau toi BE bi gian doan.", ex);
        } catch (IOException ex) {
            throw new ApiException("Khong the ket noi toi BE.", ex);
        }
    }

    private HttpRequest buildRequest(String path, String method, Object payload, String bearerToken) {
        HttpRequest.Builder builder = HttpRequest.newBuilder(resolve(path))
            .timeout(Duration.ofSeconds(20))
            .header("Accept", "application/json");

        if (bearerToken != null && !bearerToken.isBlank()) {
            builder.header("Authorization", "Bearer " + bearerToken);
        }

        if (payload != null) {
            try {
                builder.header("Content-Type", "application/json");
                String json = OBJECT_MAPPER.writeValueAsString(payload);
                builder.method(method, HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8));
            } catch (IOException ex) {
                throw new ApiException("Khong the tao JSON request gui toi BE.", ex);
            }
        } else {
            builder.method(method, HttpRequest.BodyPublishers.noBody());
        }

        return builder.build();
    }

    private String sendMultipart(String path, String method, Map<String, List<String>> fields, List<MultipartFilePart> files, String bearerToken) {
        String boundary = "----WebPCBoundary" + UUID.randomUUID();
        byte[] body = buildMultipartBody(boundary, fields, files);
        HttpRequest.Builder builder = HttpRequest.newBuilder(resolve(path))
            .timeout(Duration.ofSeconds(20))
            .header("Accept", "application/json")
            .header("Content-Type", "multipart/form-data; boundary=" + boundary);

        if (bearerToken != null && !bearerToken.isBlank()) {
            builder.header("Authorization", "Bearer " + bearerToken);
        }

        builder.method(method, HttpRequest.BodyPublishers.ofByteArray(body));
        return sendRaw(builder.build());
    }

    private static byte[] buildMultipartBody(
        String boundary,
        Map<String, List<String>> fields,
        List<MultipartFilePart> files
    ) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        String separator = "--" + boundary + "\r\n";

        if (fields != null) {
            for (var entry : fields.entrySet()) {
                String name = entry.getKey();
                if (entry.getValue() == null) {
                    continue;
                }
                for (String value : entry.getValue()) {
                    if (value == null) {
                        continue;
                    }
                    writeLine(output, separator);
                    writeLine(output, "Content-Disposition: form-data; name=\"" + name + "\"\r\n");
                    writeLine(output, "\r\n");
                    writeLine(output, value);
                    writeLine(output, "\r\n");
                }
            }
        }

        if (files != null) {
            for (MultipartFilePart filePart : files) {
                if (filePart == null || filePart.content() == null || filePart.content().length == 0) {
                    continue;
                }
                String filename = filePart.filename() == null ? "upload.bin" : filePart.filename();
                String contentType = filePart.contentType() == null ? "application/octet-stream" : filePart.contentType();
                writeLine(output, separator);
                writeLine(output,
                    "Content-Disposition: form-data; name=\"" + filePart.name() + "\"; filename=\"" + filename + "\"\r\n");
                writeLine(output, "Content-Type: " + contentType + "\r\n");
                writeLine(output, "\r\n");
                output.writeBytes(filePart.content());
                writeLine(output, "\r\n");
            }
        }

        writeLine(output, "--" + boundary + "--\r\n");
        return output.toByteArray();
    }

    private static void writeLine(ByteArrayOutputStream output, String value) {
        output.writeBytes(value.getBytes(StandardCharsets.UTF_8));
    }

    private URI resolve(String path) {
        String baseUrl = AppConfig.getInstance().getBackendApiBaseUrl();
        String normalizedBase = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
        String normalizedPath = path.startsWith("/") ? path.substring(1) : path;
        return URI.create(normalizedBase + normalizedPath);
    }

    private String extractErrorMessage(String responseBody) {
        if (responseBody == null || responseBody.isBlank()) {
            return "BE tra ve loi khong ro noi dung.";
        }
        try {
            JsonNode root = OBJECT_MAPPER.readTree(responseBody);
            if (root.hasNonNull("message")) {
                return root.get("message").asText();
            }
        } catch (IOException ignored) {
        }
        return responseBody;
    }
}
