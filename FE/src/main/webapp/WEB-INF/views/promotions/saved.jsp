<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/head-start.jspf" %>
<link href="${ctx}/assets/css/promotions.css" rel="stylesheet">
<%@ include file="/WEB-INF/views/common/layout-start.jspf" %>

<section class="py-5">
    <div class="container">
        <h2 class="mb-4">
            <i class="bi bi-bookmark-heart-fill text-danger"></i>
            Kho khuyến mãi đã lưu
        </h2>

        <c:choose>
            <c:when test="${empty savedPromotions}">
                <div class="alert alert-info">
                    Bạn chưa lưu mã khuyến mãi nào.
                    <a href="${ctx}/Promotions">Quay lại trang khuyến mãi</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="row g-4">
                    <c:forEach var="km" items="${savedPromotions}">
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
                                        <i class="bi bi-clock"></i> HSD: <c:out value="${km.ngayKetThucDisplay}" />
                                    </div>
                                </div>

                                <div class="voucher-right">
                                    <span class="badge bg-success mb-2">Đã lưu</span>
                                    <div class="voucher-code"><c:out value="${km.maCodeKM}" /></div>
                                    <form method="post" action="${ctx}/Promotions/XoaMaKhuyenMai" class="mt-2">
                                        <input type="hidden" name="maKMKH" value="${km.maKMKH}">
                                        <button type="submit" class="btn btn-outline-danger btn-sm">
                                            <i class="bi bi-trash"></i> Xóa mã
                                        </button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>

        <div class="mt-4">
            <a href="${ctx}/Promotions" class="btn btn-secondary">
                ← Quay lại trang khuyến mãi
            </a>
        </div>
    </div>
</section>

<%@ include file="/WEB-INF/views/common/layout-end.jspf" %>
</body>
</html>
