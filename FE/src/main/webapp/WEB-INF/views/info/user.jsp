<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/head-start.jspf" %>
<link href="${ctx}/assets/css/infouser.css" rel="stylesheet">
<%@ include file="/WEB-INF/views/common/layout-start.jspf" %>

<section class="py-5 bg-light min-vh-100">
    <div class="container">
        <h2 class="mb-4 fw-bold">Tài Khoản Của Tôi</h2>
        <div class="row g-4">
            <div class="col-lg-3">
                <div class="card p-3 shadow-sm">
                    <div class="d-flex align-items-center border-bottom pb-3 mb-3">
                        <img src="https://via.placeholder.com/60" class="rounded-circle me-3 border" alt="Avatar" style="width:60px; height:60px; object-fit:cover;">
                        <div class="overflow-hidden">
                            <p class="mb-0 fw-bold">Xin chào,</p>
                            <p class="mb-0 text-primary fw-semibold text-truncate"><c:out value="${sessionScope.UserName}" /></p>
                        </div>
                    </div>
                    <ul class="nav nav-pills flex-column user-dashboard-nav">
                        <li class="nav-item"><a class="nav-link active-info" href="${ctx}/InfoUser"><i class="bi bi-person-circle me-2"></i>Thông tin cá nhân</a></li>
                        <li class="nav-item"><a class="nav-link" href="${ctx}/InfoAddress"><i class="bi bi-geo-alt me-2"></i>Sổ địa chỉ</a></li>
                        <li class="nav-item mt-3"><a class="nav-link text-danger" href="${ctx}/Login/Logout"><i class="bi bi-box-arrow-right me-2"></i>Đăng xuất</a></li>
                    </ul>
                </div>
            </div>

            <div class="col-lg-9">
                <div class="card p-4 shadow-sm">
                    <h4 class="mb-4 fw-bold border-bottom pb-2">Thông tin cá nhân</h4>
                    <div class="row g-4">
                        <div class="col-md-6">
                            <label class="form-label fw-semibold">Họ và Tên</label>
                            <input type="text" class="form-control form-control-lg" value="${fn:escapeXml(userProfile.hoTen)}" readonly>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label fw-semibold">Email</label>
                            <input type="email" class="form-control form-control-lg" value="${fn:escapeXml(userProfile.email)}" readonly>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label fw-semibold">Số điện thoại</label>
                            <input type="tel" class="form-control form-control-lg" value="${fn:escapeXml(userProfile.soDienThoai)}" readonly>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label fw-semibold">Tên đăng nhập</label>
                            <input type="text" class="form-control form-control-lg" value="${fn:escapeXml(userProfile.tenDangNhap)}" readonly>
                        </div>
                        <div class="col-12">
                            <button type="button" class="btn btn-secondary btn-lg mt-3" disabled>Tính năng cập nhật đang bảo trì</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<%@ include file="/WEB-INF/views/common/layout-end.jspf" %>
</body>
</html>
