package com.webpc.be.common.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.webpc.be.common.config.properties.CloudinaryProperties;
import com.webpc.be.common.config.properties.StorageProperties;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final StorageProperties storageProperties;
    private final CloudinaryProperties cloudinaryProperties;
    private Cloudinary cloudinary;

    @PostConstruct
    void initCloudinary() {
        if (cloudinaryProperties != null && cloudinaryProperties.isConfigured()) {
            cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudinaryProperties.getCloudName(),
                "api_key", cloudinaryProperties.getApiKey(),
                "api_secret", cloudinaryProperties.getApiSecret(),
                "secure", true
            ));
        }
    }

    public String storeAvatar(MultipartFile file) throws IOException {
        if (useCloudinary()) {
            return storeCloudinary(file, cloudinaryProperties.getFolder().getAvatar()).url();
        }
        return storeFile(file, "avatar").url();
    }

    public StoredFile storeProductImage(MultipartFile file) throws IOException {
        if (useCloudinary()) {
            return storeCloudinary(file, cloudinaryProperties.getFolder().getProduct());
        }
        return storeFile(file, "product");
    }

    public void deleteByUrl(String fileUrl) throws IOException {
        if (fileUrl == null || fileUrl.isBlank()) {
            return;
        }
        if (useCloudinary()) {
            String publicId = extractPublicIdFromUrl(fileUrl);
            if (publicId != null && !publicId.isBlank()) {
                deleteByPublicId(publicId);
            }
            return;
        }

        String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
        Path uploadPath = Paths.get(storageProperties.getUploadDir()).toAbsolutePath().normalize();
        Files.deleteIfExists(uploadPath.resolve(fileName));
    }

    public void deleteByPublicId(String publicId) throws IOException {
        if (publicId == null || publicId.isBlank()) {
            return;
        }
        if (useCloudinary()) {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return;
        }
        Path uploadPath = Paths.get(storageProperties.getUploadDir()).toAbsolutePath().normalize();
        Files.deleteIfExists(uploadPath.resolve(publicId));
    }

    private String buildPublicUrl(String fileName) {
        String baseUrl = storageProperties.getPublicBaseUrl();
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        return baseUrl + "/uploads/" + fileName;
    }

    private String resolveExtension(String originalFilename) {
        if (originalFilename == null || !originalFilename.contains(".")) {
            return "";
        }
        return originalFilename.substring(originalFilename.lastIndexOf('.'));
    }

    private StoredFile storeCloudinary(MultipartFile file, String folder) throws IOException {
        String publicId = UUID.randomUUID().toString();
        Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
            "folder", folder,
            "public_id", publicId,
            "overwrite", true,
            "resource_type", "image"
        ));
        String url = (String) result.get("secure_url");
        String storedPublicId = (String) result.get("public_id");
        return new StoredFile(url, storedPublicId);
    }

    private StoredFile storeFile(MultipartFile file, String prefix) throws IOException {
        String extension = resolveExtension(file.getOriginalFilename());
        String fileName = prefix + "-" + UUID.randomUUID() + extension;
        Path uploadPath = Paths.get(storageProperties.getUploadDir()).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);
        Files.copy(file.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        return new StoredFile(buildPublicUrl(fileName), fileName);
    }

    private boolean useCloudinary() {
        return cloudinary != null;
    }

    private String extractPublicIdFromUrl(String fileUrl) {
        String sanitized = fileUrl;
        int queryIndex = sanitized.indexOf('?');
        if (queryIndex >= 0) {
            sanitized = sanitized.substring(0, queryIndex);
        }
        int uploadIndex = sanitized.indexOf("/upload/");
        if (uploadIndex < 0) {
            return null;
        }
        String path = sanitized.substring(uploadIndex + "/upload/".length());
        if (path.startsWith("v") && path.length() > 1 && Character.isDigit(path.charAt(1))) {
            int versionSlash = path.indexOf('/');
            if (versionSlash > -1) {
                path = path.substring(versionSlash + 1);
            }
        }
        if (path.isBlank()) {
            return null;
        }
        int lastDot = path.lastIndexOf('.');
        if (lastDot > -1) {
            path = path.substring(0, lastDot);
        }
        return path;
    }

    public record StoredFile(String url, String publicId) {
    }
}
