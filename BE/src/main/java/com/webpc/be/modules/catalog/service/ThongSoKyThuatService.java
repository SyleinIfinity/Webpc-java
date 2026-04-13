package com.webpc.be.modules.catalog.service;

import com.webpc.be.modules.catalog.dto.request.CreateThongSoRequest;
import com.webpc.be.modules.catalog.dto.request.UpdateThongSoRequest;
import com.webpc.be.modules.catalog.dto.response.ThongSoKyThuatResponse;
import com.webpc.be.modules.catalog.entity.ThongSoKyThuat;
import com.webpc.be.modules.catalog.repository.ThongSoKyThuatRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ThongSoKyThuatService {

    private final ThongSoKyThuatRepository thongSoKyThuatRepository;

    @Transactional(readOnly = true)
    public List<ThongSoKyThuatResponse> getByProductId(Integer maSanPham) {
        return thongSoKyThuatRepository.findByMaSanPhamOrderByMaThongSoAsc(maSanPham).stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional
    public ThongSoKyThuatResponse create(CreateThongSoRequest request) {
        ThongSoKyThuat entity = new ThongSoKyThuat();
        entity.setMaSanPham(request.maSanPham());
        entity.setTenThongSo(request.tenThongSo());
        entity.setGiaTri(request.giaTri());
        thongSoKyThuatRepository.save(entity);
        return toResponse(entity);
    }

    @Transactional
    public boolean update(Integer id, UpdateThongSoRequest request) {
        ThongSoKyThuat entity = thongSoKyThuatRepository.findByMaThongSo(id).orElse(null);
        if (entity == null) {
            return false;
        }
        entity.setTenThongSo(request.tenThongSo());
        entity.setGiaTri(request.giaTri());
        thongSoKyThuatRepository.save(entity);
        return true;
    }

    @Transactional
    public boolean delete(Integer id) {
        ThongSoKyThuat entity = thongSoKyThuatRepository.findByMaThongSo(id).orElse(null);
        if (entity == null) {
            return false;
        }
        thongSoKyThuatRepository.delete(entity);
        return true;
    }

    private ThongSoKyThuatResponse toResponse(ThongSoKyThuat entity) {
        return new ThongSoKyThuatResponse(
            entity.getMaThongSo(),
            entity.getMaSanPham(),
            entity.getTenThongSo(),
            entity.getGiaTri()
        );
    }
}
