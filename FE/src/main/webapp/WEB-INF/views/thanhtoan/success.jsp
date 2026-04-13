<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/head-start.jspf" %>
<%@ include file="/WEB-INF/views/common/layout-start.jspf" %>

<div class="container py-5 text-center">
    <div class="card shadow-sm border-0 p-5 mx-auto" style="max-width: 600px; border-radius: 15px;">
        <div class="mb-4">
            <i class="bi bi-check-circle-fill text-success" style="font-size: 5rem;"></i>
        </div>

        <h2 class="fw-bold mb-3 text-success">Đặt Hàng Thành Công!</h2>

        <p class="lead text-muted mb-4">
            Cảm ơn bạn đã mua sắm tại TechParts.<br>
            Mã đơn hàng của bạn là: <strong>#<c:out value="${orderId}" /></strong>
        </p>

        <div class="alert alert-info">
            Đơn hàng đang chờ xác nhận. Chúng tôi sẽ liên hệ với bạn sớm nhất để giao hàng.
        </div>

        <div class="d-flex justify-content-center gap-3 mt-4">
            <a href="${ctx}/Product" class="btn btn-outline-secondary px-4">
                <i class="bi bi-arrow-left me-2"></i>Tiếp tục mua sắm
            </a>

            <a href="${ctx}/DonHang" class="btn btn-primary px-4">
                Xem đơn hàng <i class="bi bi-box-seam ms-2"></i>
            </a>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/layout-end.jspf" %>
</body>
</html>
