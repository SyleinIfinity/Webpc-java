package com.webpc.fe.servlet;

import com.webpc.fe.common.BaseServlet;
import com.webpc.fe.config.AppConfig;
import com.webpc.fe.model.catalog.ProductViewModel;
import com.webpc.fe.service.CatalogService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@WebServlet(urlPatterns = {"/BuildPC", "/BuildPC/Index", "/BuildPC/GetProductsByCategory"})
public class BuildPCServlet extends BaseServlet {

    private static final List<String> CATEGORIES = List.of(
        "Vi xử lý (CPU)",
        "Bo mạch chủ (Mainboard)",
        "RAM",
        "Ổ cứng SSD",
        "Ổ cứng HDD",
        "Card màn hình (VGA)",
        "Nguồn (PSU)",
        "Vỏ Case",
        "Tản nhiệt"
    );

    private final CatalogService catalogService = new CatalogService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/BuildPC/GetProductsByCategory".equals(path)) {
            handleCategoryProducts(response, request.getParameter("category"));
            return;
        }

        request.setAttribute("pageTitle", "Xây dựng cấu hình PC");
        request.setAttribute("activeNav", "buildpc");
        request.setAttribute("categories", CATEGORIES);
        request.setAttribute("cartEnabled", AppConfig.getInstance().isCartEnabled());
        render(request, response, "buildpc/index.jsp");
    }

    private void handleCategoryProducts(HttpServletResponse response, String category) throws IOException {
        List<Map<String, Object>> result = catalogService.getProducts().stream()
            .filter(item -> matchCategory(item, category))
            .limit(50)
            .map(item -> Map.<String, Object>of(
                "Id", item.getMaSanPham(),
                "Name", item.getTenSanPham(),
                "Price", item.getGiaHienThi() == null ? BigDecimal.ZERO : item.getGiaHienThi(),
                "OldPrice", item.getCoKhuyenMai() && item.getGiaBan() != null ? item.getGiaBan() : BigDecimal.ZERO,
                "Image", item.getHinhAnhDaiDien()
            ))
            .toList();

        writeJson(response, result);
    }

    private boolean matchCategory(ProductViewModel product, String category) {
        if (category == null || category.isBlank()) {
            return true;
        }
        String keyword = getKeywordFromCategory(category).toLowerCase(Locale.ROOT);
        return contains(product.getTenSanPham(), keyword) || contains(product.getTenDanhMuc(), keyword);
    }

    private boolean contains(String value, String keyword) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(keyword);
    }

    private String getKeywordFromCategory(String category) {
        if (category == null) {
            return "";
        }
        if (category.contains("CPU")) return "cpu";
        if (category.contains("Mainboard")) return "mainboard";
        if (category.contains("RAM")) return "ram";
        if (category.contains("SSD")) return "ssd";
        if (category.contains("HDD")) return "hdd";
        if (category.contains("VGA")) return "vga";
        if (category.contains("Nguon")) return "nguon";
        if (category.contains("Case")) return "case";
        if (category.contains("Tan nhiet")) return "tan nhiet";
        return category;
    }
}
