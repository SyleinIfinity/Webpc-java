package com.webpc.fenhanvien.service;

import com.webpc.fenhanvien.common.ApiClient;
import com.webpc.fenhanvien.model.common.MessageResponse;
import com.webpc.fenhanvien.model.sale.KhuyenMaiResponse;
import java.util.List;
import java.util.Map;

public class SalePromotionService {

    private final ApiClient apiClient = new ApiClient();

    public List<KhuyenMaiResponse> getAll(String token) {
        return apiClient.getList("KhuyenMai", KhuyenMaiResponse.class, token);
    }

    public MessageResponse update(Integer id, Map<String, Object> payload, String token) {
        return apiClient.patch("KhuyenMai/" + id, payload, MessageResponse.class, token);
    }
}

