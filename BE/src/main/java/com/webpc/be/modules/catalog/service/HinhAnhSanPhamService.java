package com.webpc.be.modules.catalog.service;

import com.webpc.be.common.exception.BadRequestException;
import com.webpc.be.common.util.FileStorageService;
import com.webpc.be.modules.catalog.dto.response.ImageResponse;
import com.webpc.be.modules.catalog.entity.HinhAnhSanPham;
import com.webpc.be.modules.catalog.entity.SanPham;
import com.webpc.be.modules.catalog.repository.HinhAnhSanPhamRepository;
import com.webpc.be.modules.catalog.repository.SanPhamRepository;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class HinhAnhSanPhamService {

    private final HinhAnhSanPhamRepository hinhAnhSanPhamRepository;
    private final SanPhamRepository sanPhamRepository;
    private final FileStorageService fileStorageService;

    @Transactional(readOnly = true)
    public List<ImageResponse> getByProductId(Integer productId) {
        return hinhAnhSanPhamRepository.findByMaSanPhamOrderByIdAsc(productId).stream()
            .map(image -> new ImageResponse(image.getId(), image.getUrlHinhAnh(), image.isLaAnhDaiDien(), image.getPublicId()))
            .toList();
    }

    @Transactional
    public ImageResponse addImage(Integer productId, MultipartFile file) throws IOException {
        SanPham product = sanPhamRepository.findByMaSanPham(productId)
            .orElseThrow(() -> new BadRequestException("San pham khong ton tai"));

        FileStorageService.StoredFile storedFile = fileStorageService.storeProductImage(file);
        HinhAnhSanPham image = new HinhAnhSanPham();
        image.setMaSanPham(productId);
        image.setUrlHinhAnh(storedFile.url());
        image.setPublicId(storedFile.publicId());
        image.setLaAnhDaiDien(product.getHinhAnhs() == null || product.getHinhAnhs().isEmpty());
        hinhAnhSanPhamRepository.save(image);

        return new ImageResponse(image.getId(), image.getUrlHinhAnh(), image.isLaAnhDaiDien(), image.getPublicId());
    }

    @Transactional
    public boolean deleteImage(Integer imageId) throws IOException {
        HinhAnhSanPham image = hinhAnhSanPhamRepository.findById(imageId).orElse(null);
        if (image == null) {
            return false;
        }
        fileStorageService.deleteByPublicId(image.getPublicId());
        hinhAnhSanPhamRepository.delete(image);
        return true;
    }

    @Transactional
    public boolean deleteAllByProductId(Integer productId) throws IOException {
        List<HinhAnhSanPham> images = hinhAnhSanPhamRepository.findByMaSanPhamOrderByIdAsc(productId);
        if (images.isEmpty()) {
            return false;
        }
        for (HinhAnhSanPham image : images) {
            fileStorageService.deleteByPublicId(image.getPublicId());
        }
        hinhAnhSanPhamRepository.deleteAll(images);
        return true;
    }

    @Transactional
    public boolean setMainImage(Integer productId, Integer imageId) {
        List<HinhAnhSanPham> images = hinhAnhSanPhamRepository.findByMaSanPhamOrderByIdAsc(productId);
        HinhAnhSanPham targetImage = images.stream()
            .filter(item -> item.getId().equals(imageId))
            .findFirst()
            .orElseThrow(() -> new BadRequestException("Hinh anh ID " + imageId + " khong thuoc ve san pham ID " + productId + "."));

        for (HinhAnhSanPham image : images) {
            image.setLaAnhDaiDien(image.getId().equals(targetImage.getId()));
        }
        hinhAnhSanPhamRepository.saveAll(images);
        return true;
    }
}
