package com.webpc.be.modules.catalog.service;

import com.webpc.be.common.exception.BadRequestException;
import com.webpc.be.common.util.FileStorageService;
import com.webpc.be.modules.catalog.dto.request.CreateProductRequest;
import com.webpc.be.modules.catalog.dto.request.UpdateProductRequest;
import com.webpc.be.modules.catalog.dto.response.ImageResponse;
import com.webpc.be.modules.catalog.dto.response.ProductResponse;
import com.webpc.be.modules.catalog.entity.DanhMuc;
import com.webpc.be.modules.catalog.entity.HinhAnhSanPham;
import com.webpc.be.modules.catalog.entity.SanPham;
import com.webpc.be.modules.catalog.repository.DanhMucRepository;
import com.webpc.be.modules.catalog.repository.HinhAnhSanPhamRepository;
import com.webpc.be.modules.catalog.repository.SanPhamRepository;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SanPhamService {

    private final SanPhamRepository sanPhamRepository;
    private final DanhMucRepository danhMucRepository;
    private final HinhAnhSanPhamRepository hinhAnhSanPhamRepository;
    private final FileStorageService fileStorageService;

    @Transactional(readOnly = true)
    public List<ProductResponse> getAll() {
        return sanPhamRepository.findAllByOrderByMaSanPhamAsc().stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse getById(Integer id) {
        return sanPhamRepository.findByMaSanPham(id).map(this::toResponse).orElse(null);
    }

    @Transactional
    public void create(CreateProductRequest request) throws IOException {
        ensureCategoryExists(request.getMaDanhMuc());

        SanPham entity = new SanPham();
        entity.setTenSanPham(request.getTenSanPham());
        entity.setGiaBan(defaultMoney(request.getGiaBan()));
        entity.setGiaKhuyenMai(request.getGiaKhuyenMai());
        entity.setTrangThai(request.getTrangThai() == null || request.getTrangThai());
        entity.setSoLuongTon(request.getSoLuongTon() == null ? 0 : request.getSoLuongTon());
        entity.setMoTa(request.getMoTa());
        entity.setMaDanhMuc(request.getMaDanhMuc());
        sanPhamRepository.save(entity);

        List<HinhAnhSanPham> newImages = saveImages(entity.getMaSanPham(), request.getHinhAnhs());
        Integer mainImageId = resolveMainImageId(request.getMainImageIndex(), newImages);
        if (mainImageId != null) {
            setMainImage(entity.getMaSanPham(), mainImageId);
        } else {
            ensureMainImage(entity.getMaSanPham());
        }
    }

    @Transactional
    public boolean update(Integer id, UpdateProductRequest request) throws IOException {
        SanPham entity = sanPhamRepository.findByMaSanPham(id).orElse(null);
        if (entity == null) {
            return false;
        }

        if (request.getTenSanPham() != null && !request.getTenSanPham().isBlank()) {
            entity.setTenSanPham(request.getTenSanPham());
        }
        if (request.getGiaBan() != null) {
            entity.setGiaBan(request.getGiaBan());
        }
        if (request.getGiaKhuyenMai() != null) {
            entity.setGiaKhuyenMai(request.getGiaKhuyenMai());
        }
        if (request.getSoLuongTon() != null) {
            entity.setSoLuongTon(request.getSoLuongTon());
        }
        if (request.getMoTa() != null && !request.getMoTa().isBlank()) {
            entity.setMoTa(request.getMoTa());
        }
        if (request.getTrangThai() != null) {
            entity.setTrangThai(request.getTrangThai());
        }
        if (request.getMaDanhMuc() != null) {
            ensureCategoryExists(request.getMaDanhMuc());
            entity.setMaDanhMuc(request.getMaDanhMuc());
        }
        sanPhamRepository.save(entity);

        if (request.getPublicIdsToDelete() != null && !request.getPublicIdsToDelete().isEmpty()) {
            for (String publicId : request.getPublicIdsToDelete()) {
                fileStorageService.deleteByPublicId(publicId);
            }
            hinhAnhSanPhamRepository.deleteByPublicIdIn(request.getPublicIdsToDelete());
        }

        List<HinhAnhSanPham> newImages = saveImages(id, request.getHinhAnhs());
        Integer mainImageId = request.getMainImageId();
        if (mainImageId == null) {
            mainImageId = resolveMainImageId(request.getMainImageIndex(), newImages);
        }
        if (mainImageId != null) {
            boolean updated = setMainImage(id, mainImageId);
            if (!updated) {
                ensureMainImage(id);
            }
        } else {
            ensureMainImage(id);
        }
        return true;
    }

    @Transactional
    public boolean delete(Integer id) throws IOException {
        SanPham entity = sanPhamRepository.findByMaSanPham(id).orElse(null);
        if (entity == null) {
            return false;
        }
        for (HinhAnhSanPham image : entity.getHinhAnhs()) {
            fileStorageService.deleteByPublicId(image.getPublicId());
        }
        hinhAnhSanPhamRepository.deleteByMaSanPham(id);
        sanPhamRepository.delete(entity);
        return true;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getByCategoryId(Integer maDanhMuc) {
        return sanPhamRepository.findByMaDanhMucOrderByMaSanPhamAsc(maDanhMuc).stream()
            .map(this::toResponse)
            .toList();
    }

    private List<HinhAnhSanPham> saveImages(Integer productId, List<org.springframework.web.multipart.MultipartFile> files)
        throws IOException {
        if (files == null || files.isEmpty()) {
            return List.of();
        }

        List<HinhAnhSanPham> images = new ArrayList<>();
        for (var file : files) {
            if (file == null || file.isEmpty()) {
                continue;
            }
            FileStorageService.StoredFile storedFile = fileStorageService.storeProductImage(file);
            HinhAnhSanPham image = new HinhAnhSanPham();
            image.setMaSanPham(productId);
            image.setUrlHinhAnh(storedFile.url());
            image.setPublicId(storedFile.publicId());
            image.setLaAnhDaiDien(false);
            images.add(image);
        }
        if (images.isEmpty()) {
            return List.of();
        }
        hinhAnhSanPhamRepository.saveAll(images);
        return images;
    }

    private void ensureCategoryExists(Integer maDanhMuc) {
        DanhMuc danhMuc = danhMucRepository.findByMaDanhMuc(maDanhMuc).orElse(null);
        if (danhMuc == null) {
            throw new BadRequestException("Danh muc khong ton tai.");
        }
    }

    private ProductResponse toResponse(SanPham entity) {
        return new ProductResponse(
            entity.getMaSanPham(),
            entity.getTenSanPham(),
            entity.getGiaBan(),
            entity.getGiaKhuyenMai(),
            entity.isTrangThai(),
            entity.getSoLuongTon(),
            entity.getMoTa(),
            entity.getDanhMuc() != null ? entity.getDanhMuc().getTenDanhMuc() : null,
            entity.getHinhAnhs() == null ? List.of() : entity.getHinhAnhs().stream()
                .map(image -> new ImageResponse(image.getId(), image.getUrlHinhAnh(), image.isLaAnhDaiDien(), image.getPublicId()))
                .toList()
        );
    }

    private BigDecimal defaultMoney(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private Integer resolveMainImageId(Integer mainImageIndex, List<HinhAnhSanPham> newImages) {
        if (mainImageIndex == null || newImages == null || newImages.isEmpty()) {
            return null;
        }
        if (mainImageIndex < 0 || mainImageIndex >= newImages.size()) {
            return null;
        }
        return newImages.get(mainImageIndex).getId();
    }

    private boolean setMainImage(Integer productId, Integer imageId) {
        List<HinhAnhSanPham> images = hinhAnhSanPhamRepository.findByMaSanPhamOrderByIdAsc(productId);
        if (images.isEmpty()) {
            return false;
        }
        boolean found = false;
        for (HinhAnhSanPham image : images) {
            boolean isMain = image.getId().equals(imageId);
            if (isMain) {
                found = true;
            }
            image.setLaAnhDaiDien(isMain);
        }
        if (!found) {
            return false;
        }
        hinhAnhSanPhamRepository.saveAll(images);
        return true;
    }

    private void ensureMainImage(Integer productId) {
        List<HinhAnhSanPham> images = hinhAnhSanPhamRepository.findByMaSanPhamOrderByIdAsc(productId);
        if (images.isEmpty()) {
            return;
        }
        boolean hasMain = images.stream().anyMatch(HinhAnhSanPham::isLaAnhDaiDien);
        if (!hasMain) {
            images.get(0).setLaAnhDaiDien(true);
            hinhAnhSanPhamRepository.saveAll(images);
        }
    }
}
