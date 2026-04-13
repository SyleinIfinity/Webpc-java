<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/head-start.jspf" %>
<%@ include file="/WEB-INF/views/common/layout-start.jspf" %>
<div class="row g-4 mb-4">
    <div class="col-md-6 col-xl-3"><div class="card" style="border-left: 5px solid var(--primary-color);"><p style="color:var(--text-muted); margin-bottom:5px;">Nhân viên</p><h3 style="font-size:1.8rem; color:var(--primary-color);">${statsNhanVien}</h3></div></div>
    <div class="col-md-6 col-xl-3"><div class="card" style="border-left: 5px solid var(--success);"><p style="color:var(--text-muted); margin-bottom:5px;">Khách hàng</p><h3 style="font-size:1.8rem; color:var(--success);">${statsKhachHang}</h3></div></div>
    <div class="col-md-6 col-xl-3"><div class="card" style="border-left: 5px solid var(--warning);"><p style="color:var(--text-muted); margin-bottom:5px;">Sản phẩm</p><h3 style="font-size:1.8rem; color:var(--warning);">${statsSanPham}</h3></div></div>
    <div class="col-md-6 col-xl-3"><div class="card" style="border-left: 5px solid var(--danger);"><p style="color:var(--text-muted); margin-bottom:5px;">Tồn kho thấp</p><h3 style="font-size:1.8rem; color:var(--danger);">${statsTonKhoThap}</h3></div></div>
</div>

<div class="card">
    <h4>Phiếu nhập gần nhất</h4>
    <table class="table table-bordered table-striped">
        <thead>
            <tr><th>Mã phiếu</th><th>Nhân viên</th><th>Ngày nhập</th><th>Tổng tiền</th><th>Ghi chú</th></tr>
        </thead>
        <tbody>
            <c:forEach var="item" items="${latestPhieuNhaps}">
                <tr>
                    <td><c:out value="${item.maCodePhieu}" /></td>
                    <td><c:out value="${item.tenNhanVien}" /></td>
                    <td><c:out value="${item.ngayNhapDisplay}" /></td>
                    <td><fmt:formatNumber value="${item.tongTienNhap}" pattern="#,##0" /> đ</td>
                    <td><c:out value="${item.ghiChu}" /></td>
                </tr>
            </c:forEach>
            <c:if test="${empty latestPhieuNhaps}">
                <tr><td colspan="5" class="text-center text-muted">Chưa có dữ liệu phiếu nhập.</td></tr>
            </c:if>
        </tbody>
    </table>
</div>
<%@ include file="/WEB-INF/views/common/layout-end.jspf" %>
