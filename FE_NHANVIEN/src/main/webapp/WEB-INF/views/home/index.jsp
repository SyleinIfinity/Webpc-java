<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cổng nhân viên WEBPC</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background: linear-gradient(135deg, #0f172a 0%, #1e3a8a 55%, #f8fafc 100%); min-height: 100vh; }
        .hero-card { border: 0; border-radius: 28px; box-shadow: 0 20px 60px rgba(15,23,42,.25); }
    </style>
</head>
<body class="d-flex align-items-center">
    <div class="container py-5">
        <div class="row justify-content-center">
            <div class="col-xl-10">
                <div class="card hero-card overflow-hidden">
                    <div class="row g-0">
                        <div class="col-lg-6 p-5 text-white" style="background: linear-gradient(160deg, #111827 0%, #2563eb 100%);">
                            <h1 class="display-5 fw-bold mb-3">WEBPC Nhân Viên</h1>
                            <p class="lead mb-4">Frontend quản trị được tách riêng từ `WEBPC_NHANVIEN`, đúng theo solution gốc.</p>
                            <ul class="mb-0">
                                <li>Admin: dashboard, khách hàng, nhân viên, sản phẩm, phiếu nhập, thông số</li>
                                <li>Sale: dashboard nền, đang chờ parity đơn hàng và khuyến mãi từ BE</li>
                                <li>Tech: dashboard nền, đang chờ module kỹ thuật chuyên biệt</li>
                            </ul>
                        </div>
                        <div class="col-lg-6 p-5">
                            <h2 class="fw-bold mb-3">Đi tiếp</h2>
                            <p class="text-secondary mb-4">Đăng nhập bằng tài khoản nhân viên để vào đúng khu vực theo vai trò.</p>
                            <a href="${pageContext.request.contextPath}/Account/Login" class="btn btn-primary btn-lg px-4">Đăng nhập hệ thống</a>
                            <div class="mt-4 pt-4 border-top text-secondary small">
                                Backend API đang dùng: <code><c:out value="${backendApiBaseUrl}"/></code>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
