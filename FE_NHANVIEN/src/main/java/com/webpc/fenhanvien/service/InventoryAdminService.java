package com.webpc.fenhanvien.service;

import com.webpc.fenhanvien.common.ApiClient;
import com.webpc.fenhanvien.model.admin.PhieuNhapResponse;
import java.util.List;

public class InventoryAdminService {

    private final ApiClient apiClient = new ApiClient();

    public List<PhieuNhapResponse> getPhieuNhaps(String token) {
        return apiClient.getList("PhieuNhap", PhieuNhapResponse.class, token);
    }
}
