package com.webpc.fenhanvien.service;

import com.webpc.fenhanvien.common.ApiClient;
import com.webpc.fenhanvien.model.common.MessageResponse;
import com.webpc.fenhanvien.model.sale.DonHangListResponse;
import com.webpc.fenhanvien.model.sale.DonHangResponse;
import java.util.List;
import java.util.Map;

public class SaleOrderService {

    private final ApiClient apiClient = new ApiClient();

    public List<DonHangListResponse> getAll(String token) {
        return apiClient.getList("DonHang", DonHangListResponse.class, token);
    }

    public DonHangResponse getById(Integer id, String token) {
        return apiClient.get("DonHang/" + id, DonHangResponse.class, token);
    }

    public MessageResponse approve(Integer id, String token) {
        return apiClient.put("DonHang/approve/" + id, MessageResponse.class, token);
    }

    public MessageResponse reject(Integer id, String reason, String token) {
        Map<String, Object> payload = Map.of("lyDoTuChoi", reason);
        return apiClient.put("DonHang/reject/" + id, payload, MessageResponse.class, token);
    }
}

