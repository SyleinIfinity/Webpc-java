package com.webpc.be.modules.promotion.service;

import com.webpc.be.common.exception.BadRequestException;
import com.webpc.be.modules.promotion.dto.request.CreateKhuyenMaiRequest;
import com.webpc.be.modules.promotion.dto.request.UpdateKhuyenMaiRequest;
import com.webpc.be.modules.promotion.dto.response.KhuyenMaiResponse;
import com.webpc.be.modules.promotion.entity.KhuyenMai;
import com.webpc.be.modules.promotion.repository.KhuyenMaiRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KhuyenMaiService {

    private final KhuyenMaiRepository khuyenMaiRepository;

    @Transactional(readOnly = true)
    public List<KhuyenMaiResponse> getAll() {
        return khuyenMaiRepository.findAll().stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public KhuyenMaiResponse getById(Integer id) {
        if (id == null) {
            return null;
        }
        return khuyenMaiRepository.findById(id).map(this::toResponse).orElse(null);
    }

    @Transactional
    public KhuyenMaiResponse create(CreateKhuyenMaiRequest request) {
        validateDateRange(request.ngayBatDau(), request.ngayKetThuc());
        if (khuyenMaiRepository.findByMaCodeKM(request.maCodeKM()).isPresent()) {
            throw new BadRequestException("Ma khuyen mai '" + request.maCodeKM() + "' da ton tai.");
        }

        KhuyenMai entity = new KhuyenMai();
        entity.setMaCodeKM(request.maCodeKM());
        entity.setTenChuongTrinh(request.tenChuongTrinh());
        entity.setGiaTriGiam(request.giaTriGiam());
        entity.setLoaiGiam(request.loaiGiam());
        entity.setDonHangToiThieu(request.donHangToiThieu());
        entity.setGiamToiDa(request.giamToiDa());
        entity.setNgayBatDau(request.ngayBatDau());
        entity.setNgayKetThuc(request.ngayKetThuc());
        entity.setSoLuongConLai(request.soLuongConLai());

        khuyenMaiRepository.save(entity);
        return toResponse(entity);
    }

    @Transactional
    public boolean update(Integer id, UpdateKhuyenMaiRequest request) {
        if (id == null) {
            return false;
        }
        KhuyenMai entity = khuyenMaiRepository.findById(id).orElse(null);
        if (entity == null) {
            return false;
        }

        validateDateRange(request.ngayBatDau(), request.ngayKetThuc());

        if (!entity.getMaCodeKM().equalsIgnoreCase(request.maCodeKM())
            && khuyenMaiRepository.findByMaCodeKM(request.maCodeKM()).isPresent()) {
            throw new BadRequestException("Ma khuyen mai '" + request.maCodeKM() + "' da ton tai.");
        }

        entity.setMaCodeKM(request.maCodeKM());
        entity.setTenChuongTrinh(request.tenChuongTrinh());
        entity.setGiaTriGiam(request.giaTriGiam());
        entity.setLoaiGiam(request.loaiGiam());
        entity.setDonHangToiThieu(request.donHangToiThieu());
        entity.setGiamToiDa(request.giamToiDa());
        entity.setNgayBatDau(request.ngayBatDau());
        entity.setNgayKetThuc(request.ngayKetThuc());
        entity.setSoLuongConLai(request.soLuongConLai());
        khuyenMaiRepository.save(entity);
        return true;
    }

    @Transactional
    public boolean delete(Integer id) {
        if (id == null || !khuyenMaiRepository.existsById(id)) {
            return false;
        }
        khuyenMaiRepository.deleteById(id);
        return true;
    }

    private void validateDateRange(java.time.LocalDateTime start, java.time.LocalDateTime end) {
        if (start != null && end != null && end.isBefore(start)) {
            throw new BadRequestException("Ngay ket thuc phai lon hon hoac bang ngay bat dau.");
        }
    }

    private KhuyenMaiResponse toResponse(KhuyenMai entity) {
        return new KhuyenMaiResponse(
            entity.getMaKhuyenMai(),
            entity.getMaCodeKM(),
            entity.getTenChuongTrinh(),
            entity.getGiaTriGiam(),
            entity.getLoaiGiam(),
            entity.getDonHangToiThieu(),
            entity.getGiamToiDa(),
            entity.getNgayBatDau(),
            entity.getNgayKetThuc(),
            entity.getSoLuongConLai()
        );
    }
}

