package com.webpc.fe.servlet;

import com.webpc.fe.common.ApiException;
import com.webpc.fe.common.BaseServlet;
import com.webpc.fe.config.AppConfig;
import com.webpc.fe.model.catalog.CategoryViewModel;
import com.webpc.fe.model.catalog.ProductViewModel;
import com.webpc.fe.model.catalog.ThongSoKyThuatViewModel;
import com.webpc.fe.service.CatalogService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@WebServlet(urlPatterns = {"/Product", "/Product/Index", "/Product/Detail"})
public class ProductServlet extends BaseServlet {

    private final CatalogService catalogService = new CatalogService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/Product/Detail".equals(path)) {
            showDetail(request, response);
            return;
        }
        showIndex(request, response);
    }

    private void showIndex(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String search = valueOrEmpty(request.getParameter("search"));
        Integer categoryId = parseNullableInt(request.getParameter("categoryId"));

        List<CategoryViewModel> categories = catalogService.getCategories();
        List<ProductViewModel> products = categoryId != null
            ? catalogService.getProductsByCategory(categoryId)
            : catalogService.getProducts();

        if (!search.isBlank()) {
            String keyword = search.toLowerCase(Locale.ROOT);
            products = new ArrayList<>(products.stream()
                .filter(item -> item.getTenSanPham() != null && item.getTenSanPham().toLowerCase(Locale.ROOT).contains(keyword))
                .toList());
        }

        request.setAttribute("pageTitle", "Sản phẩm");
        request.setAttribute("activeNav", "product");
        request.setAttribute("categories", categories);
        request.setAttribute("products", products);
        request.setAttribute("currentSearch", search);
        request.setAttribute("currentCategory", categoryId);
        request.setAttribute("cartEnabled", AppConfig.getInstance().isCartEnabled());
        render(request, response, "product/index.jsp");
    }

    private void showDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer id = parseNullableInt(request.getParameter("id"));
        if (id == null || id <= 0) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            ProductViewModel product = catalogService.getProduct(id);
            List<ThongSoKyThuatViewModel> specs = catalogService.getSpecifications(id);
            product.setThongSoKyThuat(specs);

            request.setAttribute("pageTitle", product.getTenSanPham());
            request.setAttribute("activeNav", "product");
            request.setAttribute("product", product);
            request.setAttribute("cartEnabled", AppConfig.getInstance().isCartEnabled());
            render(request, response, "product/detail.jsp");
        } catch (ApiException ex) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, ex.getMessage());
        }
    }

    private String valueOrEmpty(String value) {
        return value == null ? "" : value.trim();
    }
}
