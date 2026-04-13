<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/head-start.jspf" %>
<%@ include file="/WEB-INF/views/common/layout-start.jspf" %>

<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-7 col-lg-6">
            <div class="card shadow-lg border-0 rounded-lg">
                <div class="card-header bg-primary text-white text-center py-3">
                    <h4 class="mb-0 fw-bold">Thanh toán qua VietQR</h4>
                </div>
                <div class="card-body p-4">
                    <div class="mb-3 text-center">
                        <span class="badge bg-info text-dark px-3 py-2 fs-6">Đơn hàng #<c:out value="${order.maCodeDonHang}" /></span>
                    </div>
                    <div class="mb-3 text-center">
                        <span class="fs-5 fw-bold text-danger">
                            Tổng tiền: <fmt:formatNumber value="${order.tongTien}" pattern="#,##0" />₫
                        </span>
                    </div>
                    <div class="mb-4 text-center">
                        <span class="text-muted">
                            Vui lòng sử dụng ứng dụng ngân hàng để quét mã QR bên dưới và hoàn tất thanh toán.
                        </span>
                    </div>
                    <div class="qr-container my-4 text-center">
                        <img src="${fn:escapeXml(qrImage)}" class="img-fluid border border-2 p-2 rounded bg-white" alt="Mã QR Thanh Toán" style="max-width: 320px;">
                    </div>
                    <div class="mb-3 text-center">
                        <span class="text-primary fw-bold animate-pulse" id="paymentStatusText">Đang chờ thanh toán...</span>
                        <div class="spinner-border text-primary ms-2" role="status" style="width: 1.5rem; height: 1.5rem;">
                            <span class="visually-hidden">Đang xử lý...</span>
                        </div>
                    </div>
                    <div class="alert alert-warning small text-center">
                        Không chỉnh sửa nội dung chuyển khoản để hệ thống tự động xác nhận đơn hàng.
                    </div>

                    <form method="post" action="${ctx}/ThanhToan/ConfirmPayment" class="text-center mt-3">
                        <input type="hidden" name="orderId" value="${order.maDonHang}">
                        <button type="submit" class="btn btn-outline-success">
                            <i class="bi bi-check-circle"></i> Tôi đã thanh toán
                        </button>
                    </form>
                </div>
                <div class="card-footer bg-light text-center p-3 d-flex justify-content-center gap-2">
                    <a href="${ctx}/Home" class="btn btn-outline-secondary btn-sm">
                        <i class="bi bi-arrow-left"></i> Về trang chủ
                    </a>
                    <a href="${ctx}/DonHang/Detail?id=${order.maDonHang}" class="btn btn-outline-primary btn-sm">
                        <i class="bi bi-box-seam"></i> Xem đơn hàng
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<style>
    .animate-pulse {
        animation: pulse 1.5s infinite;
    }
    @keyframes pulse {
        0% { opacity: 0.6; }
        50% { opacity: 1; }
        100% { opacity: 0.6; }
    }
</style>

<%@ include file="/WEB-INF/views/common/layout-end.jspf" %>
<script>
    (function () {
        var orderId = '${order.maDonHang}';
        var statusEl = document.getElementById('paymentStatusText');
        var hasRedirected = false;

        function redirectSuccess() {
            if (hasRedirected) {
                return;
            }
            hasRedirected = true;
            window.location.href = window.APP_CTX + '/ThanhToan/Success?id=' + orderId;
        }

        function checkPaymentStatus() {
            if (!orderId || hasRedirected) {
                return;
            }
            fetch(window.APP_CTX + '/ThanhToan/CheckStatus?orderId=' + encodeURIComponent(orderId))
                .then(function (response) { return response.json(); })
                .then(function (data) {
                    if (data.status === 'PAID') {
                        if (statusEl) {
                            statusEl.innerText = 'Đã xác nhận thanh toán.';
                            statusEl.classList.remove('text-primary');
                            statusEl.classList.add('text-success');
                        }
                        redirectSuccess();
                    }
                })
                .catch(function () {});
        }

        setInterval(checkPaymentStatus, 3000);
        checkPaymentStatus();
    })();
</script>
</body>
</html>
