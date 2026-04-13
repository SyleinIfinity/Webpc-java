package com.webpc.fenhanvien.service;

import com.webpc.fenhanvien.common.ApiClient;
import com.webpc.fenhanvien.model.admin.CategoryResponse;
import com.webpc.fenhanvien.model.admin.ProductResponse;
import com.webpc.fenhanvien.model.admin.ThongSoKyThuatResponse;
import java.util.List;
import java.util.Map;

public class CatalogAdminService {

    private final ApiClient apiClient = new ApiClient();

    public List<ProductResponse> getProducts(String token) {
        return apiClient.getList("SanPham", ProductResponse.class, token);
    }

    public List<CategoryResponse> getCategories(String token) {
        return apiClient.getList("DanhMuc", CategoryResponse.class, token);
    }

    public List<ThongSoKyThuatResponse> getSpecifications(Integer productId, String token) {
        return apiClient.getList("ThongSoKyThuat/sanpham/" + productId, ThongSoKyThuatResponse.class, token);
    }

    public void createProduct(Map<String, List<String>> fields, List<ApiClient.MultipartFilePart> files, String token) {
        apiClient.postMultipart("SanPham", fields, files, token);
    }

    public void createCategory(Map<String, Object> payload, String token) {
        apiClient.post("DanhMuc", payload, CategoryResponse.class, token);
    }

    public void updateProduct(Integer productId, Map<String, List<String>> fields, List<ApiClient.MultipartFilePart> files, String token) {
        apiClient.patchMultipart("SanPham/" + productId, fields, files, token);
    }

    public void deleteProduct(Integer productId, String token) {
        apiClient.deleteRaw("SanPham/" + productId, token);
    }
}
