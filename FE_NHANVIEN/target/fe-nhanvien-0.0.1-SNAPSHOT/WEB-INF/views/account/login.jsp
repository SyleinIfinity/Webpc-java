<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập hệ thống - WEBPC</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css">
</head>
<body class="auth-page">
    <div class="login-wrapper">
        <div class="login-card">
            <div class="login-left">
                <svg xmlns="http://www.w3.org/2000/svg" width="80" height="80" fill="currentColor" viewBox="0 0 16 16">
                    <path d="M8 1a1 1 0 0 1 1-1h6a1 1 0 0 1 1 1v14a1 1 0 0 1-1 1H9a1 1 0 0 1-1-1V1Zm1 13.5a.5.5 0 1 0 1 0 .5.5 0 0 0-1 0Zm2 0a.5.5 0 1 0 1 0 .5.5 0 0 0-1 0ZM9.5 1a.5.5 0 0 0 0 1h5a.5.5 0 0 0 0-1h-5ZM9 3.5a.5.5 0 0 0 .5.5h5a.5.5 0 0 0 0-1h-5a.5.5 0 0 0-.5.5ZM1.5 2A1.5 1.5 0 0 0 0 3.5v7A1.5 1.5 0 0 0 1.5 12H6v2h4.111a6.331 6.331 0 0 1-.111.493V15h1v-1H2v1h1v-1H1.5a.5.5 0 0 1-.5-.5V3.5a.5.5 0 0 1 .5-.5h4v-1h-4ZM2 3.5v7h4v-7H2Z"/>
                </svg>
                <div class="brand-title">WEB QLLINHKIENPC</div>
                <p class="brand-desc">Hệ thống nội bộ dành cho Admin, Sale và Tech.<br>Đăng nhập để truy cập bảng điều khiển.</p>
                <div class="role-badges">
                    <span class="badge">Quản trị viên</span>
                    <span class="badge">Sales</span>
                    <span class="badge">Kỹ thuật</span>
                </div>
            </div>

            <div class="login-right">
                <div class="login-header">
                    <h3>Chào mừng trở lại</h3>
                    <p>Nhập thông tin tài khoản của bạn.</p>
                </div>

                <c:if test="${not empty flashInfo}">
                    <div class="validation-summary-valid mb-3"><c:out value="${flashInfo}" /></div>
                </c:if>
                <c:if test="${not empty loginError}">
                    <div class="validation-summary-errors mb-3"><c:out value="${loginError}" /></div>
                </c:if>

                <form method="post" action="${pageContext.request.contextPath}/Account/Login">
                    <div class="form-group">
                        <label class="form-label">Tên đăng nhập</label>
                        <input name="TenDangNhap" class="form-input" placeholder="Ví dụ: admin, sale01..." required value="${param.TenDangNhap}">
                    </div>

                    <div class="form-group">
                        <label class="form-label">Mật khẩu</label>
                        <input name="MatKhau" type="password" class="form-input" placeholder="••••••••" required>
                    </div>

                    <button type="submit" class="btn-submit">ĐĂNG NHẬP HỆ THỐNG</button>
                </form>

                <div class="footer-text">&copy; 2026 WEBPC Internal System</div>
            </div>
        </div>
    </div>
</body>
</html>
