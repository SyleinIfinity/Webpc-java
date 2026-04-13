package com.webpc.be.modules.catalog.service;

import com.webpc.be.common.exception.BadRequestException;
import com.webpc.be.common.exception.ResourceNotFoundException;
import com.webpc.be.modules.catalog.dto.request.CreateCategoryRequest;
import com.webpc.be.modules.catalog.dto.request.UpdateCategoryRequest;
import com.webpc.be.modules.catalog.dto.response.CategoryResponse;
import com.webpc.be.modules.catalog.entity.DanhMuc;
import com.webpc.be.modules.catalog.repository.DanhMucRepository;
import com.webpc.be.modules.catalog.repository.SanPhamRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DanhMucService {

    private final DanhMucRepository danhMucRepository;
    private final SanPhamRepository sanPhamRepository;

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAll() {
        return danhMucRepository.findAllByOrderByMaDanhMucAsc().stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse getById(Integer id) {
        return danhMucRepository.findByMaDanhMuc(id)
            .map(this::toResponse)
            .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay danh muc"));
    }

    @Transactional
    public CategoryResponse create(CreateCategoryRequest request) {
        DanhMuc parent = null;
        if (request.maDanhMucCha() != null && request.maDanhMucCha() > 0) {
            parent = danhMucRepository.findByMaDanhMuc(request.maDanhMucCha())
                .orElseThrow(() -> new BadRequestException("Ma danh muc cha khong ton tai"));
        }

        DanhMuc entity = new DanhMuc();
        entity.setTenDanhMuc(request.tenDanhMuc());
        entity.setMoTa(request.moTa());
        entity.setDanhMucCha(parent);
        danhMucRepository.save(entity);
        return getById(entity.getMaDanhMuc());
    }

    @Transactional
    public CategoryResponse update(Integer id, UpdateCategoryRequest request) {
        DanhMuc entity = danhMucRepository.findByMaDanhMuc(id)
            .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay danh muc de cap nhat"));

        if (request.tenDanhMuc() != null && !request.tenDanhMuc().isBlank()) {
            entity.setTenDanhMuc(request.tenDanhMuc());
        }
        if (request.moTa() != null) {
            entity.setMoTa(request.moTa());
        }
        if (request.maDanhMucCha() != null) {
            if (request.maDanhMucCha() <= 0) {
                entity.setDanhMucCha(null);
            } else {
                if (request.maDanhMucCha().equals(id)) {
                    throw new BadRequestException("Mot danh muc khong the lam cha cua chinh no!");
                }
                DanhMuc parent = danhMucRepository.findByMaDanhMuc(request.maDanhMucCha())
                    .orElseThrow(() -> new BadRequestException("Ma danh muc cha khong ton tai"));
                entity.setDanhMucCha(parent);
            }
        }

        danhMucRepository.save(entity);
        return getById(id);
    }

    @Transactional
    public boolean delete(Integer id) {
        DanhMuc entity = danhMucRepository.findByMaDanhMuc(id).orElse(null);
        if (entity == null) {
            return false;
        }
        if (entity.getDanhMucCons() != null && !entity.getDanhMucCons().isEmpty()) {
            throw new BadRequestException("Khong the xoa danh muc nay vi dang chua cac danh muc con.");
        }
        if (sanPhamRepository.existsByMaDanhMuc(id)) {
            throw new BadRequestException("Khong the xoa danh muc nay vi dang chua san pham.");
        }
        danhMucRepository.delete(entity);
        return true;
    }

    private CategoryResponse toResponse(DanhMuc entity) {
        return new CategoryResponse(
            entity.getMaDanhMuc(),
            entity.getTenDanhMuc(),
            entity.getMoTa(),
            entity.getDanhMucCha() != null ? entity.getDanhMucCha().getMaDanhMuc() : null,
            entity.getDanhMucCha() != null ? entity.getDanhMucCha().getTenDanhMuc() : null,
            entity.getDanhMucCons() != null ? entity.getDanhMucCons().size() : 0
        );
    }
}
