package com.webpc.fenhanvien.servlet;

import com.webpc.fenhanvien.common.ApiClient;
import com.webpc.fenhanvien.common.ApiException;
import com.webpc.fenhanvien.common.BaseServlet;
import com.webpc.fenhanvien.service.CatalogAdminService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@WebServlet(urlPatterns = {
    "/Admin/SanPham",
    "/Admin/SanPham/Print",
    "/Admin/SanPham/Export",
    "/Admin/SanPham/Create",
    "/Admin/SanPham/CreateCategory",
    "/Admin/SanPham/Update",
    "/Admin/SanPham/Delete"
})
@MultipartConfig
public class AdminSanPhamServlet extends BaseServlet {

    private final CatalogAdminService catalogAdminService = new CatalogAdminService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/Admin/SanPham/Print".equals(path)) {
            // Legacy route: keep it but render print UI as an in-page overlay.
            redirect(request, response, "/Admin/SanPham?print=1");
            return;
        }
        if ("/Admin/SanPham/Export".equals(path)) {
            export(request, response);
            return;
        }
        showList(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        if ("/Admin/SanPham/Create".equals(path)) {
            createProduct(request, response);
            return;
        }
        if ("/Admin/SanPham/CreateCategory".equals(path)) {
            createCategory(request, response);
            return;
        }
        if ("/Admin/SanPham/Update".equals(path)) {
            updateProduct(request, response);
            return;
        }
        if ("/Admin/SanPham/Delete".equals(path)) {
            deleteProduct(request, response);
            return;
        }
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private void showList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("pageTitle", "Quản lý sản phẩm");
        request.setAttribute("menuArea", "admin");
        request.setAttribute("activeMenu", "sanpham");
        request.setAttribute("printDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        try {
            request.setAttribute("items", catalogAdminService.getProducts(currentToken(request)));
            request.setAttribute("categories", catalogAdminService.getCategories(currentToken(request)));
        } catch (ApiException ex) {
            request.setAttribute("pageError", ex.getMessage());
        }
        render(request, response, "admin/sanpham.jsp");
    }

    private void createProduct(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String tenSanPham = trim(request.getParameter("tenSanPham"));
        String maDanhMuc = trim(request.getParameter("maDanhMuc"));
        if (tenSanPham.isBlank() || maDanhMuc.isBlank()) {
            flashError(request, "Vui lòng nhập đầy đủ tên sản phẩm và danh mục.");
            redirect(request, response, "/Admin/SanPham");
            return;
        }

        Map<String, List<String>> fields = buildCommonFields(request, true);
        addField(fields, "mainImageIndex", trim(request.getParameter("mainImageIndex")));
        List<ApiClient.MultipartFilePart> files = extractFiles(request);
        try {
            catalogAdminService.createProduct(fields, files, currentToken(request));
            flashSuccess(request, "Đã thêm sản phẩm mới.");
        } catch (ApiException ex) {
            flashError(request, ex.getMessage());
        }
        redirect(request, response, "/Admin/SanPham");
    }

    private void updateProduct(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String idValue = trim(request.getParameter("maSanPham"));
        if (idValue.isBlank()) {
            flashError(request, "Không tìm thấy mã sản phẩm để cập nhật.");
            redirect(request, response, "/Admin/SanPham");
            return;
        }

        Integer productId;
        try {
            productId = Integer.parseInt(idValue);
        } catch (NumberFormatException ex) {
            flashError(request, "Mã sản phẩm không hợp lệ.");
            redirect(request, response, "/Admin/SanPham");
            return;
        }

        Map<String, List<String>> fields = buildCommonFields(request, false);
        String[] publicIds = request.getParameterValues("publicIdsToDelete");
        if (publicIds != null) {
            for (String publicId : publicIds) {
                addField(fields, "publicIdsToDelete", trim(publicId));
            }
        }
        addField(fields, "mainImageIndex", trim(request.getParameter("mainImageIndex")));
        addField(fields, "mainImageId", trim(request.getParameter("mainImageId")));

        List<ApiClient.MultipartFilePart> files = extractFiles(request);
        try {
            catalogAdminService.updateProduct(productId, fields, files, currentToken(request));
            flashSuccess(request, "Cập nhật sản phẩm thành công.");
        } catch (ApiException ex) {
            flashError(request, ex.getMessage());
        }
        redirect(request, response, "/Admin/SanPham");
    }

    private void createCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String tenDanhMuc = trim(request.getParameter("tenDanhMuc"));
        if (tenDanhMuc.isBlank()) {
            flashError(request, "Vui lòng nhập tên danh mục.");
            redirect(request, response, "/Admin/SanPham");
            return;
        }

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("tenDanhMuc", tenDanhMuc);
        String moTa = trim(request.getParameter("moTaDanhMuc"));
        if (!moTa.isBlank()) {
            payload.put("moTa", moTa);
        }
        String maDanhMucCha = trim(request.getParameter("maDanhMucCha"));
        if (!maDanhMucCha.isBlank()) {
            try {
                payload.put("maDanhMucCha", Integer.parseInt(maDanhMucCha));
            } catch (NumberFormatException ex) {
                flashError(request, "Mã danh mục cha không hợp lệ.");
                redirect(request, response, "/Admin/SanPham");
                return;
            }
        }

        try {
            catalogAdminService.createCategory(payload, currentToken(request));
            flashSuccess(request, "Đã thêm danh mục mới.");
        } catch (ApiException ex) {
            flashError(request, ex.getMessage());
        }
        redirect(request, response, "/Admin/SanPham");
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idValue = trim(request.getParameter("maSanPham"));
        if (idValue.isBlank()) {
            flashError(request, "Không tìm thấy mã sản phẩm để xóa.");
            redirect(request, response, "/Admin/SanPham");
            return;
        }
        try {
            Integer productId = Integer.parseInt(idValue);
            catalogAdminService.deleteProduct(productId, currentToken(request));
            flashSuccess(request, "Đã xóa sản phẩm.");
        } catch (NumberFormatException ex) {
            flashError(request, "Mã sản phẩm không hợp lệ.");
        } catch (ApiException ex) {
            flashError(request, ex.getMessage());
        }
        redirect(request, response, "/Admin/SanPham");
    }

    private Map<String, List<String>> buildCommonFields(HttpServletRequest request, boolean isCreate) {
        Map<String, List<String>> fields = new LinkedHashMap<>();
        addField(fields, "tenSanPham", trim(request.getParameter("tenSanPham")));
        addField(fields, "maDanhMuc", trim(request.getParameter("maDanhMuc")));
        addField(fields, "giaBan", trim(request.getParameter("giaBan")));
        addField(fields, "giaKhuyenMai", trim(request.getParameter("giaKhuyenMai")));
        addField(fields, "soLuongTon", trim(request.getParameter("soLuongTon")));
        addField(fields, "moTa", trim(request.getParameter("moTa")));
        String trangThai = trim(request.getParameter("trangThai"));
        if (!trangThai.isBlank()) {
            addField(fields, "trangThai", trangThai);
        } else if (isCreate) {
            addField(fields, "trangThai", "true");
        }
        return fields;
    }

    private void addField(Map<String, List<String>> fields, String key, String value) {
        if (value == null || value.isBlank()) {
            return;
        }
        fields.computeIfAbsent(key, ignored -> new ArrayList<>()).add(value);
    }

    private List<ApiClient.MultipartFilePart> extractFiles(HttpServletRequest request) throws IOException, ServletException {
        List<ApiClient.MultipartFilePart> files = new ArrayList<>();
        for (Part part : request.getParts()) {
            if (!"hinhAnhs".equals(part.getName()) || part.getSize() == 0) {
                continue;
            }
            String fileName = part.getSubmittedFileName();
            byte[] content = part.getInputStream().readAllBytes();
            files.add(new ApiClient.MultipartFilePart(
                "hinhAnhs",
                fileName == null ? "upload.bin" : fileName,
                part.getContentType(),
                content
            ));
        }
        return files;
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }

    private void export(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String format = request.getParameter("format");
        if (format == null || format.isBlank()) {
            format = "xlsx";
        }

        if (!"xlsx".equalsIgnoreCase(format)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write("Chi ho tro format=xlsx");
            return;
        }

        List<com.webpc.fenhanvien.model.admin.ProductResponse> items;
        try {
            items = catalogAdminService.getProducts(currentToken(request));
        } catch (ApiException ex) {
            // Keep behavior consistent with other pages: show an HTML error.
            throw new ServletException(ex.getMessage(), ex);
        }

        exportXlsx(items, response);
    }

    private void exportXlsx(List<com.webpc.fenhanvien.model.admin.ProductResponse> items, HttpServletResponse response) throws IOException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "sanpham_" + timestamp + ".xlsx";

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("SanPham");

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setWrapText(true);

            CellStyle moneyStyle = workbook.createCellStyle();
            moneyStyle.cloneStyleFrom(cellStyle);
            moneyStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("#,##0"));

            Row header = sheet.createRow(0);
            String[] cols = new String[] {
                "Ma SP",
                "San pham",
                "Danh muc",
                "Gia ban",
                "Gia khuyen mai",
                "Ton kho",
                "Trang thai",
                "Mo ta"
            };
            for (int i = 0; i < cols.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(cols[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            if (items != null) {
                for (var item : items) {
                    Row row = sheet.createRow(rowIdx++);
                    int col = 0;

                    createCell(row, col++, safeInt(item.getMaSanPham()), cellStyle);
                    createCell(row, col++, safeString(item.getTenSanPham()), cellStyle);
                    createCell(row, col++, safeString(item.getTenDanhMuc()), cellStyle);
                    createCell(row, col++, safeBigDecimal(item.getGiaBan()), moneyStyle);
                    createCell(row, col++, safeBigDecimal(item.getGiaKhuyenMai()), moneyStyle);
                    createCell(row, col++, safeInt(item.getSoLuongTon()), cellStyle);
                    createCell(row, col++, item.isTrangThai() ? "Dang ban" : "Tam an", cellStyle);
                    createCell(row, col, safeString(item.getMoTa()), cellStyle);
                }
            }

            // Set reasonable column widths (autosize is slow with large sheets)
            sheet.setColumnWidth(0, 10 * 256);
            sheet.setColumnWidth(1, 32 * 256);
            sheet.setColumnWidth(2, 22 * 256);
            sheet.setColumnWidth(3, 14 * 256);
            sheet.setColumnWidth(4, 16 * 256);
            sheet.setColumnWidth(5, 10 * 256);
            sheet.setColumnWidth(6, 12 * 256);
            sheet.setColumnWidth(7, 45 * 256);

            workbook.write(response.getOutputStream());
        }
    }

    private void createCell(Row row, int colIndex, String value, CellStyle style) {
        Cell cell = row.createCell(colIndex);
        cell.setCellValue(value == null ? "" : value);
        if (style != null) {
            cell.setCellStyle(style);
        }
    }

    private void createCell(Row row, int colIndex, Integer value, CellStyle style) {
        Cell cell = row.createCell(colIndex);
        if (value != null) {
            cell.setCellValue(value);
        } else {
            cell.setCellValue("");
        }
        if (style != null) {
            cell.setCellStyle(style);
        }
    }

    private void createCell(Row row, int colIndex, java.math.BigDecimal value, CellStyle style) {
        Cell cell = row.createCell(colIndex);
        if (value != null) {
            cell.setCellValue(value.doubleValue());
        } else {
            cell.setCellValue("");
        }
        if (style != null) {
            cell.setCellStyle(style);
        }
    }

    private Integer safeInt(Integer value) {
        return value;
    }

    private java.math.BigDecimal safeBigDecimal(java.math.BigDecimal value) {
        return value;
    }

    private String safeString(String value) {
        return value == null ? "" : value;
    }
}
