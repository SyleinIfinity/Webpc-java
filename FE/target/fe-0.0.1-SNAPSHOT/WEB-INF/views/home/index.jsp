<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/head-start.jspf" %>
<%@ include file="/WEB-INF/views/common/layout-start.jspf" %>

<section class="hero-section">
    <div class="hero-overlay"></div>
    <div class="container h-100">
        <div class="row h-100 align-items-center">
            <div class="col-lg-8">
                <h1 class="hero-title mb-4">Linh kiện máy tính cho cấu hình mới</h1>
                <p class="hero-subtitle mb-4">Frontend JSP mới đang được nối trực tiếp với backend Spring Boot ở các nhóm auth, user, address và catalog.</p>
                <a href="${ctx}/Product" class="btn btn-primary btn-lg px-5">Xem Sản Phẩm</a>
            </div>
        </div>
    </div>
</section>

<section class="py-5">
    <div class="container">
        <h2 class="section-title mb-4">Danh Mục Nổi Bật</h2>
        <div class="row g-4">
            <div class="col-6 col-md-4 col-lg-2"><div class="category-card"><i class="bi bi-cpu category-icon"></i><h6 class="category-name">CPU</h6></div></div>
            <div class="col-6 col-md-4 col-lg-2"><div class="category-card"><i class="bi bi-motherboard category-icon"></i><h6 class="category-name">Mainboard</h6></div></div>
            <div class="col-6 col-md-4 col-lg-2"><div class="category-card"><i class="bi bi-memory category-icon"></i><h6 class="category-name">RAM</h6></div></div>
            <div class="col-6 col-md-4 col-lg-2"><div class="category-card"><i class="bi bi-device-hdd category-icon"></i><h6 class="category-name">Lưu Trữ</h6></div></div>
            <div class="col-6 col-md-4 col-lg-2"><div class="category-card"><i class="bi bi-gpu-card category-icon"></i><h6 class="category-name">VGA</h6></div></div>
            <div class="col-6 col-md-4 col-lg-2"><div class="category-card"><i class="bi bi-fan category-icon"></i><h6 class="category-name">Tản Nhiệt</h6></div></div>
        </div>
    </div>
</section>

<section class="py-5 bg-light">
    <div class="container">
        <h2 class="section-title mb-4">Sản Phẩm Mới Nhất</h2>
        <div class="row g-4">
            <c:forEach var="item" items="${latestProducts}">
                <div class="col-md-6 col-lg-3">
                    <a href="${ctx}/Product/Detail?id=${item.maSanPham}" class="text-decoration-none">
                        <div class="product-card">
                            <div class="product-image">
                                <img src="${item.hinhAnhDaiDien}" alt="${item.tenSanPham}">
                            </div>
                            <div class="product-info">
                                <h5 class="product-title"><c:out value="${item.tenSanPham}" /></h5>
                                <div class="d-flex justify-content-between align-items-center">
                                    <span class="product-price">
                                        <fmt:formatNumber value="${item.giaHienThi}" pattern="#,##0" />₫
                                    </span>
                                    <span class="btn-add-cart"><i class="bi bi-arrow-right"></i></span>
                                </div>
                            </div>
                        </div>
                    </a>
                </div>
            </c:forEach>
        </div>
    </div>
</section>

<%@ include file="/WEB-INF/views/common/layout-end.jspf" %>
</body>
</html>
