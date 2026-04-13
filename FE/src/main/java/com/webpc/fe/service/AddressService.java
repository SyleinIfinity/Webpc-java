package com.webpc.fe.service;

import com.webpc.fe.common.ApiClient;
import com.webpc.fe.model.address.AddressViewModel;
import com.webpc.fe.model.address.LocationItem;
import java.util.List;
import java.util.Map;

public class AddressService {

    private final ApiClient apiClient = new ApiClient();

    public List<AddressViewModel> getAddressesByCustomer(Integer maKhachHang, String token) {
        return apiClient.getList("SoDiaChi/khachhang/" + maKhachHang, AddressViewModel.class, token);
    }

    public AddressViewModel getAddress(Integer maSoDiaChi, String token) {
        return apiClient.get("SoDiaChi/" + maSoDiaChi, AddressViewModel.class, token);
    }

    public void createAddress(Map<String, Object> payload, String token) {
        apiClient.postNoResponse("SoDiaChi", payload, token);
    }

    public void updateAddress(Integer maSoDiaChi, Map<String, Object> payload, String token) {
        apiClient.patchNoResponse("SoDiaChi/" + maSoDiaChi, payload, token);
    }

    public void deleteAddress(Integer maSoDiaChi, String token) {
        apiClient.delete("SoDiaChi/" + maSoDiaChi, token);
    }

    public List<LocationItem> getProvinces() {
        return apiClient.getList("SoDiaChi/provinces", LocationItem.class, null);
    }

    public List<LocationItem> getDistricts(String provinceId) {
        return apiClient.getList("SoDiaChi/districts/" + provinceId, LocationItem.class, null);
    }

    public List<LocationItem> getWards(String districtId) {
        return apiClient.getList("SoDiaChi/wards/" + districtId, LocationItem.class, null);
    }
}
