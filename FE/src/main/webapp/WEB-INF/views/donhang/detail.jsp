<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/head-start.jspf" %>
<link href="${ctx}/assets/css/order-detail.css" rel="stylesheet">
<%@ include file="/WEB-INF/views/common/layout-start.jspf" %>

<c:set var="statusClass" value="wait" />
<c:set var="statusLabel" value="${order.trangThai}" />
<c:set var="stepStatus" value="0" />
<c:choose>
    <c:when test="${order.trangThai == 'Huy'}">
        <c:set var="statusClass" value="cancel" />
        <c:set var="statusLabel" value="ĐÃ HỦY" />
        <c:set var="stepStatus" value="-1" />
    </c:when>
    <c:when test="${order.trangThai == 'HoanThanh' or order.trangThai == 'DaGiao'}">
        <c:set var="statusClass" value="done" />
        <c:set var="statusLabel" value="HOÀN THÀNH" />
        <c:set var="stepStatus" value="3" />
    </c:when>
    <c:when test="${order.trangThai == 'DangGiao'}">
        <c:set var="statusClass" value="shipping" />
        <c:set var="statusLabel" value="ĐANG VẬN CHUYỂN" />
        <c:set var="stepStatus" value="2" />
    </c:when>
    <c:when test="${order.trangThai == 'ChoXacNhan'}">
        <c:set var="statusClass" value="wait" />
        <c:set var="statusLabel" value="CHỜ XÁC NHẬN" />
        <c:set var="stepStatus" value="1" />
    </c:when>
    <c:when test="${order.trangThai == 'ChoThanhToan'}">
        <c:set var="statusClass" value="wait" />
        <c:set var="statusLabel" value="CHỜ THANH TOÁN" />
        <c:set var="stepStatus" value="1" />
    </c:when>
</c:choose>

<c:set var="isPaid" value="false" />
<c:if test="${order.trangThai == 'HoanThanh' or order.trangThai == 'DaThanhToan' or order.trangThai == 'DaGiao'}">
    <c:set var="isPaid" value="true" />
</c:if>
<c:forEach var="tx" items="${order.giaoDichs}">
    <c:if test="${tx.trangThai == 'Success'}">
        <c:set var="isPaid" value="true" />
    </c:if>
</c:forEach>

<div class="order-detail-container">
    <div class="container">
        <div class="d-flex flex-wrap justify-content-between align-items-center mb-4">
            <div>
                <a href="${ctx}/DonHang" class="back-link text-decoration-none fw-bold mb-2 d-inline-block">
                    <i class="bi bi-arrow-left"></i> QUAY LẠI DANH SÁCH
                </a>
                <h3 class="fw-bold mb-0 text-uppercase">
                    Đơn hàng <span class="text-primary">#<c:out value="${order.maCodeDonHang}" /></span>
                </h3>
                <span class="text-muted small">Ngày đặt: <c:out value="${order.ngayDatDisplay}" /></span>
            </div>
            <div class="mt-3 mt-md-0">
                <span class="status-tag ${statusClass} px-3 py-2 rounded-pill fw-bold text-white">
                    <c:out value="${statusLabel}" />
                </span>
            </div>
        </div>

        <c:choose>
            <c:when test="${stepStatus != -1}">
                <div class="tracking-wrapper mb-5">
                    <ul class="step-wizard-list">
                        <li class="step-wizard-item ${stepStatus >= 1 ? 'active' : ''}">
                            <span class="progress-count">1</span>
                            <span class="progress-label">Đang xử lý</span>
                        </li>
                        <li class="step-wizard-item ${stepStatus >= 2 ? 'active' : ''}">
                            <span class="progress-count">2</span>
                            <span class="progress-label">Đang vận chuyển</span>
                        </li>
                        <li class="step-wizard-item ${stepStatus >= 3 ? 'active' : ''}">
                            <span class="progress-count"><i class="bi bi-check-lg"></i></span>
                            <span class="progress-label">Giao thành công</span>
                        </li>
                    </ul>
                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-danger text-center fw-bold text-uppercase mb-4">
                    <i class="bi bi-x-octagon-fill me-2"></i> Đơn hàng này đã bị hủy
                </div>
            </c:otherwise>
        </c:choose>

        <div class="row">
            <div class="col-lg-8">
                <div class="card shadow-sm border-0 mb-4">
                    <div class="card-header bg-white py-3">
                        <h5 class="mb-0 fw-bold"><i class="bi bi-bag-check-fill text-primary"></i> Danh sách linh kiện</h5>
                    </div>
                    <div class="card-body">
                        <c:forEach var="item" items="${order.chiTiet}">
                            <div class="d-flex align-items-center mb-3 pb-3 border-bottom">
                                <div class="flex-shrink-0">
                                    <img src="${empty item.hinhAnh ? 'https://via.placeholder.com/80' : item.hinhAnh}"
                                         alt="${fn:escapeXml(item.tenSanPham)}"
                                         class="img-fluid rounded border"
                                         style="width: 80px; height: 80px; object-fit: cover;"
                                         onerror="this.src='https://via.placeholder.com/80'" />
                                </div>
                                <div class="flex-grow-1 ms-3">
                                    <div class="row">
                                        <div class="col-md-8">
                                            <a href="${ctx}/Product/Detail?id=${item.maSanPham}" class="text-decoration-none text-dark fw-bold">
                                                <c:out value="${item.tenSanPham}" />
                                            </a>
                                            <div class="text-muted small mt-1">Số lượng: x <c:out value="${item.soLuong}" /></div>
                                        </div>
                                        <div class="col-md-4 text-md-end">
                                            <span class="text-danger fw-bold"><fmt:formatNumber value="${item.thanhTien}" pattern="#,##0" /> đ</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>

            <div class="col-lg-4">
                <div class="card shadow-sm border-0 mb-3">
                    <div class="card-header bg-white py-3">
                        <h5 class="mb-0 fw-bold"><i class="bi bi-geo-alt-fill text-danger"></i> Địa chỉ nhận hàng</h5>
                    </div>
                    <div class="card-body">
                        <p class="mb-1"><strong><c:out value="${order.nguoiNhan}" /></strong></p>
                        <p class="mb-1 text-muted"><c:out value="${order.soDienThoaiGiao}" /></p>
                        <p class="mb-0 small"><c:out value="${order.diaChiGiaoHang}" /></p>
                    </div>
                </div>

                <div class="card shadow-sm border-0">
                    <div class="card-header bg-white py-3">
                        <h5 class="mb-0 fw-bold"><i class="bi bi-credit-card-2-front-fill text-success"></i> Thanh toán</h5>
                    </div>
                    <div class="card-body">
                        <div class="d-flex justify-content-between mb-2">
                            <span>Phương thức:</span>
                            <c:choose>
                                <c:when test="${order.phuongThucThanhToan == 'COD'}">
                                    <span class="badge bg-secondary">Tiền mặt (COD)</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-primary">VietQR / Chuyển khoản</span>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div class="d-flex justify-content-between mb-3">
                            <span>Trạng thái:</span>
                            <c:choose>
                                <c:when test="${isPaid}">
                                    <span class="fw-bold text-success">Đã thanh toán</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="fw-bold text-danger">Chưa thanh toán</span>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <hr>

                        <div class="d-flex justify-content-between mb-2">
                            <span>Giảm giá:</span>
                            <span class="text-success">-<fmt:formatNumber value="${order.giamGia}" pattern="#,##0" /> đ</span>
                        </div>
                        <div class="d-flex justify-content-between mb-2">
                            <span>Phí vận chuyển:</span>
                            <span><fmt:formatNumber value="${order.phiVanChuyen}" pattern="#,##0" /> đ</span>
                        </div>
                        <div class="d-flex justify-content-between mb-3">
                            <span class="fw-bold fs-5">Tổng cộng:</span>
                            <span class="fw-bold fs-5 text-danger"><fmt:formatNumber value="${order.tongTien}" pattern="#,##0" /> đ</span>
                        </div>

                        <div class="d-grid gap-2">
                            <c:if test="${order.phuongThucThanhToan == 'VietQR' and not isPaid and order.trangThai != 'Huy' and order.trangThai != 'HoanThanh'}">
                                <a href="${ctx}/ThanhToan/Payment?id=${order.maDonHang}" class="btn btn-primary fw-bold">
                                    <i class="bi bi-qr-code"></i> THANH TOÁN NGAY
                                </a>
                            </c:if>

                            <c:if test="${order.trangThai == 'DangGiao'}">
                                <form method="post" action="${ctx}/DonHang/ConfirmReceived" onsubmit="return confirm('Bạn xác nhận đã nhận đủ hàng?');">
                                    <input type="hidden" name="id" value="${order.maDonHang}">
                                    <button type="submit" class="btn btn-success w-100 fw-bold text-white">
                                        <i class="bi bi-check-circle-fill"></i> ĐÃ NHẬN ĐƯỢC HÀNG
                                    </button>
                                </form>
                            </c:if>

                            <c:if test="${order.trangThai == 'ChoXacNhan' or order.trangThai == 'ChoThanhToan'}">
                                <form method="post" action="${ctx}/DonHang/CancelOrder" onsubmit="return confirm('Bạn có chắc muốn hủy đơn hàng này?');">
                                    <input type="hidden" name="id" value="${order.maDonHang}">
                                    <button type="submit" class="btn btn-outline-danger w-100">
                                        <i class="bi bi-x-lg"></i> Hủy đơn hàng
                                    </button>
                                </form>
                            </c:if>

                            <c:if test="${order.trangThai == 'HoanThanh' or order.trangThai == 'DaGiao'}">
                                <div class="alert alert-success text-center py-2 mb-0">
                                    <i class="bi bi-star-fill"></i> Đơn hàng hoàn tất
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>

                <c:if test="${not empty order.giaoDichs}">
                    <div class="card shadow-sm border-0 mt-3">
                        <div class="card-header bg-white py-3">
                            <h6 class="mb-0 fw-bold"><i class="bi bi-clock-history text-primary"></i> Lịch sử giao dịch</h6>
                        </div>
                        <div class="card-body">
                            <c:forEach var="tx" items="${order.giaoDichs}">
                                <div class="d-flex justify-content-between border-bottom py-2">
                                    <div>
                                        <div class="fw-semibold"><c:out value="${tx.phuongThuc}" /></div>
                                        <small class="text-muted"><c:out value="${tx.ngayGiaoDichDisplay}" /></small>
                                    </div>
                                    <div class="text-end">
                                        <div class="fw-bold"><fmt:formatNumber value="${tx.soTien}" pattern="#,##0" /> đ</div>
                                        <small class="badge ${tx.trangThai == 'Success' ? 'bg-success' : 'bg-secondary'}"><c:out value="${tx.trangThai}" /></small>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/layout-end.jspf" %>
</body>
</html>
