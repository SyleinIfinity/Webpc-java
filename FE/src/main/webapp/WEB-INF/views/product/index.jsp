<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/head-start.jspf" %>
<link href="${ctx}/assets/css/product-index.css" rel="stylesheet">
<%@ include file="/WEB-INF/views/common/layout-start.jspf" %>

<div class="container py-4">
    <div class="row">
        <div class="col-lg-3 mb-4">
            <div class="category-widget">
                <div class="cat-header">
                    <i class="bi bi-grid-fill me-2"></i> Danh Mục
                </div>
                <ul class="cat-list">
                    <li class="cat-item">
                        <a href="${ctx}/Product" class="cat-link ${empty currentCategory ? 'active' : ''}">
                            <span>Tất cả sản phẩm</span>
                        </a>
                    </li>
                    <c:forEach var="parent" items="${categories}">
                        <c:if test="${empty parent.maDanhMucCha or parent.maDanhMucCha <= 0}">
                            <li class="cat-item">
                                <a href="${ctx}/Product?categoryId=${parent.maDanhMuc}" class="cat-link ${currentCategory == parent.maDanhMuc ? 'active' : ''}">
                                    <span><c:out value="${parent.tenDanhMuc}" /></span>
                                </a>
                                <div class="cat-submenu expanded">
                                    <c:forEach var="child" items="${categories}">
                                        <c:if test="${child.maDanhMucCha == parent.maDanhMuc}">
                                            <a href="${ctx}/Product?categoryId=${child.maDanhMuc}" class="sub-link ${currentCategory == child.maDanhMuc ? 'active' : ''}">
                                                <c:out value="${child.tenDanhMuc}" />
                                            </a>
                                        </c:if>
                                    </c:forEach>
                                </div>
                            </li>
                        </c:if>
                    </c:forEach>
                </ul>
            </div>
        </div>

        <div class="col-lg-9">
            <div class="d-flex justify-content-between align-items-center mb-4 bg-white p-3 rounded shadow-sm">
                <h4 class="mb-0 fw-bold text-dark">
                    <c:choose>
                        <c:when test="${not empty currentCategory}">Sản phẩm theo danh mục</c:when>
                        <c:when test="${not empty currentSearch}">Kết quả tìm kiếm: "<c:out value="${currentSearch}" />"</c:when>
                        <c:otherwise>Tất cả sản phẩm</c:otherwise>
                    </c:choose>
                </h4>
                <span class="text-muted small">Hiển thị ${fn:length(products)} sản phẩm</span>
            </div>

            <c:choose>
                <c:when test="${not empty products}">
                    <div class="row row-cols-1 row-cols-md-2 row-cols-xl-3 g-4">
                        <c:forEach var="item" items="${products}">
                            <div class="col">
                                <div class="card h-100 product-card border-0 shadow-sm">
                                    <div class="position-relative overflow-hidden">
                                        <a href="${ctx}/Product/Detail?id=${item.maSanPham}">
                                            <img src="${item.hinhAnhDaiDien}" class="card-img-top p-3 product-img-custom" alt="${item.tenSanPham}">
                                        </a>
                                        <c:if test="${item.coKhuyenMai}">
                                            <span class="badge bg-danger position-absolute top-0 start-0 m-3">SALE</span>
                                        </c:if>
                                    </div>
                                    <div class="card-body d-flex flex-column">
                                        <h5 class="card-title fs-6">
                                            <a href="${ctx}/Product/Detail?id=${item.maSanPham}" class="text-decoration-none text-dark fw-bold text-truncate-2">
                                                <c:out value="${item.tenSanPham}" />
                                            </a>
                                        </h5>
                                        <div class="mt-auto">
                                            <div class="price-box mb-2">
                                                <c:choose>
                                                    <c:when test="${item.coKhuyenMai}">
                                                        <span class="text-danger fw-bold fs-5 me-2"><fmt:formatNumber value="${item.giaKhuyenMai}" pattern="#,##0" /> đ</span>
                                                        <span class="text-muted text-decoration-line-through small"><fmt:formatNumber value="${item.giaBan}" pattern="#,##0" /> đ</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-primary fw-bold fs-5"><fmt:formatNumber value="${item.giaBan}" pattern="#,##0" /> đ</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            <div class="d-grid gap-2 d-flex mt-auto">
                                                <a href="${ctx}/Product/Detail?id=${item.maSanPham}" class="btn btn-outline-primary btn-sm flex-grow-1">Chi tiết</a>
                                                <c:choose>
                                                    <c:when test="${cartEnabled}">
                                                        <button class="btn btn-primary btn-sm" type="button" onclick="addToCart(${item.maSanPham})">
                                                            <i class="bi bi-cart-plus"></i>
                                                        </button>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <button class="btn btn-primary btn-sm" type="button" disabled="disabled" title="Cart chua duoc port o BE">
                                                            <i class="bi bi-cart-plus"></i>
                                                        </button>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="alert alert-warning text-center py-5">
                        <i class="bi bi-search fs-1 d-block mb-3"></i>
                        <h5>Không tìm thấy sản phẩm nào.</h5>
                        <a href="${ctx}/Product" class="btn btn-primary mt-2">Xem tất cả sản phẩm</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<c:choose>
    <c:when test="${not empty sessionScope.UserToken or not empty sessionScope.User}">
        <c:set var="productUserLoggedInJs" value="true" />
    </c:when>
    <c:otherwise>
        <c:set var="productUserLoggedInJs" value="false" />
    </c:otherwise>
</c:choose>
<div id="product-config"
     data-is-user-logged-in="${productUserLoggedInJs}"
     data-login-url="${ctx}/Login"
     hidden></div>

<%@ include file="/WEB-INF/views/common/layout-end.jspf" %>
<script>
    (function () {
        var productConfigEl = document.getElementById('product-config');
        window.ProductConfig = {
            isUserLoggedIn: productConfigEl ? productConfigEl.dataset.isUserLoggedIn : 'false',
            loginUrl: productConfigEl ? productConfigEl.dataset.loginUrl : ''
        };
    })();
</script>
<script src="${ctx}/assets/js/product-index.js"></script>
</body>
</html>
