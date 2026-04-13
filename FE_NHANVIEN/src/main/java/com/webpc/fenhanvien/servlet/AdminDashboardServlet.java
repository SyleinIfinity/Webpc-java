package com.webpc.fenhanvien.servlet;

import com.webpc.fenhanvien.common.ApiException;
import com.webpc.fenhanvien.common.BaseServlet;
import com.webpc.fenhanvien.model.admin.KhachHangResponse;
import com.webpc.fenhanvien.model.admin.NhanVienResponse;
import com.webpc.fenhanvien.model.admin.PhieuNhapResponse;
import com.webpc.fenhanvien.model.admin.ProductResponse;
import com.webpc.fenhanvien.service.CatalogAdminService;
import com.webpc.fenhanvien.service.InventoryAdminService;
import com.webpc.fenhanvien.service.UserAdminService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@WebServlet("/Admin/Dashboard")
public class AdminDashboardServlet extends BaseServlet {

    private final UserAdminService userAdminService = new UserAdminService();
    private final CatalogAdminService catalogAdminService = new CatalogAdminService();
    private final InventoryAdminService inventoryAdminService = new InventoryAdminService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<NhanVienResponse> nhanViens = userAdminService.getNhanViens(currentToken(request));
            List<KhachHangResponse> khachHangs = userAdminService.getKhachHangs(currentToken(request));
            List<ProductResponse> sanPhams = catalogAdminService.getProducts(currentToken(request));
            List<PhieuNhapResponse> phieuNhaps = inventoryAdminService.getPhieuNhaps(currentToken(request));

            long tonKhoThap = sanPhams.stream().filter(item -> item.getSoLuongTon() != null && item.getSoLuongTon() < 5).count();

            request.setAttribute("pageTitle", "Tổng quan hệ thống");
            request.setAttribute("menuArea", "admin");
            request.setAttribute("activeMenu", "dashboard");
            request.setAttribute("statsNhanVien", nhanViens.size());
            request.setAttribute("statsKhachHang", khachHangs.size());
            request.setAttribute("statsSanPham", sanPhams.size());
            request.setAttribute("statsTonKhoThap", tonKhoThap);
            request.setAttribute(
                "latestPhieuNhaps",
                phieuNhaps.stream()
                    .sorted(Comparator.comparing(PhieuNhapResponse::getNgayNhap, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                    .limit(5)
                    .toList()
            );
            render(request, response, "admin/dashboard.jsp");
        } catch (ApiException ex) {
            request.setAttribute("pageTitle", "Tổng quan hệ thống");
            request.setAttribute("menuArea", "admin");
            request.setAttribute("activeMenu", "dashboard");
            request.setAttribute("pageError", ex.getMessage());
            render(request, response, "admin/dashboard.jsp");
        }
    }
}
