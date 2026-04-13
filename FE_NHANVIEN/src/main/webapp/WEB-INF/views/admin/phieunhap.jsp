<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/head-start.jspf" %>
<%@ include file="/WEB-INF/views/common/layout-start.jspf" %>
<div class="section-card">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h4 class="mb-0">Danh sách phiếu nhập</h4>
        <div class="text-muted small">Đối chiếu trực tiếp với `Areas/Admin/PhieuNhap`.</div>
    </div>
    <table class="table table-striped table-hover">
        <thead>
            <tr><th>Mã phiếu</th><th>Nhân viên</th><th>Ngày nhập</th><th>Tổng tiền</th><th>Số dòng</th><th>Ghi chú</th></tr>
        </thead>
        <tbody>
            <c:forEach var="item" items="${items}">
                <tr>
                    <td><c:out value="${item.maCodePhieu}" /></td>
                    <td><c:out value="${item.tenNhanVien}" /></td>
                    <td><c:out value="${item.ngayNhapDisplay}" /></td>
                    <td><fmt:formatNumber value="${item.tongTienNhap}" pattern="#,##0" /> đ</td>
                    <td>${fn:length(item.chiTiet)}</td>
                    <td><c:out value="${item.ghiChu}" /></td>
                </tr>
            </c:forEach>
            <c:if test="${empty items}">
                <tr><td colspan="6" class="text-center text-muted">Không có dữ liệu phiếu nhập.</td></tr>
            </c:if>
        </tbody>
    </table>
</div>
<%@ include file="/WEB-INF/views/common/layout-end.jspf" %>
