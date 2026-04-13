package com.webpc.fenhanvien.service;

import com.webpc.fenhanvien.common.ApiClient;
import com.webpc.fenhanvien.model.admin.KhachHangResponse;
import com.webpc.fenhanvien.model.admin.NhanVienResponse;
import com.webpc.fenhanvien.model.admin.VaiTroResponse;
import java.util.List;

public class UserAdminService {

    private final ApiClient apiClient = new ApiClient();

    public List<NhanVienResponse> getNhanViens(String token) {
        return apiClient.getList("NhanVien", NhanVienResponse.class, token);
    }

    public List<KhachHangResponse> getKhachHangs(String token) {
        return apiClient.getList("KhachHang", KhachHangResponse.class, token);
    }

    public List<VaiTroResponse> getVaiTros(String token) {
        return apiClient.getList("VaiTro", VaiTroResponse.class, token);
    }
}
