<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/head-start.jspf" %>
<link href="${ctx}/assets/css/order-history.css" rel="stylesheet">
<%@ include file="/WEB-INF/views/common/layout-start.jspf" %>

<section class="py-5 bg-light min-vh-100">
    <div class="container">
        <h2 class="mb-4 fw-bold">Tài Khoản Của Tôi</h2>

        <div class="row g-4">
            <div class="col-lg-3">
                <div class="card p-3 shadow-sm">
                    <div class="d-flex align-items-center border-bottom pb-3 mb-3">
                        <img src="https://via.placeholder.com/60"
                             class="rounded-circle me-3 border"
                             style="width:60px;height:60px;object-fit:cover;">
                        <div>
                            <p class="mb-0 fw-bold">Xin chào,</p>
                            <p class="mb-0 text-primary fw-semibold text-truncate">
                                <c:out value="${sessionScope.UserName}" default="Khách hàng" />
                            </p>
                        </div>
                    </div>

                    <ul class="nav nav-pills flex-column user-dashboard-nav">
                        <li class="nav-item">
                            <a class="nav-link" href="${ctx}/InfoUser">
                                <i class="bi bi-person-circle me-2"></i>Thông tin cá nhân
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link active-info" href="${ctx}/DonHang">
                                <i class="bi bi-receipt me-2"></i>Lịch sử đơn hàng
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${ctx}/InfoAddress">
                                <i class="bi bi-geo-alt me-2"></i>Sổ địa chỉ
                            </a>
                        </li>
                        <li class="nav-item mt-3">
                            <a class="nav-link text-danger" href="${ctx}/Login/Logout">
                                <i class="bi bi-box-arrow-right me-2"></i>Đăng xuất
                            </a>
                        </li>
                    </ul>
                </div>
            </div>

            <div class="col-lg-9">
                <div class="card p-4 shadow-sm">
                    <h4 class="mb-4 fw-bold border-bottom pb-2">Lịch sử giao dịch</h4>

                    <div class="order-history-container">
                        <c:choose>
                            <c:when test="${empty orders}">
                                <div class="row">
                                    <div class="col-md-8 mx-auto">
                                        <div class="empty-state-box">
                                            <img src="https://cdn-icons-png.flaticon.com/512/3081/3081840.png"
                                                 style="width: 100px; opacity: 0.5; margin-bottom: 20px;">
                                            <h4 class="fw-bold text-uppercase">Chưa có đơn hàng nào</h4>
                                            <p class="text-muted">Hãy khám phá sản phẩm và tạo đơn đầu tiên của bạn.</p>
                                            <a href="${ctx}/Product" class="btn-tech-shop">Mua Sắm Ngay</a>
                                        </div>
                                    </div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="row">
                                    <div class="col-12">
                                        <c:forEach var="order" items="${orders}">
                                            <c:set var="statusClass" value="wait" />
                                            <c:set var="statusLabel" value="${order.trangThai}" />
                                            <c:choose>
                                                <c:when test="${order.trangThai == 'ChoXacNhan'}">
                                                    <c:set var="statusClass" value="wait" />
                                                    <c:set var="statusLabel" value="CHỜ XÁC NHẬN" />
                                                </c:when>
                                                <c:when test="${order.trangThai == 'ChoThanhToan'}">
                                                    <c:set var="statusClass" value="wait" />
                                                    <c:set var="statusLabel" value="CHỜ THANH TOÁN" />
                                                </c:when>
                                                <c:when test="${order.trangThai == 'DangGiao'}">
                                                    <c:set var="statusClass" value="shipping" />
                                                    <c:set var="statusLabel" value="ĐANG VẬN CHUYỂN" />
                                                </c:when>
                                                <c:when test="${order.trangThai == 'HoanThanh' or order.trangThai == 'DaGiao'}">
                                                    <c:set var="statusClass" value="done" />
                                                    <c:set var="statusLabel" value="HOÀN THÀNH" />
                                                </c:when>
                                                <c:when test="${order.trangThai == 'Huy'}">
                                                    <c:set var="statusClass" value="cancel" />
                                                    <c:set var="statusLabel" value="ĐÃ HỦY" />
                                                </c:when>
                                            </c:choose>

                                            <div class="order-card">
                                                <div class="order-header">
                                                    <div>
                                                        <i class="bi bi-terminal me-2"></i>
                                                        <span class="order-id"><c:out value="${order.maCodeDonHang}" /></span>
                                                        <span class="order-date"><c:out value="${order.ngayDatDisplay}" /></span>
                                                    </div>
                                                    <span class="status-tag ${statusClass}"><c:out value="${statusLabel}" /></span>
                                                </div>

                                                <div class="order-body">
                                                    <c:forEach var="prod" items="${order.chiTiet}" varStatus="loop">
                                                        <c:if test="${loop.index < 2}">
                                                            <div class="product-preview-row">
                                                                <div class="thumb-box">
                                                                    <img src="${empty prod.hinhAnh ? 'https://via.placeholder.com/80' : prod.hinhAnh}"
                                                                         alt="Product"
                                                                         onerror="this.src='https://via.placeholder.com/80'">
                                                                </div>
                                                                <div class="info-box">
                                                                    <div class="prod-name"><c:out value="${prod.tenSanPham}" /></div>
                                                                    <div class="prod-meta">
                                                                        <span class="badge bg-light text-dark border">x <c:out value="${prod.soLuong}" /></span>
                                                                        <span class="fw-bold ms-2"><fmt:formatNumber value="${prod.thanhTien}" pattern="#,##0" /> đ</span>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </c:if>
                                                    </c:forEach>

                                                    <c:if test="${fn:length(order.chiTiet) > 2}">
                                                        <div class="text-center mt-2 border-top pt-2">
                                                            <small class="text-muted fst-italic">...và còn ${fn:length(order.chiTiet) - 2} sản phẩm khác</small>
                                                        </div>
                                                    </c:if>
                                                </div>

                                                <div class="order-footer">
                                                    <div class="total-section">
                                                        <span class="lbl-total">Tổng thành tiền</span>
                                                        <span class="price-total"><fmt:formatNumber value="${order.tongTien}" pattern="#,##0" /><small>đ</small></span>
                                                    </div>
                                                    <div>
                                                        <a href="${ctx}/DonHang/Detail?id=${order.maDonHang}" class="btn-detail-outline">
                                                            Chi tiết đơn hàng <i class="bi bi-arrow-right-short"></i>
                                                        </a>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<%@ include file="/WEB-INF/views/common/layout-end.jspf" %>
</body>
</html>
