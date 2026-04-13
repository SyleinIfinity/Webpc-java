<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/head-start.jspf" %>
<%@ include file="/WEB-INF/views/common/layout-start.jspf" %>
<div class="section-card">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h4 class="mb-0">Danh sách nhân viên</h4>
        <div class="text-muted small">Pha này mới port màn hình đọc dữ liệu. Create/Edit/Delete sẽ làm ở phase sau.</div>
    </div>
    <table class="table table-striped table-hover">
        <thead>
            <tr><th>Mã NV</th><th>Họ tên</th><th>SĐT</th><th>Vai trò</th><th>Tài khoản</th><th>Email</th><th>Trạng thái</th></tr>
        </thead>
        <tbody>
            <c:forEach var="item" items="${items}">
                <tr>
                    <td><c:out value="${item.maCodeNhanVien}" /></td>
                    <td><c:out value="${item.hoTen}" /></td>
                    <td><c:out value="${item.soDienThoai}" /></td>
                    <td><c:out value="${item.tenVaiTro}" /></td>
                    <td><c:out value="${item.tenDangNhap}" /></td>
                    <td><c:out value="${item.email}" /></td>
                    <td>
                        <span class="status-pill ${item.trangThaiTaiKhoan == 'Active' ? 'ok' : 'off'}">
                            <c:out value="${item.trangThaiTaiKhoan}" />
                        </span>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty items}">
                <tr><td colspan="7" class="text-center text-muted">Không có dữ liệu nhân viên.</td></tr>
            </c:if>
        </tbody>
    </table>
</div>
<%@ include file="/WEB-INF/views/common/layout-end.jspf" %>
