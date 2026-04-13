<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/head-start.jspf" %>
<%@ include file="/WEB-INF/views/common/layout-start.jspf" %>
<div class="section-card">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <div>
            <h4 class="mb-0">Danh sách sản phẩm</h4>
            <div class="text-muted small">Màn hình đọc dữ liệu từ `api/SanPham` + `api/DanhMuc`.</div>
        </div>
        <div class="d-flex gap-2 align-items-center">
            <span class="text-muted small">${fn:length(categories)} danh mục</span>
            <button type="button" class="btn btn-sm btn-primary" data-bs-toggle="modal" data-bs-target="#productCreateModal">
                <i class="fas fa-plus me-1"></i>Thêm sản phẩm
            </button>
            <div class="dropdown">
                <button class="btn btn-sm btn-outline-primary dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                    Xuất file
                </button>
                <ul class="dropdown-menu dropdown-menu-end">
                    <li>
                        <button type="button" class="dropdown-item" data-bs-toggle="modal" data-bs-target="#productPrintModal">
                            In / PDF (xem trước)
                        </button>
                    </li>
                    <li>
                        <button type="button" class="dropdown-item" data-bs-toggle="modal" data-bs-target="#productExcelModal">
                            Excel (xem trước)
                        </button>
                    </li>
                    <li>
                        <a class="dropdown-item" href="${ctx}/Admin/SanPham/Export?format=xlsx">Tải Excel (.xlsx)</a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <div class="d-flex flex-wrap gap-2 align-items-center mb-3">
        <ul class="nav nav-pills flex-wrap category-tabs" id="categoryTabs">
            <li class="nav-item">
                <button type="button" class="nav-link active" data-category="all">Tất cả</button>
            </li>
            <c:forEach var="cat" items="${categories}">
                <li class="nav-item">
                    <button type="button" class="nav-link" data-category="${cat.maDanhMuc}">
                        <c:out value="${cat.tenDanhMuc}" />
                    </button>
                </li>
            </c:forEach>
        </ul>
    </div>
    <div class="d-flex flex-wrap gap-2 align-items-end mb-3 product-filter-bar">
        <div>
            <label class="form-label small">Lọc danh mục</label>
            <select class="form-select form-select-sm" id="categoryFilter">
                <option value="all">Tất cả</option>
                <c:forEach var="cat" items="${categories}">
                    <option value="${cat.maDanhMuc}"><c:out value="${cat.tenDanhMuc}" /></option>
                </c:forEach>
            </select>
        </div>
        <div class="flex-grow-1">
            <label class="form-label small">Tìm kiếm theo tên</label>
            <input class="form-control form-control-sm" id="productSearch" placeholder="Nhập tên sản phẩm" />
        </div>
        <button type="button" class="btn btn-sm btn-outline-secondary" id="btnClearFilters">Xóa lọc</button>
        <div class="ms-auto text-muted small" id="filterSummary"></div>
    </div>
    <table class="table table-striped table-hover" id="productTable">
        <thead>
            <tr><th>Mã SP</th><th>Sản phẩm</th><th>Danh mục</th><th>Giá bán</th><th>Khuyến mãi</th><th>Tồn kho</th><th>Trạng thái</th><th class="text-end">Thao tác</th></tr>
        </thead>
        <tbody>
            <c:forEach var="item" items="${items}">
                <c:set var="matchedCategoryId" value="" />
                <c:forEach var="cat" items="${categories}">
                    <c:if test="${cat.tenDanhMuc == item.tenDanhMuc}">
                        <c:set var="matchedCategoryId" value="${cat.maDanhMuc}" />
                    </c:if>
                </c:forEach>
                <tr data-id="${item.maSanPham}"
                    data-name="${fn:escapeXml(item.tenSanPham)}"
                    data-gia="${item.giaBan}"
                    data-khuyenmai="${item.giaKhuyenMai}"
                    data-ton="${item.soLuongTon}"
                    data-trangthai="${item.trangThai}"
                    data-danhmuc-id="${matchedCategoryId}"
                    data-mota="${fn:escapeXml(item.moTa)}">
                    <td>${item.maSanPham}</td>
                    <td>
                        <c:set var="mainImageUrl" value="" />
                        <c:forEach var="img" items="${item.danhSachAnh}">
                            <c:if test="${img.laAnhDaiDien}">
                                <c:set var="mainImageUrl" value="${img.url}" />
                            </c:if>
                        </c:forEach>
                        <c:if test="${empty mainImageUrl && not empty item.danhSachAnh}">
                            <c:set var="mainImageUrl" value="${item.danhSachAnh[0].url}" />
                        </c:if>
                        <div class="d-flex align-items-center gap-2">
                            <c:if test="${not empty mainImageUrl}">
                                <img class="product-thumb" src="${mainImageUrl}" alt="" />
                            </c:if>
                            <div>
                                <div class="fw-bold"><c:out value="${item.tenSanPham}" /></div>
                                <div class="text-muted small"><c:out value="${item.moTa}" /></div>
                            </div>
                        </div>
                    </td>
                    <td><c:out value="${item.tenDanhMuc}" /></td>
                    <td><fmt:formatNumber value="${item.giaBan}" pattern="#,##0" /> đ</td>
                    <td>
                        <c:choose>
                            <c:when test="${item.giaKhuyenMai != null}">
                                <fmt:formatNumber value="${item.giaKhuyenMai}" pattern="#,##0" /> đ
                            </c:when>
                            <c:otherwise><span class="text-muted">-</span></c:otherwise>
                        </c:choose>
                    </td>
                    <td>${item.soLuongTon}</td>
                    <td><span class="status-pill ${item.trangThai ? 'ok' : 'off'}">${item.trangThai ? 'Đang bán' : 'Tạm ẩn'}</span></td>
                    <td class="text-end">
                        <div class="d-inline-flex gap-1">
                            <button type="button" class="btn btn-sm btn-outline-primary js-edit-product">Sửa</button>
                            <button type="button" class="btn btn-sm btn-outline-danger js-delete-product">Xóa</button>
                        </div>
                        <div class="d-none js-images">
                            <c:forEach var="img" items="${item.danhSachAnh}">
                                <span class="js-image"
                                      data-id="${img.id}"
                                      data-url="${img.url}"
                                      data-public-id="${img.publicId}"
                                      data-main="${img.laAnhDaiDien}"></span>
                            </c:forEach>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty items}">
                <tr><td colspan="8" class="text-center text-muted">Không có dữ liệu sản phẩm.</td></tr>
            </c:if>
            <tr id="noMatchRow" class="d-none">
                <td colspan="8" class="text-center text-muted">Không có sản phẩm phù hợp.</td>
            </tr>
        </tbody>
    </table>
</div>

<div class="modal fade" id="productCreateModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <form class="product-form" action="${ctx}/Admin/SanPham/Create" method="post" enctype="multipart/form-data">
                <div class="modal-header">
                    <h5 class="modal-title">Thêm sản phẩm</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="row g-3">
                        <div class="col-md-6">
                            <label class="form-label">Tên sản phẩm</label>
                            <input class="form-control" name="tenSanPham" required />
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Danh mục</label>
                            <div class="input-group">
                                <select class="form-select" name="maDanhMuc" required>
                                    <option value="">Chọn danh mục</option>
                                    <c:forEach var="cat" items="${categories}">
                                        <option value="${cat.maDanhMuc}">${cat.tenDanhMuc}</option>
                                    </c:forEach>
                                </select>
                                <button class="btn btn-outline-secondary" type="button" data-bs-toggle="modal" data-bs-target="#categoryCreateModal">
                                    <i class="fas fa-plus"></i>
                                </button>
                            </div>
                            <div class="form-text">Bấm [+] để thêm nhanh danh mục mới nếu cần.</div>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label">Giá bán (VND)</label>
                            <div class="input-group">
                                <input class="form-control" type="number" step="0.01" name="giaBan" placeholder="Ví dụ: 1500000" />
                                <span class="input-group-text">VND</span>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label">Giá khuyến mãi (VND)</label>
                            <div class="input-group">
                                <input class="form-control" type="number" step="0.01" name="giaKhuyenMai" placeholder="Ví dụ: 1200000" />
                                <span class="input-group-text">VND</span>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label">Tồn kho</label>
                            <input class="form-control" type="number" name="soLuongTon" />
                        </div>
                        <div class="col-md-12">
                            <label class="form-label">Mô tả</label>
                            <textarea class="form-control" name="moTa" rows="3"></textarea>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Trạng thái</label>
                            <select class="form-select" name="trangThai">
                                <option value="true" selected>Đang bán</option>
                                <option value="false">Tạm ẩn</option>
                            </select>
                        </div>
                        <div class="col-md-12">
                            <label class="form-label">Ảnh sản phẩm</label>
                            <input class="form-control" type="file" id="createImageInput" name="hinhAnhs" multiple accept="image/*" />
                            <div class="d-flex justify-content-between align-items-center mt-2">
                                <div class="text-muted small">Hỗ trợ nhiều ảnh, có thể xóa ảnh đã chọn trước khi lưu.</div>
                                <button class="btn btn-sm btn-outline-danger" type="button" id="createImageClear">Xóa tất cả ảnh đã chọn</button>
                            </div>
                            <div id="createImagePreview" class="image-preview mt-2"></div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-primary">Lưu sản phẩm</button>
                </div>
            </form>
        </div>
    </div>
</div>

<div class="modal fade" id="categoryCreateModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form action="${ctx}/Admin/SanPham/CreateCategory" method="post">
                <div class="modal-header">
                    <h5 class="modal-title">Thêm danh mục</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="mb-3">
                        <label class="form-label">Tên danh mục</label>
                        <input class="form-control" name="tenDanhMuc" required />
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Danh mục cha (tuỳ chọn)</label>
                        <select class="form-select" name="maDanhMucCha">
                            <option value="">Không chọn</option>
                            <c:forEach var="cat" items="${categories}">
                                <option value="${cat.maDanhMuc}">${cat.tenDanhMuc}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Mô tả</label>
                        <textarea class="form-control" name="moTaDanhMuc" rows="3"></textarea>
                    </div>
                    <div class="text-muted small">Danh mục mới sẽ sẵn sàng sau khi tải lại trang.</div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Đóng</button>
                    <button type="submit" class="btn btn-primary">Lưu danh mục</button>
                </div>
            </form>
        </div>
    </div>
</div>

<div class="modal fade" id="productEditModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <form class="product-form" action="${ctx}/Admin/SanPham/Update" method="post" enctype="multipart/form-data">
                <div class="modal-header">
                    <h5 class="modal-title">Cập nhật sản phẩm</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="maSanPham" id="editMaSanPham" />
                    <div class="row g-3">
                        <div class="col-md-6">
                            <label class="form-label">Tên sản phẩm</label>
                            <input class="form-control" name="tenSanPham" id="editTenSanPham" />
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Danh mục</label>
                            <select class="form-select" name="maDanhMuc" id="editDanhMuc">
                                <option value="">Giữ nguyên</option>
                                <c:forEach var="cat" items="${categories}">
                                    <option value="${cat.maDanhMuc}">${cat.tenDanhMuc}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label">Giá bán (VND)</label>
                            <div class="input-group">
                                <input class="form-control" type="number" step="0.01" name="giaBan" id="editGiaBan" />
                                <span class="input-group-text">VND</span>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label">Giá khuyến mãi (VND)</label>
                            <div class="input-group">
                                <input class="form-control" type="number" step="0.01" name="giaKhuyenMai" id="editGiaKhuyenMai" />
                                <span class="input-group-text">VND</span>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label">Tồn kho</label>
                            <input class="form-control" type="number" name="soLuongTon" id="editSoLuongTon" />
                        </div>
                        <div class="col-md-12">
                            <label class="form-label">Mô tả</label>
                            <textarea class="form-control" name="moTa" rows="3" id="editMoTa"></textarea>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Trạng thái</label>
                            <select class="form-select" name="trangThai" id="editTrangThai">
                                <option value="true">Đang bán</option>
                                <option value="false">Tạm ẩn</option>
                            </select>
                        </div>
                        <div class="col-md-12">
                            <label class="form-label">Ảnh hiện tại (chọn để xóa)</label>
                            <div id="editImageList" class="image-list"></div>
                            <div id="editImageEmpty" class="text-muted small mt-2">Không có ảnh hiện tại.</div>
                        </div>
                        <div class="col-md-12">
                            <label class="form-label">Ảnh mới (nếu có)</label>
                            <input class="form-control" type="file" id="editImageInput" name="hinhAnhs" multiple accept="image/*" />
                            <div class="d-flex justify-content-between align-items-center mt-2">
                                <div class="text-muted small">Có thể xóa ảnh đã chọn trước khi lưu.</div>
                                <button class="btn btn-sm btn-outline-danger" type="button" id="editImageClear">Xóa tất cả ảnh đã chọn</button>
                            </div>
                            <div id="editImagePreview" class="image-preview mt-2"></div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Đóng</button>
                    <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
                </div>
            </form>
        </div>
    </div>
</div>

<div class="modal fade" id="productDeleteModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form action="${ctx}/Admin/SanPham/Delete" method="post">
                <div class="modal-header">
                    <h5 class="modal-title">Xóa sản phẩm</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="maSanPham" id="deleteMaSanPham" />
                    <p class="mb-0">Bạn chắc chắn muốn xóa sản phẩm <strong id="deleteTenSanPham">này</strong>?</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-danger">Xóa</button>
                </div>
            </form>
        </div>
    </div>
</div>

<div class="modal fade" id="productExcelModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-xl modal-dialog-scrollable">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Xem trước Excel: Danh sách sản phẩm</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <div class="text-muted small">Ngày in: ${printDate}</div>
                    <a class="btn btn-sm btn-success" href="${ctx}/Admin/SanPham/Export?format=xlsx">
                        <i class="fas fa-file-excel me-1"></i>Tải Excel
                    </a>
                </div>
                <div class="table-responsive">
                    <table class="table table-bordered excel-preview-table">
                        <thead>
                            <tr>
                                <th>Mã SP</th>
                                <th>Sản phẩm</th>
                                <th>Danh mục</th>
                                <th>Giá bán</th>
                                <th>Giá khuyến mãi</th>
                                <th>Tồn kho</th>
                                <th>Trạng thái</th>
                                <th>Mô tả</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${items}">
                                <tr>
                                    <td>${item.maSanPham}</td>
                                    <td><c:out value="${item.tenSanPham}" /></td>
                                    <td><c:out value="${item.tenDanhMuc}" /></td>
                                    <td><fmt:formatNumber value="${item.giaBan}" pattern="#,##0" /> đ</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${item.giaKhuyenMai != null}">
                                                <fmt:formatNumber value="${item.giaKhuyenMai}" pattern="#,##0" /> đ
                                            </c:when>
                                            <c:otherwise><span class="text-muted">-</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${item.soLuongTon}</td>
                                    <td>${item.trangThai ? 'Đang bán' : 'Tạm ẩn'}</td>
                                    <td><c:out value="${item.moTa}" /></td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty items}">
                                <tr><td colspan="8" class="text-center text-muted">Không có dữ liệu sản phẩm.</td></tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<style>
    .print-modal .modal-header {
        background: #0f172a;
        color: #fff;
        border-bottom: 1px solid rgba(255,255,255,.12);
    }

    .print-modal .modal-header .btn-close {
        filter: invert(1);
        opacity: .9;
    }

    .print-toolbar {
        background: #f8fafc;
        border: 1px solid #e2e8f0;
        border-radius: 16px;
        padding: 12px 14px;
    }

    .print-toolbar .form-label {
        font-size: .8rem;
        font-weight: 800;
        color: #334155;
        margin-bottom: .25rem;
    }

    .print-toolbar .form-select,
    .print-toolbar .form-control {
        font-size: .875rem;
        padding-top: .35rem;
        padding-bottom: .35rem;
    }

    .print-sheet {
        background: #fff;
        border-radius: 12px;
        padding: 20px;
        box-shadow: 0 10px 24px rgba(15, 23, 42, 0.08);
    }

    .print-sheet.frame-none { border: none; }
    .print-sheet.frame-thin { border: 1px solid #0f172a; border-radius: 0; }
    .print-sheet.frame-double { border: 3px double #0f172a; border-radius: 0; }

    .print-sheet.template-minimal .vn-only { display: none !important; }
    .print-sheet.template-vn .minimal-only { display: none !important; }

    .print-title {
        font-weight: 900;
        letter-spacing: .6px;
        text-transform: uppercase;
        color: #0f172a;
    }

    .print-header {
        border-bottom: 2px solid #0f172a;
        padding-bottom: 6px;
        margin-bottom: 12px;
    }

    .print-table thead th {
        background: #0f172a;
        color: #fff;
        font-size: 12px;
        text-transform: uppercase;
        letter-spacing: .4px;
        border: 1px solid #000 !important;
    }

    .print-table td,
    .print-table th {
        border: 1px solid #000 !important;
    }

    .print-table {
        border-collapse: collapse !important;
        border-spacing: 0 !important;
    }

    .print-table tbody tr:nth-child(even) {
        background: #f8fafc;
    }

    .editable[contenteditable="true"] {
        outline: 2px dashed rgba(37, 99, 235, .35);
        outline-offset: 4px;
        border-radius: 8px;
        padding: 2px 6px;
        cursor: text;
        user-select: text;
    }

    .editable[contenteditable="true"]:focus {
        outline: 2px solid rgba(37, 99, 235, .55);
        box-shadow: 0 0 0 4px rgba(37, 99, 235, .12);
    }

    .vn-header .line {
        width: 62%;
        height: 2px;
        background: #0f172a;
        margin: 8px auto 0;
    }

    .vn-sign .signature-space {
        height: 60px;
    }

    .density-compact .print-table { font-size: 11px; }
    .density-compact .print-table td { padding-top: .35rem; padding-bottom: .35rem; }
    .density-normal .print-table { font-size: 13px; }

    .col-hidden { display: none !important; }

    .product-form .form-label {
        font-weight: 700;
        color: #0f172a;
    }

    .category-tabs .nav-link {
        border-radius: 999px;
        padding: 0.35rem 0.9rem;
        font-size: 0.85rem;
        color: #1e293b;
        border: 1px solid #e2e8f0;
    }

    .category-tabs .nav-link.active {
        background: #1d4ed8;
        color: #fff;
        border-color: #1d4ed8;
        box-shadow: 0 6px 14px rgba(29, 78, 216, 0.2);
    }

    .product-filter-bar {
        padding: 10px 12px;
        border-radius: 12px;
        background: #f8fafc;
        border: 1px solid #e2e8f0;
    }

    .image-list {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
        gap: 10px;
    }

    .product-thumb {
        width: 48px;
        height: 48px;
        object-fit: cover;
        border-radius: 8px;
        border: 1px solid #e2e8f0;
        background: #fff;
    }

    .image-chip {
        border: 1px solid #e2e8f0;
        border-radius: 10px;
        padding: 8px;
        display: flex;
        gap: 8px;
        align-items: center;
        background: #f8fafc;
    }

    .image-chip .main-radio {
        margin-left: auto;
        display: flex;
        align-items: center;
        gap: 4px;
        font-size: 12px;
        color: #475569;
    }

    .image-chip img {
        width: 48px;
        height: 48px;
        object-fit: cover;
        border-radius: 8px;
        border: 1px solid #e2e8f0;
    }

    .image-preview {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(96px, 1fr));
        gap: 8px;
    }

    .image-preview .preview-item {
        border: 1px dashed #cbd5f5;
        background: #f8fafc;
        border-radius: 10px;
        padding: 6px;
        text-align: center;
        position: relative;
    }

    .image-preview img {
        width: 100%;
        height: 80px;
        object-fit: cover;
        border-radius: 8px;
        border: 1px solid #e2e8f0;
    }

    .image-preview .preview-name {
        font-size: 11px;
        color: #64748b;
        margin-top: 4px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }

    .image-preview .remove-preview {
        position: absolute;
        top: 6px;
        right: 6px;
        width: 22px;
        height: 22px;
        border-radius: 999px;
        border: none;
        background: rgba(239, 68, 68, 0.95);
        color: #fff;
        font-size: 12px;
        line-height: 22px;
        cursor: pointer;
    }

    .image-preview .preview-main {
        margin-top: 6px;
        display: flex;
        justify-content: center;
        gap: 6px;
        font-size: 11px;
        color: #475569;
    }

    .excel-preview-table thead th {
        background: #1d4ed8;
        color: #fff;
        font-size: 12px;
        text-transform: uppercase;
        letter-spacing: .4px;
    }

    /* Print only the modal content. */
    @media print {
        body * { visibility: hidden !important; }
        .modal-backdrop { display: none !important; }

        #productPrintModal, #productPrintModal * { visibility: visible !important; }
        #productPrintModal { position: absolute !important; left: 0 !important; top: 0 !important; width: 100% !important; height: auto !important; overflow: visible !important; }
        #productPrintModal .modal-dialog { max-width: none !important; width: 100% !important; margin: 0 !important; }
        #productPrintModal .modal-content { border: none !important; }
        #productPrintModal .no-print { display: none !important; }

        .editable[contenteditable="true"] {
            outline: none !important;
            box-shadow: none !important;
            padding: 0 !important;
        }

        .print-sheet {
            padding: 8mm;
            box-shadow: none;
        }
    }
</style>

<style id="product-print-page-style">
    @media print {
        @page { size: A4 portrait; margin: 10mm; }
    }
</style>

<div class="modal fade print-modal" id="productPrintModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-xl modal-dialog-scrollable">
        <div class="modal-content">
            <div class="modal-header no-print">
                <div class="d-flex align-items-center gap-2">
                    <i class="fas fa-print"></i>
                    <h5 class="modal-title mb-0">Xem trước khi in: Danh sách sản phẩm</h5>
                </div>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="print-toolbar no-print mb-3">
                    <div class="d-flex flex-wrap gap-2 align-items-end">
                        <button type="button" class="btn btn-sm btn-primary" id="btnPrintNow">Tải PDF</button>
                        <button type="button" class="btn btn-sm btn-outline-secondary" data-bs-dismiss="modal">Đóng</button>

                        <div class="ms-auto d-flex flex-wrap gap-2 align-items-end">
                            <div>
                                <div class="form-label">Mẫu</div>
                                <select id="template" class="form-select form-select-sm">
                                    <option value="minimal" selected>Đơn giản</option>
                                    <option value="vn">Hành chính (VN)</option>
                                </select>
                            </div>
                            <div>
                                <div class="form-label">Khung viền</div>
                                <select id="frame" class="form-select form-select-sm">
                                    <option value="none" selected>Không</option>
                                    <option value="thin">1 nét</option>
                                    <option value="double">2 nét</option>
                                </select>
                            </div>
                            <div>
                                <div class="form-label">Khổ giấy</div>
                                <select id="paperSize" class="form-select form-select-sm">
                                    <option value="A4" selected>A4</option>
                                    <option value="A3">A3</option>
                                    <option value="Letter">Letter</option>
                                </select>
                            </div>
                            <div>
                                <div class="form-label">Hướng</div>
                                <select id="orientation" class="form-select form-select-sm">
                                    <option value="portrait" selected>Dọc</option>
                                    <option value="landscape">Ngang</option>
                                </select>
                            </div>
                            <div>
                                <div class="form-label">Mật độ</div>
                                <select id="density" class="form-select form-select-sm">
                                    <option value="normal" selected>Normal</option>
                                    <option value="compact">Compact</option>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="mt-3 d-flex flex-wrap gap-3 align-items-center">
                        <div class="fw-bold text-muted small">Hiển thị cột:</div>
                        <label class="form-check form-check-inline mb-0">
                            <input class="form-check-input js-col-toggle" type="checkbox" value="img" checked />
                            <span class="form-check-label">Ảnh</span>
                        </label>
                        <label class="form-check form-check-inline mb-0">
                            <input class="form-check-input js-col-toggle" type="checkbox" value="danhmuc" checked />
                            <span class="form-check-label">Danh mục</span>
                        </label>
                        <label class="form-check form-check-inline mb-0">
                            <input class="form-check-input js-col-toggle" type="checkbox" value="khuyenmai" checked />
                            <span class="form-check-label">Khuyến mãi</span>
                        </label>
                        <label class="form-check form-check-inline mb-0">
                            <input class="form-check-input js-col-toggle" type="checkbox" value="tonkho" checked />
                            <span class="form-check-label">Tồn kho</span>
                        </label>
                        <label class="form-check form-check-inline mb-0">
                            <input class="form-check-input js-col-toggle" type="checkbox" value="trangthai" checked />
                            <span class="form-check-label">Trạng thái</span>
                        </label>
                        <label class="form-check form-check-inline mb-0">
                            <input class="form-check-input" id="toggleMoTa" type="checkbox" checked />
                            <span class="form-check-label">Mô tả</span>
                        </label>
                    </div>

                    <div class="text-muted small mt-2">
                        Tip: với mẫu “Hành chính (VN)”, bạn có thể click vào các dòng chữ để sửa trước khi in.
                    </div>
                </div>

                <div id="printRoot" class="density-normal">
                    <div id="printSheet" class="print-sheet template-minimal frame-none">
                        <div class="vn-only vn-header mb-3">
                            <div class="row g-3">
                                <div class="col-6 text-center">
                                    <div class="editable fw-bold text-uppercase" contenteditable="true">CỬA HÀNG LINH KIỆN WEBPC</div>
                                    <div class="editable small" contenteditable="true">Số: ....../WEBPC</div>
                                    <div class="line"></div>
                                </div>
                                <div class="col-6 text-center">
                                    <div class="editable fw-bold text-uppercase" contenteditable="true">CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM</div>
                                    <div class="editable fw-bold" contenteditable="true">Độc lập - Tự do - Hạnh phúc</div>
                                    <div class="line"></div>
                                    <div class="editable small mt-2" contenteditable="true">TP. Hồ Chí Minh, ngày ..... tháng ..... năm .....</div>
                                </div>
                            </div>
                        </div>

                        <div class="minimal-only print-header">
                            <div class="d-flex justify-content-between small text-muted">
                                <div><strong>WEBPC STAFF</strong></div>
                                <div>Ngày in: ${printDate}</div>
                            </div>
                        </div>

                        <div class="text-center mb-3">
                            <div class="print-title editable" contenteditable="true">DANH SÁCH SẢN PHẨM</div>
                            <div class="text-muted small editable minimal-only" contenteditable="true">
                                Người in: <strong><c:out value="${sessionScope.UserName}" /></strong>
                                (<c:out value="${sessionScope.RoleName}" />) | Tổng: <strong>${fn:length(items)}</strong> sản phẩm
                            </div>
                            <div class="text-muted small vn-only editable" contenteditable="true">
                                Tổng: <strong>${fn:length(items)}</strong> sản phẩm
                            </div>
                        </div>

                        <table id="printTable" class="table table-bordered print-table">
                            <thead>
                                <tr>
                                    <th style="width: 70px;">Mã</th>
                                    <th class="col-img" style="width: 70px;">Ảnh</th>
                                    <th>Sản phẩm</th>
                                    <th class="col-danhmuc" style="width: 160px;">Danh mục</th>
                                    <th style="width: 120px;">Giá bán</th>
                                    <th class="col-khuyenmai" style="width: 130px;">Khuyến mãi</th>
                                    <th class="col-tonkho" style="width: 90px;">Tồn kho</th>
                                    <th class="col-trangthai" style="width: 110px;">Trạng thái</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${items}">
                                    <tr>
                                        <td>${item.maSanPham}</td>
                                        <td class="col-img">
                                            <c:choose>
                                                <c:when test="${not empty item.danhSachAnh}">
                                                    <img src="${item.danhSachAnh[0].url}" alt=""
                                                         style="width: 56px; height: 56px; object-fit: cover; border-radius: 10px; border: 1px solid #e2e8f0;" />
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted small">-</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <div class="fw-bold"><c:out value="${item.tenSanPham}" /></div>
                                            <div class="text-muted small js-desc"><c:out value="${item.moTa}" /></div>
                                        </td>
                                        <td class="col-danhmuc"><c:out value="${item.tenDanhMuc}" /></td>
                                        <td><fmt:formatNumber value="${item.giaBan}" pattern="#,##0" /> đ</td>
                                        <td class="col-khuyenmai">
                                            <c:choose>
                                                <c:when test="${item.giaKhuyenMai != null}">
                                                    <fmt:formatNumber value="${item.giaKhuyenMai}" pattern="#,##0" /> đ
                                                </c:when>
                                                <c:otherwise><span class="text-muted">-</span></c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="col-tonkho">${item.soLuongTon}</td>
                                        <td class="col-trangthai">${item.trangThai ? 'Đang bán' : 'Tạm ẩn'}</td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty items}">
                                    <tr><td colspan="8" class="text-center text-muted">Không có dữ liệu sản phẩm.</td></tr>
                                </c:if>
                            </tbody>
                        </table>

                        <div class="vn-only vn-sign mt-4">
                            <div class="row text-center g-3">
                                <div class="col-6">
                                    <div class="fw-bold">NGƯỜI LẬP</div>
                                    <div class="text-muted small">(Ký, ghi rõ họ tên)</div>
                                    <div class="signature-space"></div>
                                    <div class="editable fw-bold" contenteditable="true">................................</div>
                                </div>
                                <div class="col-6">
                                    <div class="fw-bold">DUYỆT</div>
                                    <div class="text-muted small">(Ký, ghi rõ họ tên)</div>
                                    <div class="signature-space"></div>
                                    <div class="editable fw-bold" contenteditable="true">................................</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/html2canvas/1.4.1/html2canvas.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        const modalEl = document.getElementById('productPrintModal');
        const modal = window.bootstrap ? window.bootstrap.Modal.getOrCreateInstance(modalEl) : null;

        const root = modalEl.querySelector('#printRoot');
        const sheet = modalEl.querySelector('#printSheet');
        const pageStyle = document.getElementById('product-print-page-style');
        const template = modalEl.querySelector('#template');
        const frame = modalEl.querySelector('#frame');
        const paperSize = modalEl.querySelector('#paperSize');
        const orientation = modalEl.querySelector('#orientation');
        const density = modalEl.querySelector('#density');
        const toggleMoTa = modalEl.querySelector('#toggleMoTa');
        const btnPrintNow = modalEl.querySelector('#btnPrintNow');

        function applyPage() {
            const size = paperSize.value || 'A4';
            const orient = orientation.value || 'portrait';
            pageStyle.textContent =
                '@media print { @page { size: ' + size + ' ' + orient + '; margin: 10mm; } }';
        }

        function applyDensity() {
            const val = density.value || 'normal';
            root.classList.toggle('density-compact', val === 'compact');
            root.classList.toggle('density-normal', val !== 'compact');
        }

        function applyTemplate() {
            const val = template.value || 'minimal';
            sheet.classList.toggle('template-vn', val === 'vn');
            sheet.classList.toggle('template-minimal', val !== 'vn');

            // Sensible defaults: official template usually wants a frame.
            if (val === 'vn' && (frame.value === 'none')) {
                frame.value = 'double';
                applyFrame();
            }
        }

        function applyFrame() {
            const val = frame.value || 'none';
            sheet.classList.remove('frame-none', 'frame-thin', 'frame-double');
            if (val === 'thin') {
                sheet.classList.add('frame-thin');
                return;
            }
            if (val === 'double') {
                sheet.classList.add('frame-double');
                return;
            }
            sheet.classList.add('frame-none');
        }

        function setColumnHidden(col, hidden) {
            const elements = modalEl.querySelectorAll('.col-' + col);
            elements.forEach((el) => el.classList.toggle('col-hidden', hidden));
        }

        modalEl.querySelectorAll('.js-col-toggle').forEach((checkbox) => {
            checkbox.addEventListener('change', () => {
                setColumnHidden(checkbox.value, !checkbox.checked);
            });
            setColumnHidden(checkbox.value, !checkbox.checked);
        });

        toggleMoTa.addEventListener('change', () => {
            modalEl.querySelectorAll('.js-desc').forEach((el) => {
                el.classList.toggle('col-hidden', !toggleMoTa.checked);
            });
        });

        btnPrintNow.addEventListener('click', async () => {
            if (!window.html2canvas || !window.jspdf) {
                alert('Chưa tải được thư viện PDF. Vui lòng tải lại trang.');
                return;
            }
            const originalText = btnPrintNow.textContent;
            btnPrintNow.disabled = true;
            btnPrintNow.textContent = 'Đang tạo PDF...';
            try {
                const canvas = await window.html2canvas(sheet, {
                    scale: 2,
                    useCORS: true,
                    backgroundColor: '#fff',
                    logging: false
                });
                const imgData = canvas.toDataURL('image/png');
                const size = (paperSize.value || 'A4').toLowerCase();
                const orient = orientation.value || 'portrait';
                const { jsPDF } = window.jspdf;
                const pdf = new jsPDF({ orientation: orient, unit: 'mm', format: size });
                const pageWidth = pdf.internal.pageSize.getWidth();
                const pageHeight = pdf.internal.pageSize.getHeight();
                const imgWidth = pageWidth;
                const imgHeight = (canvas.height * imgWidth) / canvas.width;
                let remainingHeight = imgHeight;
                let position = 0;
                while (remainingHeight > 0) {
                    pdf.addImage(imgData, 'PNG', 0, position, imgWidth, imgHeight, undefined, 'FAST');
                    remainingHeight -= pageHeight;
                    if (remainingHeight > 0) {
                        pdf.addPage();
                        position -= pageHeight;
                    }
                }
                const dateStr = new Date().toISOString().slice(0, 10);
                pdf.save('danh-sach-san-pham-' + dateStr + '.pdf');
            } catch (err) {
                console.error(err);
                alert('Không thể tạo PDF. Vui lòng thử lại.');
            } finally {
                btnPrintNow.disabled = false;
                btnPrintNow.textContent = originalText;
            }
        });

        // init
        applyPage();
        applyDensity();
        applyFrame();
        applyTemplate();
        modalEl.querySelectorAll('.js-desc').forEach((el) => {
            el.classList.toggle('col-hidden', !toggleMoTa.checked);
        });

        template.addEventListener('change', applyTemplate);
        frame.addEventListener('change', applyFrame);
        paperSize.addEventListener('change', applyPage);
        orientation.addEventListener('change', applyPage);
        density.addEventListener('change', applyDensity);

        // Auto-open modal if /Admin/SanPham?print=1
        try {
            const params = new URLSearchParams(window.location.search);
            if (params.get('print') === '1' && modal) {
                modal.show();
            }
        } catch (e) {}

        modalEl.addEventListener('hidden.bs.modal', () => {
            try {
                const url = new URL(window.location.href);
                if (url.searchParams.get('print') === '1') {
                    url.searchParams.delete('print');
                    window.history.replaceState({}, '', url.toString());
                }
            } catch (e) {}
        });

        const productTable = document.getElementById('productTable');
        const productRows = productTable
            ? Array.from(productTable.querySelectorAll('tbody tr[data-id]'))
            : [];
        const noMatchRow = document.getElementById('noMatchRow');
        const categoryFilter = document.getElementById('categoryFilter');
        const productSearch = document.getElementById('productSearch');
        const clearFiltersBtn = document.getElementById('btnClearFilters');
        const filterSummary = document.getElementById('filterSummary');
        const categoryTabs = document.querySelectorAll('#categoryTabs [data-category]');

        function normalizeText(text) {
            return (text || '').toLowerCase().trim();
        }

        function setActiveCategoryTab(categoryValue) {
            categoryTabs.forEach((tab) => {
                const isActive = (tab.dataset.category || 'all') === categoryValue;
                tab.classList.toggle('active', isActive);
            });
        }

        function applyFilters() {
            if (!productRows.length) {
                return;
            }
            const categoryValue = categoryFilter ? categoryFilter.value : 'all';
            const searchValue = normalizeText(productSearch ? productSearch.value : '');
            let visibleCount = 0;

            productRows.forEach((row) => {
                const rowCategory = row.dataset.danhmucId || '';
                const rowName = normalizeText(row.dataset.name);
                const matchCategory = categoryValue === 'all' || categoryValue === '' || rowCategory === categoryValue;
                const matchSearch = !searchValue || rowName.includes(searchValue);
                const shouldShow = matchCategory && matchSearch;
                row.classList.toggle('d-none', !shouldShow);
                if (shouldShow) {
                    visibleCount += 1;
                }
            });

            if (noMatchRow) {
                noMatchRow.classList.toggle('d-none', visibleCount !== 0);
            }
            if (filterSummary) {
                filterSummary.textContent = 'Hiển thị ' + visibleCount + '/' + productRows.length + ' sản phẩm';
            }
        }

        if (categoryFilter) {
            categoryFilter.addEventListener('change', () => {
                setActiveCategoryTab(categoryFilter.value || 'all');
                applyFilters();
            });
        }

        if (productSearch) {
            productSearch.addEventListener('input', applyFilters);
        }

        if (clearFiltersBtn) {
            clearFiltersBtn.addEventListener('click', () => {
                if (productSearch) {
                    productSearch.value = '';
                }
                if (categoryFilter) {
                    categoryFilter.value = 'all';
                }
                setActiveCategoryTab('all');
                applyFilters();
            });
        }

        categoryTabs.forEach((tab) => {
            tab.addEventListener('click', () => {
                const value = tab.dataset.category || 'all';
                if (categoryFilter) {
                    categoryFilter.value = value;
                }
                setActiveCategoryTab(value);
                applyFilters();
            });
        });

        function bindImagePicker(inputEl, previewEl, clearBtn, mainFieldName, onMainSelected) {
            if (!inputEl || !previewEl) {
                return;
            }
            let selectedIndex = 0;

            function rebuild() {
                previewEl.innerHTML = '';
                const files = Array.from(inputEl.files || []);
                if (!files.length) {
                    selectedIndex = 0;
                    const empty = document.createElement('div');
                    empty.className = 'text-muted small';
                    empty.textContent = 'Chưa chọn ảnh.';
                    previewEl.appendChild(empty);
                    return;
                }
                if (selectedIndex >= files.length) {
                    selectedIndex = 0;
                }
                files.forEach((file, index) => {
                    const wrapper = document.createElement('div');
                    wrapper.className = 'preview-item';

                    const img = document.createElement('img');
                    const url = URL.createObjectURL(file);
                    img.src = url;
                    img.alt = file.name || '';
                    img.onload = () => URL.revokeObjectURL(url);

                    const label = document.createElement('div');
                    label.className = 'preview-name';
                    label.textContent = file.name || 'image';

                    const removeBtn = document.createElement('button');
                    removeBtn.type = 'button';
                    removeBtn.className = 'remove-preview js-remove-image';
                    removeBtn.dataset.index = String(index);
                    removeBtn.textContent = '×';

                    const mainWrap = document.createElement('label');
                    mainWrap.className = 'preview-main';

                    const mainRadio = document.createElement('input');
                    mainRadio.type = 'radio';
                    mainRadio.name = mainFieldName;
                    mainRadio.value = String(index);
                    mainRadio.className = 'form-check-input';
                    mainRadio.checked = index === selectedIndex;
                    mainRadio.addEventListener('change', () => {
                        selectedIndex = index;
                        if (typeof onMainSelected === 'function') {
                            onMainSelected();
                        }
                    });

                    const mainText = document.createElement('span');
                    mainText.textContent = 'Ảnh đại diện';

                    mainWrap.appendChild(mainRadio);
                    mainWrap.appendChild(mainText);

                    wrapper.appendChild(removeBtn);
                    wrapper.appendChild(img);
                    wrapper.appendChild(label);
                    wrapper.appendChild(mainWrap);
                    previewEl.appendChild(wrapper);
                });
            }

            function removeAt(index) {
                if (!window.DataTransfer) {
                    inputEl.value = '';
                    rebuild();
                    return;
                }
                const files = Array.from(inputEl.files || []);
                const dt = new DataTransfer();
                files.forEach((file, i) => {
                    if (i !== index) {
                        dt.items.add(file);
                    }
                });
                inputEl.files = dt.files;
                rebuild();
            }

            previewEl.addEventListener('click', (event) => {
                const btn = event.target.closest('.js-remove-image');
                if (!btn) {
                    return;
                }
                const index = Number(btn.dataset.index);
                if (Number.isNaN(index)) {
                    return;
                }
                removeAt(index);
            });

            if (clearBtn) {
                clearBtn.addEventListener('click', () => {
                    inputEl.value = '';
                    rebuild();
                });
            }

            inputEl.addEventListener('change', rebuild);
            rebuild();
        }

        const createImageInput = document.getElementById('createImageInput');
        const createImagePreview = document.getElementById('createImagePreview');
        const createImageClear = document.getElementById('createImageClear');
        bindImagePicker(createImageInput, createImagePreview, createImageClear, 'mainImageIndex');

        const editImageInput = document.getElementById('editImageInput');
        const editImagePreview = document.getElementById('editImagePreview');
        const editImageClear = document.getElementById('editImageClear');
        bindImagePicker(editImageInput, editImagePreview, editImageClear, 'mainImageIndex', () => {
            if (!editModalEl) {
                return;
            }
            editModalEl.querySelectorAll('input[name="mainImageId"]').forEach((radio) => {
                radio.checked = false;
            });
        });

        applyFilters();

        const editModalEl = document.getElementById('productEditModal');
        const deleteModalEl = document.getElementById('productDeleteModal');

        document.querySelectorAll('.js-edit-product').forEach((btn) => {
            btn.addEventListener('click', () => {
                const row = btn.closest('tr');
                if (!row || !editModalEl) {
                    return;
                }
                editModalEl.querySelector('#editMaSanPham').value = row.dataset.id || '';
                editModalEl.querySelector('#editTenSanPham').value = row.dataset.name || '';
                editModalEl.querySelector('#editGiaBan').value = row.dataset.gia || '';
                editModalEl.querySelector('#editGiaKhuyenMai').value = row.dataset.khuyenmai || '';
                editModalEl.querySelector('#editSoLuongTon').value = row.dataset.ton || '';
                editModalEl.querySelector('#editMoTa').value = row.dataset.mota || '';
                editModalEl.querySelector('#editTrangThai').value = row.dataset.trangthai === 'false' ? 'false' : 'true';

                const categorySelect = editModalEl.querySelector('#editDanhMuc');
                if (categorySelect) {
                    categorySelect.value = row.dataset.danhmucId || '';
                }

                const imageList = editModalEl.querySelector('#editImageList');
                const imageEmpty = editModalEl.querySelector('#editImageEmpty');
                imageList.innerHTML = '';
                const images = row.querySelectorAll('.js-image');
                if (images.length === 0) {
                    imageEmpty.classList.remove('d-none');
                } else {
                    imageEmpty.classList.add('d-none');
                    let hasMain = false;
                    let firstMainRadio = null;
                    images.forEach((img) => {
                        const wrapper = document.createElement('label');
                        wrapper.className = 'image-chip';

                        const checkbox = document.createElement('input');
                        checkbox.type = 'checkbox';
                        checkbox.name = 'publicIdsToDelete';
                        checkbox.value = img.dataset.publicId || '';
                        checkbox.className = 'form-check-input';

                        const thumb = document.createElement('img');
                        thumb.src = img.dataset.url || '';
                        thumb.alt = '';

                        const mainWrap = document.createElement('label');
                        mainWrap.className = 'main-radio';

                        const mainRadio = document.createElement('input');
                        mainRadio.type = 'radio';
                        mainRadio.name = 'mainImageId';
                        mainRadio.value = img.dataset.id || '';
                        mainRadio.className = 'form-check-input';
                        mainRadio.checked = img.dataset.main === 'true';
                        mainRadio.addEventListener('change', () => {
                            if (!mainRadio.checked) {
                                return;
                            }
                            const preview = editModalEl.querySelector('#editImagePreview');
                            if (preview) {
                                preview.querySelectorAll('input[name="mainImageIndex"]').forEach((radio) => {
                                    radio.checked = false;
                                });
                            }
                        });
                        if (mainRadio.checked) {
                            hasMain = true;
                        }
                        if (!firstMainRadio) {
                            firstMainRadio = mainRadio;
                        }

                        const mainText = document.createElement('span');
                        mainText.textContent = 'Ảnh đại diện';

                        mainWrap.appendChild(mainRadio);
                        mainWrap.appendChild(mainText);

                        wrapper.appendChild(checkbox);
                        wrapper.appendChild(thumb);
                        wrapper.appendChild(mainWrap);
                        imageList.appendChild(wrapper);
                    });
                    if (!hasMain && firstMainRadio) {
                        firstMainRadio.checked = true;
                    }
                }

                const modal = window.bootstrap ? window.bootstrap.Modal.getOrCreateInstance(editModalEl) : null;
                if (modal) {
                    modal.show();
                }
            });
        });

        document.querySelectorAll('.js-delete-product').forEach((btn) => {
            btn.addEventListener('click', () => {
                const row = btn.closest('tr');
                if (!row || !deleteModalEl) {
                    return;
                }
                deleteModalEl.querySelector('#deleteMaSanPham').value = row.dataset.id || '';
                deleteModalEl.querySelector('#deleteTenSanPham').textContent = row.dataset.name || 'này';

                const modal = window.bootstrap ? window.bootstrap.Modal.getOrCreateInstance(deleteModalEl) : null;
                if (modal) {
                    modal.show();
                }
            });
        });
    });
</script>
<%@ include file="/WEB-INF/views/common/layout-end.jspf" %>
