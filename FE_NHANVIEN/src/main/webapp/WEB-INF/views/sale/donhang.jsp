<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/head-start.jspf" %>
<%@ include file="/WEB-INF/views/common/layout-start.jspf" %>
<div class="row g-4">
    <div class="col-lg-7">
        <div class="section-card">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <div>
                    <h4 class="mb-0">Danh sách đơn hàng</h4>
                    <div class="text-muted small">Dữ liệu lấy từ <code>api/DonHang</code>.</div>
                </div>
                <span class="text-muted small">${fn:length(orders)} đơn</span>
            </div>

            <table class="table table-striped table-hover">
                <thead>
                    <tr>
                        <th>Mã</th>
                        <th>Khách</th>
                        <th>Ngày đặt</th>
                        <th>Thanh toán</th>
                        <th>Tổng tiền</th>
                        <th>Trạng thái</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="row" items="${orders}">
                        <tr class="${not empty selectedOrder && selectedOrder.maDonHang == row.maDonHang ? 'table-primary' : ''}">
                            <td class="fw-bold"><c:out value="${row.maCodeDonHang}" /></td>
                            <td>
                                <div class="fw-bold"><c:out value="${empty row.tenKhachHang ? ('KH#' + row.maKhachHang) : row.tenKhachHang}" /></div>
                                <div class="text-muted small">ID: ${row.maKhachHang}</div>
                            </td>
                            <td><c:out value="${row.ngayDatDisplay}" /></td>
                            <td><c:out value="${row.phuongThucThanhToan}" /></td>
                            <td><fmt:formatNumber value="${row.tongTien}" pattern="#,##0" /> đ</td>
                            <td><span class="status-pill ok"><c:out value="${row.trangThai}" /></span></td>
                            <td class="text-end">
                                <a class="btn btn-sm btn-outline-primary" href="${ctx}/Sale/DonHang?id=${row.maDonHang}">Xem</a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty orders}">
                        <tr><td colspan="7" class="text-center text-muted">Chưa có đơn hàng.</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>

    <div class="col-lg-5">
        <div class="section-card">
            <h4 class="mb-3">Chi tiết đơn hàng</h4>
            <c:choose>
                <c:when test="${not empty selectedOrder}">
                    <div class="feature-box mb-3">
                        <div class="d-flex justify-content-between">
                            <div class="fw-bold"><c:out value="${selectedOrder.maCodeDonHang}" /></div>
                            <span class="status-pill ok"><c:out value="${selectedOrder.trangThai}" /></span>
                        </div>
                        <div class="text-muted small">Ngày đặt: <c:out value="${selectedOrder.ngayDatDisplay}" /></div>
                        <div class="text-muted small">Thanh toán: <c:out value="${selectedOrder.phuongThucThanhToan}" /></div>
                        <div class="text-muted small">Khách hàng ID: ${selectedOrder.maKhachHang}</div>
                    </div>

                    <div class="mb-3">
                        <div class="fw-bold mb-2">Thông tin giao hàng</div>
                        <div class="text-muted small">Người nhận: <c:out value="${selectedOrder.nguoiNhan}" /></div>
                        <div class="text-muted small">SĐT: <c:out value="${selectedOrder.soDienThoaiGiao}" /></div>
                        <div class="text-muted small">Địa chỉ: <c:out value="${selectedOrder.diaChiGiaoHang}" /></div>
                    </div>

                    <div class="mb-3">
                        <div class="fw-bold mb-2">Sản phẩm</div>
                        <c:forEach var="ct" items="${selectedOrder.chiTiet}">
                            <div class="d-flex align-items-center gap-2 border rounded-3 p-2 mb-2">
                                <c:if test="${not empty ct.hinhAnh}">
                                    <img src="${ct.hinhAnh}" alt="" style="width:48px;height:48px;object-fit:cover;border-radius:10px;border:1px solid #e2e8f0;" />
                                </c:if>
                                <div class="flex-grow-1">
                                    <div class="fw-bold"><c:out value="${ct.tenSanPham}" /></div>
                                    <div class="text-muted small">SL: ${ct.soLuong} | Đơn giá: <fmt:formatNumber value="${ct.donGiaLucMua}" pattern="#,##0" /> đ</div>
                                </div>
                                <div class="fw-bold text-danger"><fmt:formatNumber value="${ct.thanhTien}" pattern="#,##0" /> đ</div>
                            </div>
                        </c:forEach>
                        <c:if test="${empty selectedOrder.chiTiet}">
                            <div class="text-muted small">Không có chi tiết sản phẩm.</div>
                        </c:if>
                    </div>

                    <div class="mb-3">
                        <div class="fw-bold mb-2">Thanh toán</div>
                        <div class="d-flex justify-content-between text-muted small"><span>Phí vận chuyển</span><span><fmt:formatNumber value="${selectedOrder.phiVanChuyen}" pattern="#,##0" /> đ</span></div>
                        <div class="d-flex justify-content-between fw-bold"><span>Tổng tiền</span><span class="text-danger"><fmt:formatNumber value="${selectedOrder.tongTien}" pattern="#,##0" /> đ</span></div>
                    </div>

                    <div class="mb-3">
                        <div class="fw-bold mb-2">Giao dịch</div>
                        <c:forEach var="gd" items="${selectedOrder.giaoDichs}">
                            <div class="d-flex justify-content-between border rounded-3 p-2 mb-2">
                                <div>
                                    <div class="fw-bold">#${gd.maGiaoDich} <c:out value="${gd.phuongThuc}" /></div>
                                    <div class="text-muted small"><c:out value="${gd.ngayGiaoDichDisplay}" /></div>
                                </div>
                                <div class="text-end">
                                    <div class="fw-bold"><fmt:formatNumber value="${gd.soTien}" pattern="#,##0" /> đ</div>
                                    <div class="text-muted small"><c:out value="${gd.trangThai}" /></div>
                                </div>
                            </div>
                        </c:forEach>
                        <c:if test="${empty selectedOrder.giaoDichs}">
                            <div class="text-muted small">Chưa có giao dịch.</div>
                        </c:if>
                    </div>

                    <div class="d-flex gap-2">
                        <form method="post" action="${ctx}/Sale/DonHang" class="d-inline">
                            <input type="hidden" name="action" value="approve" />
                            <input type="hidden" name="id" value="${selectedOrder.maDonHang}" />
                            <button type="submit" class="btn btn-success">Duyệt</button>
                        </form>

                        <form method="post" action="${ctx}/Sale/DonHang" class="d-inline flex-grow-1">
                            <input type="hidden" name="action" value="reject" />
                            <input type="hidden" name="id" value="${selectedOrder.maDonHang}" />
                            <div class="input-group">
                                <input type="text" name="reason" class="form-control" placeholder="Lý do hủy" />
                                <button type="submit" class="btn btn-outline-danger">Hủy</button>
                            </div>
                        </form>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="text-muted">Chọn một đơn hàng ở danh sách để xem chi tiết.</div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/views/common/layout-end.jspf" %>
