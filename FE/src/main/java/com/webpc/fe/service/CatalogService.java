package com.webpc.fe.service;

import com.webpc.fe.common.ApiClient;
import com.webpc.fe.model.catalog.CategoryViewModel;
import com.webpc.fe.model.catalog.ProductViewModel;
import com.webpc.fe.model.catalog.ThongSoKyThuatViewModel;
import java.util.List;

public class CatalogService {

    private final ApiClient apiClient = new ApiClient();

    public List<CategoryViewModel> getCategories() {
        return apiClient.getList("DanhMuc", CategoryViewModel.class, null);
    }

    public List<ProductViewModel> getProducts() {
        return apiClient.getList("SanPham", ProductViewModel.class, null);
    }

    public List<ProductViewModel> getProductsByCategory(Integer maDanhMuc) {
        return apiClient.getList("SanPham/danhmuc/" + maDanhMuc, ProductViewModel.class, null);
    }

    public ProductViewModel getProduct(Integer id) {
        return apiClient.get("SanPham/" + id, ProductViewModel.class, null);
    }

    public List<ThongSoKyThuatViewModel> getSpecifications(Integer maSanPham) {
        return apiClient.getList("ThongSoKyThuat/sanpham/" + maSanPham, ThongSoKyThuatViewModel.class, null);
    }
}
