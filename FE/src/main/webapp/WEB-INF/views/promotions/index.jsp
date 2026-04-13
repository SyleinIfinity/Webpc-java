<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/head-start.jspf" %>
<link href="${ctx}/assets/css/promotions.css" rel="stylesheet">
<%@ include file="/WEB-INF/views/common/layout-start.jspf" %>

<section class="promo-hero-banner">
    <div class="container h-100">
        <div class="row h-100 align-items-center">
            <div class="col-12 text-center">
                <h1 class="hero-banner-title mb-3">KHO KHUYẾN MÃI</h1>
                <p class="hero-banner-subtitle">Lưu mã trước khi thanh toán để nhận ưu đãi tốt nhất.</p>
            </div>
        </div>
    </div>
</section>

<section class="voucher-section py-5">
    <div class="container">
        <div class="voucher-highlight-banner mb-5">
            <div class="row align-items-center">
                <div class="col-lg-8">
                    <div class="highlight-content">
                        <span class="highlight-badge">HOT DEAL</span>
                        <h2 class="highlight-title">Giảm giá trực tiếp và giảm theo phần trăm</h2>
                        <p class="highlight-text">Áp dụng theo điều kiện đơn hàng. Hệ thống sẽ tự tính tại bước thanh toán.</p>
                    </div>
                </div>
                <div class="col-lg-4 text-end">
                    <img src="https://images.unsplash.com/photo-1607082348824-0a96f2a4b9da?w=400" alt="Discount" class="highlight-image">
                </div>
            </div>
        </div>

        <div class="d-flex justify-content-end mb-4">
            <a href="${ctx}/Promotions/Saved" class="btn btn-outline-primary">
                <i class="bi bi-bookmark-heart"></i>
                Kho khuyến mãi đã lưu
                <span class="badge bg-danger ms-1"><c:out value="${fn:length(promotionModel.khoKhuyenMaiDaLuu)}" /></span>
            </a>
        </div>

        <c:choose>
            <c:when test="${empty promotionModel.tatCaKhuyenMai}">
                <div class="alert alert-warning">Hiện chưa có chương trình khuyến mãi khả dụng.</div>
            </c:when>
            <c:otherwise>
                <div class="row g-4">
                    <c:forEach var="km" items="${promotionModel.tatCaKhuyenMai}">
                        <c:set var="daLuu" value="false" />
                        <c:forEach var="savedId" items="${promotionModel.khoKhuyenMaiDaLuu}">
                            <c:if test="${savedId == km.maKhuyenMai}">
                                <c:set var="daLuu" value="true" />
                            </c:if>
                        </c:forEach>

                        <div class="col-lg-6">
                            <div class="voucher-card">
                                <div class="voucher-left ${km.loaiGiam == 'DIRECT' ? 'special' : ''}">
                                    <div class="voucher-icon">
                                        <c:choose>
                                            <c:when test="${km.loaiGiam == 'PERCENT'}">
                                                <i class="bi bi-percent"></i>
                                            </c:when>
                                            <c:otherwise>
                                                <i class="bi bi-gift"></i>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>

                                <div class="voucher-middle">
                                    <div class="voucher-discount">
                                        <c:choose>
                                            <c:when test="${km.loaiGiam == 'PERCENT'}">
                                                GIẢM <fmt:formatNumber value="${km.giaTriGiam}" pattern="#,##0" />%
                                            </c:when>
                                            <c:otherwise>
                                                GIẢM <fmt:formatNumber value="${km.giaTriGiam}" pattern="#,##0" />₫
                                            </c:otherwise>
                                        </c:choose>
                                    </div>

                                    <div class="voucher-title"><c:out value="${km.tenChuongTrinh}" /></div>

                                    <div class="voucher-desc">
                                        <i class="bi bi-tag"></i>
                                        Đơn tối thiểu: <strong><fmt:formatNumber value="${km.donHangToiThieu}" pattern="#,##0" />₫</strong>
                                    </div>

                                    <c:if test="${not empty km.giamToiDa}">
                                        <div class="voucher-desc">
                                            <i class="bi bi-arrow-down-circle"></i>
                                            Giảm tối đa: <strong><fmt:formatNumber value="${km.giamToiDa}" pattern="#,##0" />₫</strong>
                                        </div>
                                    </c:if>

                                    <div class="voucher-expiry">
                                        <i class="bi bi-clock"></i>
                                        HSD: <c:out value="${km.ngayKetThucDisplay}" />
                                        | Còn: <strong><c:out value="${km.soLuongConLai}" /></strong> mã
                                    </div>
                                </div>

                                <div class="voucher-right">
                                    <c:choose>
                                        <c:when test="${daLuu}">
                                            <button class="voucher-btn disabled" type="button" disabled>
                                                <i class="bi bi-check-circle"></i><span>ĐÃ LƯU</span>
                                            </button>
                                        </c:when>
                                        <c:when test="${km.coTheLuu}">
                                            <form method="post" action="${ctx}/Promotions/LuuMaKhuyenMai">
                                                <input type="hidden" name="maKhuyenMai" value="${km.maKhuyenMai}">
                                                <button type="submit" class="voucher-btn">
                                                    <i class="bi bi-clipboard"></i><span>LƯU MÃ</span>
                                                </button>
                                            </form>
                                        </c:when>
                                        <c:otherwise>
                                            <button class="voucher-btn disabled" type="button" disabled>
                                                <i class="bi bi-clock"></i><span>KHÔNG KHẢ DỤNG</span>
                                            </button>
                                        </c:otherwise>
                                    </c:choose>
                                    <div class="voucher-code"><c:out value="${km.maCodeKM}" /></div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</section>

<%@ include file="/WEB-INF/views/common/layout-end.jspf" %>
</body>
</html>
