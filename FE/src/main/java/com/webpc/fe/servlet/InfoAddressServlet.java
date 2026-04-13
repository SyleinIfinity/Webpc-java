package com.webpc.fe.servlet;

import com.webpc.fe.common.ApiException;
import com.webpc.fe.common.BaseServlet;
import com.webpc.fe.model.address.AddressFormData;
import com.webpc.fe.model.address.AddressViewModel;
import com.webpc.fe.service.AddressService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {
    "/InfoAddress",
    "/InfoAddress/Index",
    "/InfoAddress/GetDetail",
    "/InfoAddress/SaveAddress",
    "/InfoAddress/Delete",
    "/InfoAddress/GetProvinces",
    "/InfoAddress/GetDistricts",
    "/InfoAddress/GetWards"
})
public class InfoAddressServlet extends BaseServlet {

    private final AddressService addressService = new AddressService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        switch (path) {
            case "/InfoAddress/GetDetail" -> handleGetDetail(request, response);
            case "/InfoAddress/GetProvinces" -> writeJson(response, addressService.getProvinces());
            case "/InfoAddress/GetDistricts" -> writeJson(response, addressService.getDistricts(request.getParameter("provinceId")));
            case "/InfoAddress/GetWards" -> writeJson(response, addressService.getWards(request.getParameter("districtId")));
            default -> showIndex(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String path = request.getServletPath();
        switch (path) {
            case "/InfoAddress/SaveAddress" -> handleSaveAddress(request, response);
            case "/InfoAddress/Delete" -> handleDelete(request, response);
            default -> response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void showIndex(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!requireLogin(request, response)) {
            return;
        }

        List<AddressViewModel> addresses = addressService.getAddressesByCustomer(currentUser(request).getMaKhachHang(), currentToken(request));
        request.setAttribute("pageTitle", "Sổ địa chỉ");
        request.setAttribute("activeNav", "info-address");
        request.setAttribute("addresses", addresses);
        render(request, response, "info/address.jsp");
    }

    private void handleGetDetail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!requireLogin(request, response)) {
            return;
        }

        Integer id = parseNullableInt(request.getParameter("id"));
        if (id == null) {
            writeJson(response, Map.of("success", false, "message", "Thiếu mã sổ địa chỉ."));
            return;
        }

        AddressViewModel address = addressService.getAddress(id, currentToken(request));
        AddressFormData formData = new AddressFormData();
        formData.setMaSoDiaChi(address.getMaSoDiaChi());
        formData.setMaKhachHang(address.getMaKhachHang());
        formData.setTenNguoiNhan(address.getTenNguoiNhan());
        formData.setSoDienThoai(address.getSoDienThoai());
        formData.setMaTinh(address.getTinhThanhId());
        formData.setMaHuyen(address.getQuanHuyenId());
        formData.setMaXa(address.getPhuongXaId());
        formData.setTinhThanh(address.getTenTinhThanh());
        formData.setQuanHuyen(address.getTenQuanHuyen());
        formData.setPhuongXa(address.getTenPhuongXa());
        formData.setDiaChiCuThe(address.getDiaChiCuThe());
        formData.setDefault(address.isMacDinh());

        writeJson(response, Map.of("success", true, "data", formData));
    }

    private void handleSaveAddress(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!requireLogin(request, response)) {
            return;
        }

        Integer maSoDiaChi = parseNullableInt(request.getParameter("MaSoDiaChi"));
        String tenNguoiNhan = trim(request.getParameter("TenNguoiNhan"));
        String soDienThoai = trim(request.getParameter("SoDienThoai"));
        String maTinh = trim(request.getParameter("MaTinh"));
        String maHuyen = trim(request.getParameter("MaHuyen"));
        String maXa = trim(request.getParameter("MaXa"));
        String diaChiCuThe = trim(request.getParameter("DiaChiCuThe"));
        boolean isDefault = "true".equalsIgnoreCase(request.getParameter("IsDefault")) || "on".equalsIgnoreCase(request.getParameter("IsDefault"));

        if (tenNguoiNhan.isBlank() || soDienThoai.isBlank() || maTinh.isBlank() || maHuyen.isBlank() || maXa.isBlank() || diaChiCuThe.isBlank()) {
            writeJson(response, Map.of("success", false, "message", "Vui lòng nhập đầy đủ thông tin địa chỉ."));
            return;
        }

        Map<String, Object> payload = new LinkedHashMap<>();
        if (maSoDiaChi == null || maSoDiaChi <= 0) {
            payload.put("maKhachHang", currentUser(request).getMaKhachHang());
        }
        payload.put("tenNguoiNhan", tenNguoiNhan);
        payload.put("soDienThoai", soDienThoai);
        payload.put("diaChiCuThe", diaChiCuThe);
        payload.put("tinhThanhId", maTinh);
        payload.put("quanHuyenId", maHuyen);
        payload.put("phuongXaId", maXa);
        payload.put("macDinh", isDefault);

        try {
            if (maSoDiaChi == null || maSoDiaChi <= 0) {
                addressService.createAddress(payload, currentToken(request));
            } else {
                addressService.updateAddress(maSoDiaChi, payload, currentToken(request));
            }
            writeJson(response, Map.of("success", true));
        } catch (ApiException ex) {
            writeJson(response, Map.of("success", false, "message", ex.getMessage()));
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!requireLogin(request, response)) {
            return;
        }

        Integer id = parseNullableInt(request.getParameter("id"));
        if (id == null || id <= 0) {
            writeJson(response, Map.of("success", false));
            return;
        }

        try {
            addressService.deleteAddress(id, currentToken(request));
            writeJson(response, Map.of("success", true));
        } catch (ApiException ex) {
            writeJson(response, Map.of("success", false, "message", ex.getMessage()));
        }
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
