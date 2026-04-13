<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/head-start.jspf" %>
<style>
    .register-box {
        max-width: 500px;
        margin: 40px auto;
        padding: 30px;
        background: #fff;
        border-radius: 8px;
        box-shadow: 0 0 15px rgba(0,0,0,0.1);
    }
    .register-title { text-align: center; margin-bottom: 20px; color: #333; font-weight: bold; }
    .form-control { height: 45px; }
    .btn-register { padding: 10px; font-size: 18px; font-weight: bold; }
</style>
<%@ include file="/WEB-INF/views/common/layout-start.jspf" %>

<div class="register-box">
    <h2 class="register-title">Đăng ký thành viên</h2>
    <hr>

    <c:if test="${not empty errors}">
        <div class="alert alert-danger">
            <ul class="mb-0 ps-3">
                <c:forEach var="error" items="${errors}">
                    <li><c:out value="${error}" /></li>
                </c:forEach>
            </ul>
        </div>
    </c:if>

    <form method="post" action="${ctx}/Register">
        <div class="form-group mb-3">
            <label class="form-label">Họ và tên</label>
            <input name="HoTen" class="form-control" placeholder="Nhập họ và tên" value="${fn:escapeXml(hoTen)}">
        </div>
        <div class="form-group mb-3">
            <label class="form-label">Số điện thoại</label>
            <input name="SoDienThoai" class="form-control" placeholder="Nhập số điện thoại" value="${fn:escapeXml(soDienThoai)}">
        </div>
        <div class="form-group mb-3">
            <label class="form-label">Email</label>
            <input name="Email" type="email" class="form-control" placeholder="email@example.com" value="${fn:escapeXml(email)}">
        </div>
        <div class="form-group mb-3">
            <label class="form-label">Tên đăng nhập</label>
            <input name="TenDangNhap" class="form-control" placeholder="Tên đăng nhập" value="${fn:escapeXml(tenDangNhap)}">
        </div>
        <div class="form-group mb-3">
            <label class="form-label">Mật khẩu</label>
            <input name="MatKhau" type="password" class="form-control" placeholder="Nhập mật khẩu">
        </div>
        <div class="form-group mb-4">
            <label class="form-label">Xác nhận mật khẩu</label>
            <input name="XacNhanMatKhau" type="password" class="form-control" placeholder="Nhập lại mật khẩu">
        </div>
        <button type="submit" class="btn btn-primary btn-register w-100">Đăng Ký</button>
        <div class="text-center mt-3">
            <p>Đã có tài khoản? <a href="${ctx}/Login" class="text-primary">Đăng nhập ngay</a></p>
        </div>
    </form>
</div>

<%@ include file="/WEB-INF/views/common/layout-end.jspf" %>
</body>
</html>
