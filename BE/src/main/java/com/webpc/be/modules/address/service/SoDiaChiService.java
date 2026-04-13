package com.webpc.be.modules.address.service;

import com.webpc.be.common.exception.BadRequestException;
import com.webpc.be.common.exception.ResourceNotFoundException;
import com.webpc.be.modules.address.client.LocationClient;
import com.webpc.be.modules.address.dto.request.CreateSoDiaChiRequest;
import com.webpc.be.modules.address.dto.request.UpdateSoDiaChiRequest;
import com.webpc.be.modules.address.dto.response.LocationDataResponse;
import com.webpc.be.modules.address.dto.response.SoDiaChiResponse;
import com.webpc.be.modules.address.entity.SoDiaChi;
import com.webpc.be.modules.address.repository.SoDiaChiRepository;
import com.webpc.be.modules.user.repository.KhachHangRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SoDiaChiService {

    private final SoDiaChiRepository soDiaChiRepository;
    private final LocationClient locationClient;
    private final KhachHangRepository khachHangRepository;

    @Transactional(readOnly = true)
    public List<SoDiaChiResponse> getByKhachHangId(Integer maKhachHang) {
        return soDiaChiRepository.findByMaKhachHangOrderByMacDinhDescMaSoDiaChiAsc(maKhachHang).stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public SoDiaChiResponse getById(Integer id) {
        return soDiaChiRepository.findByMaSoDiaChi(id).map(this::toResponse).orElse(null);
    }

    @Transactional
    public SoDiaChiResponse create(CreateSoDiaChiRequest request) {
        if (!khachHangRepository.existsById(request.maKhachHang())) {
            throw new BadRequestException("Khach hang khong ton tai.");
        }

        LocationClient.AddressNames names = locationClient.getAddressNames(
            request.tinhThanhId(),
            request.quanHuyenId(),
            request.phuongXaId()
        );
        if (names.tinh() == null || names.huyen() == null || names.xa() == null) {
            throw new BadRequestException("Thong tin Tinh/Huyen/Xa khong hop le.");
        }

        SoDiaChi entity = new SoDiaChi();
        entity.setMaKhachHang(request.maKhachHang());
        entity.setTenNguoiNhan(request.tenNguoiNhan());
        entity.setSoDienThoai(request.soDienThoai());
        entity.setDiaChiCuThe(request.diaChiCuThe());
        entity.setTinhThanhId(request.tinhThanhId());
        entity.setQuanHuyenId(request.quanHuyenId());
        entity.setPhuongXaId(request.phuongXaId());
        entity.setTenTinhThanh(names.tinh());
        entity.setTenQuanHuyen(names.huyen());
        entity.setTenPhuongXa(names.xa());
        entity.setMacDinh(request.macDinh());
        SoDiaChi created = soDiaChiRepository.save(entity);

        if (created.isMacDinh()) {
            soDiaChiRepository.resetDefaultAddress(created.getMaKhachHang(), created.getMaSoDiaChi());
        }

        return toResponse(created);
    }

    @Transactional
    public boolean update(Integer id, UpdateSoDiaChiRequest request) {
        SoDiaChi existing = soDiaChiRepository.findByMaSoDiaChi(id).orElse(null);
        if (existing == null) {
            return false;
        }

        if (request.tenNguoiNhan() != null && !request.tenNguoiNhan().isBlank()) {
            existing.setTenNguoiNhan(request.tenNguoiNhan());
        }
        if (request.soDienThoai() != null && !request.soDienThoai().isBlank()) {
            existing.setSoDienThoai(request.soDienThoai());
        }
        if (request.diaChiCuThe() != null && !request.diaChiCuThe().isBlank()) {
            existing.setDiaChiCuThe(request.diaChiCuThe());
        }

        String provinceId = request.tinhThanhId() != null ? request.tinhThanhId() : existing.getTinhThanhId();
        String districtId = request.quanHuyenId() != null ? request.quanHuyenId() : existing.getQuanHuyenId();
        String wardId = request.phuongXaId() != null ? request.phuongXaId() : existing.getPhuongXaId();
        boolean locationChanged = !provinceId.equals(existing.getTinhThanhId())
            || !districtId.equals(existing.getQuanHuyenId())
            || !wardId.equals(existing.getPhuongXaId());

        if (locationChanged) {
            LocationClient.AddressNames names = locationClient.getAddressNames(provinceId, districtId, wardId);
            if (names.tinh() == null || names.huyen() == null || names.xa() == null) {
                throw new BadRequestException("Thong tin dia chinh khong hop le.");
            }
            existing.setTinhThanhId(provinceId);
            existing.setQuanHuyenId(districtId);
            existing.setPhuongXaId(wardId);
            existing.setTenTinhThanh(names.tinh());
            existing.setTenQuanHuyen(names.huyen());
            existing.setTenPhuongXa(names.xa());
        }

        if (request.macDinh() != null) {
            existing.setMacDinh(request.macDinh());
            if (request.macDinh()) {
                soDiaChiRepository.resetDefaultAddress(existing.getMaKhachHang(), existing.getMaSoDiaChi());
            }
        }

        soDiaChiRepository.save(existing);
        return true;
    }

    @Transactional
    public boolean delete(Integer id) {
        if (soDiaChiRepository.findByMaSoDiaChi(id).isEmpty()) {
            return false;
        }
        soDiaChiRepository.deleteById(id);
        return true;
    }

    public List<LocationDataResponse> getProvinces() {
        return locationClient.getProvinces();
    }

    public List<LocationDataResponse> getDistricts(String provinceId) {
        return locationClient.getDistricts(provinceId);
    }

    public List<LocationDataResponse> getWards(String districtId) {
        return locationClient.getWards(districtId);
    }

    private SoDiaChiResponse toResponse(SoDiaChi entity) {
        return new SoDiaChiResponse(
            entity.getMaSoDiaChi(),
            entity.getMaKhachHang(),
            entity.getTenNguoiNhan(),
            entity.getSoDienThoai(),
            entity.getDiaChiCuThe(),
            entity.getTinhThanhId(),
            entity.getTenTinhThanh(),
            entity.getQuanHuyenId(),
            entity.getTenQuanHuyen(),
            entity.getPhuongXaId(),
            entity.getTenPhuongXa(),
            entity.isMacDinh()
        );
    }
}
