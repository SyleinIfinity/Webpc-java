<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/head-start.jspf" %>
<%@ include file="/WEB-INF/views/common/layout-start.jspf" %>
<div class="section-card">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h4 class="mb-0">Danh sách khách hàng</h4>
        <div class="text-muted small">Tương ứng `Areas/Admin/KhachHang` của source gốc.</div>
    </div>
    <table class="table table-striped table-hover">
        <thead>
            <tr><th>Mã KH</th><th>Họ tên</th><th>SĐT</th><th>Email</th><th>Tài khoản</th><th>Có tài khoản</th></tr>
        </thead>
        <tbody>
            <c:forEach var="item" items="${items}">
                <tr>
                    <td>${item.maKhachHang}</td>
                    <td><c:out value="${item.hoTen}" /></td>
                    <td><c:out value="${item.soDienThoai}" /></td>
                    <td><c:out value="${item.email}" /></td>
                    <td><c:out value="${item.tenDangNhap}" /></td>
                    <td>
                        <span class="status-pill ${item.coTaiKhoan ? 'ok' : 'off'}">
                            ${item.coTaiKhoan ? 'Có' : 'Không'}
                        </span>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty items}">
                <tr><td colspan="6" class="text-center text-muted">Không có dữ liệu khách hàng.</td></tr>
            </c:if>
        </tbody>
    </table>
</div>
<%@ include file="/WEB-INF/views/common/layout-end.jspf" %>
