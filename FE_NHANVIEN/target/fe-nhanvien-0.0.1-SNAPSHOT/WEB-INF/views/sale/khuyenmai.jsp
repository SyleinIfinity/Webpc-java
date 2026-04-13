<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/head-start.jspf" %>
<%@ include file="/WEB-INF/views/common/layout-start.jspf" %>
<div class="section-card">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <div>
            <h4 class="mb-0">Khuyến mãi</h4>
            <div class="text-muted small">Dữ liệu lấy từ <code>api/KhuyenMai</code>. Bạn có thể cập nhật ngày bắt đầu, kết thúc ngay trên bảng.</div>
        </div>
        <span class="text-muted small">${fn:length(items)} chương trình</span>
    </div>

    <table class="table table-striped table-hover">
        <thead>
            <tr>
                <th>ID</th>
                <th>Code</th>
                <th>Chương trình</th>
                <th>Giảm</th>
                <th>Đơn tối thiểu</th>
                <th>Giảm tối đa</th>
                <th>Bắt đầu</th>
                <th>Kết thúc</th>
                <th>Còn lại</th>
                <th style="min-width: 260px;">Cập nhật</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="item" items="${items}">
                <tr>
                    <td>${item.maKhuyenMai}</td>
                    <td class="fw-bold"><c:out value="${item.maCodeKM}" /></td>
                    <td>
                        <div class="fw-bold"><c:out value="${item.tenChuongTrinh}" /></div>
                        <div class="text-muted small"><c:out value="${item.loaiGiam}" /></div>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${item.loaiGiam == 'PERCENT'}">
                                <c:out value="${item.giaTriGiam}" />%
                            </c:when>
                            <c:otherwise>
                                <fmt:formatNumber value="${item.giaTriGiam}" pattern="#,##0" /> đ
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td><fmt:formatNumber value="${empty item.donHangToiThieu ? 0 : item.donHangToiThieu}" pattern="#,##0" /> đ</td>
                    <td>
                        <c:choose>
                            <c:when test="${not empty item.giamToiDa}">
                                <fmt:formatNumber value="${item.giamToiDa}" pattern="#,##0" /> đ
                            </c:when>
                            <c:otherwise><span class="text-muted">-</span></c:otherwise>
                        </c:choose>
                    </td>
                    <td><c:out value="${item.ngayBatDauDisplay}" /></td>
                    <td><c:out value="${item.ngayKetThucDisplay}" /></td>
                    <td>${empty item.soLuongConLai ? 0 : item.soLuongConLai}</td>
                    <td>
                        <form method="post" action="${ctx}/Sale/KhuyenMai" class="d-flex gap-2 align-items-center">
                            <input type="hidden" name="maKhuyenMai" value="${item.maKhuyenMai}" />
                            <input type="hidden" name="maCodeKM" value="${item.maCodeKM}" />
                            <input type="hidden" name="tenChuongTrinh" value="${item.tenChuongTrinh}" />
                            <input type="hidden" name="loaiGiam" value="${item.loaiGiam}" />
                            <input type="hidden" name="giaTriGiam" value="${empty item.giaTriGiam ? '0' : item.giaTriGiam}" />
                            <input type="hidden" name="donHangToiThieu" value="${empty item.donHangToiThieu ? '0' : item.donHangToiThieu}" />
                            <input type="hidden" name="giamToiDa" value="${empty item.giamToiDa ? '' : item.giamToiDa}" />
                            <input type="hidden" name="soLuongConLai" value="${empty item.soLuongConLai ? '0' : item.soLuongConLai}" />

                            <input type="date" class="form-control form-control-sm" name="ngayBatDau" value="${item.ngayBatDauIso}" />
                            <input type="date" class="form-control form-control-sm" name="ngayKetThuc" value="${item.ngayKetThucIso}" />
                            <button type="submit" class="btn btn-sm btn-primary">Lưu</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty items}">
                <tr><td colspan="10" class="text-center text-muted">Chưa có dữ liệu khuyến mãi.</td></tr>
            </c:if>
        </tbody>
    </table>
</div>
<%@ include file="/WEB-INF/views/common/layout-end.jspf" %>
