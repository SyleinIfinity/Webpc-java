<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/head-start.jspf" %>
<link href="${ctx}/assets/css/product-detail.css" rel="stylesheet">
<%@ include file="/WEB-INF/views/common/layout-start.jspf" %>

<div class="container">
    <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${ctx}/Home" class="text-decoration-none">Trang chủ</a></li>
            <li class="breadcrumb-item"><a href="${ctx}/Product" class="text-decoration-none">Sản phẩm</a></li>
            <li class="breadcrumb-item active" aria-current="page"><c:out value="${product.tenSanPham}" /></li>
        </ol>
    </nav>

    <div class="row g-5">
        <div class="col-md-5">
            <div class="card border-0 shadow-sm p-3">
                <img id="mainImage" src="${product.hinhAnhDaiDien}" alt="${product.tenSanPham}" class="img-fluid rounded mb-3 detail-main-img">
                <c:if test="${fn:length(product.danhSachAnh) > 1}">
                    <div class="d-flex gap-2 overflow-auto pb-2">
                        <c:forEach var="img" items="${product.danhSachAnh}">
                            <img src="${img.url}" onclick="changeImage(this.src)" class="rounded border detail-thumb-img">
                        </c:forEach>
                    </div>
                </c:if>
            </div>
        </div>

        <div class="col-md-7">
            <h2 class="fw-bold text-dark mb-2"><c:out value="${product.tenSanPham}" /></h2>
            <div class="mb-3">
                <span class="badge bg-light text-dark border me-2">Mã SP: #${product.maSanPham}</span>
                <span class="badge bg-light text-dark border">Danh mục: <c:out value="${product.tenDanhMuc}" /></span>
            </div>

            <div class="price-section bg-light p-3 rounded mb-4">
                <c:choose>
                    <c:when test="${product.coKhuyenMai}">
                        <div class="d-flex align-items-end gap-3">
                            <span class="text-danger fw-bold fs-2"><fmt:formatNumber value="${product.giaKhuyenMai}" pattern="#,##0" /> đ</span>
                            <span class="text-muted text-decoration-line-through fs-5"><fmt:formatNumber value="${product.giaBan}" pattern="#,##0" /> đ</span>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <span class="text-primary fw-bold fs-2"><fmt:formatNumber value="${product.giaBan}" pattern="#,##0" /> đ</span>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="status mb-4">
                <c:choose>
                    <c:when test="${product.soLuongTon > 0}">
                        <div class="text-success fw-bold"><i class="bi bi-check-circle-fill me-1"></i> Còn hàng (Sẵn sàng giao)</div>
                    </c:when>
                    <c:otherwise>
                        <div class="text-danger fw-bold"><i class="bi bi-x-circle-fill me-1"></i> Tạm hết hàng</div>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="product-action mt-4">
                <div class="d-flex align-items-center mb-4">
                    <label class="me-3 fw-bold">Số lượng:</label>
                    <input type="number" id="inputQty" class="form-control input-qty-detail" value="1" min="1" max="99">
                </div>
                <div class="d-flex gap-3">
                    <c:choose>
                        <c:when test="${cartEnabled}">
                            <button type="button" class="btn btn-outline-primary btn-action flex-grow-1" onclick="addToCartDetail(${product.maSanPham})">
                                <i class="bi bi-cart-plus me-2"></i> Thêm vào giỏ
                            </button>
                            <button type="button" class="btn btn-primary btn-action flex-grow-1" onclick="buyNow(${product.maSanPham})">
                                <i class="bi bi-cash-coin me-2"></i> Mua ngay
                            </button>
                        </c:when>
                        <c:otherwise>
                            <button type="button" class="btn btn-outline-primary btn-action flex-grow-1" disabled="disabled">
                                <i class="bi bi-cart-plus me-2"></i> Thêm vào giỏ
                            </button>
                            <button type="button" class="btn btn-primary btn-action flex-grow-1" disabled="disabled">
                                <i class="bi bi-cash-coin me-2"></i> Mua ngay
                            </button>
                        </c:otherwise>
                    </c:choose>
                </div>
                <c:if test="${not cartEnabled}">
                    <div class="alert alert-warning mt-3 mb-0">Chức năng giỏ hàng/chốt đơn sẽ bật ở pha sau khi <code>BE</code> port xong module tương ứng.</div>
                </c:if>
            </div>

            <div class="description mt-4">
                <h5 class="fw-bold border-bottom pb-2 mb-3">Mô tả sản phẩm</h5>
                <div class="text-secondary">
                    <c:out value="${empty product.moTa ? 'Đang cập nhật mô tả chi tiết cho sản phẩm này...' : product.moTa}" />
                </div>
            </div>

            <div class="row mt-5">
                <div class="col-12">
                    <ul class="nav nav-tabs" id="productTab" role="tablist">
                        <li class="nav-item" role="presentation">
                            <button class="nav-link active fw-bold" data-bs-toggle="tab" data-bs-target="#spec" type="button">
                                <i class="bi bi-cpu me-2"></i>Thông Số Kỹ Thuật
                            </button>
                        </li>
                    </ul>
                    <div class="tab-content border border-top-0 rounded-bottom bg-white" id="productTabContent">
                        <div class="tab-pane fade show active p-4" id="spec">
                            <c:choose>
                                <c:when test="${not empty product.thongSoKyThuat}">
                                    <table class="table table-striped table-hover mb-0">
                                        <thead class="table-light">
                                            <tr>
                                                <th class="fw-bold text-primary" style="width: 35%;">Thông số</th>
                                                <th class="fw-bold text-primary">Giá trị</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="spec" items="${product.thongSoKyThuat}">
                                                <tr>
                                                    <th class="text-secondary"><i class="bi bi-check-lg text-success me-2"></i><c:out value="${spec.tenThongSo}" /></th>
                                                    <td class="fw-semibold"><c:out value="${spec.giaTri}" /></td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </c:when>
                                <c:otherwise>
                                    <div class="alert alert-info mb-0">Thông tin thông số kỹ thuật đang được cập nhật...</div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<c:choose>
    <c:when test="${not empty sessionScope.UserToken or not empty sessionScope.User}">
        <c:set var="detailUserLoggedInJs" value="true" />
    </c:when>
    <c:otherwise>
        <c:set var="detailUserLoggedInJs" value="false" />
    </c:otherwise>
</c:choose>
<div id="detail-config"
     data-is-user-logged-in="${detailUserLoggedInJs}"
     data-login-url="${ctx}/Login"
     hidden></div>

<%@ include file="/WEB-INF/views/common/layout-end.jspf" %>
<script>
    (function () {
        var detailConfigEl = document.getElementById('detail-config');
        window.DetailConfig = {
            isUserLoggedIn: detailConfigEl ? detailConfigEl.dataset.isUserLoggedIn : 'false',
            loginUrl: detailConfigEl ? detailConfigEl.dataset.loginUrl : ''
        };
    })();
</script>
<script src="${ctx}/assets/js/product-detail.js"></script>
</body>
</html>
