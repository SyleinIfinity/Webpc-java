<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/head-start.jspf" %>
<%@ include file="/WEB-INF/views/common/layout-start.jspf" %>
<c:set var="checkout" value="${checkoutModel}" />

<style>
    .checkout-container {
        background-color: #f5f5fa;
        min-height: 100vh;
    }
    .checkout-box {
        background: #fff;
        padding: 20px;
        border-radius: 8px;
        margin-bottom: 20px;
        box-shadow: 0 1px 2px rgba(0,0,0,0.05);
    }
    .section-title {
        font-weight: 600;
        font-size: 1.1rem;
        margin-bottom: 15px;
        text-transform: uppercase;
        color: #333;
    }
    .product-item img {
        width: 60px;
        height: 60px;
        object-fit: cover;
        border-radius: 4px;
        border: 1px solid #eee;
    }
    .payment-option {
        border: 1px solid #ddd;
        border-radius: 6px;
        padding: 12px;
        margin-bottom: 10px;
        cursor: pointer;
        transition: all 0.2s;
        display: flex;
        align-items: center;
        width: 100%;
    }
    .payment-option:hover {
        border-color: #0d6efd;
        background-color: #f8faff;
    }
    .payment-option input[type="radio"] {
        margin-right: 15px;
        transform: scale(1.2);
    }
    .total-row {
        font-size: 1.2rem;
        font-weight: bold;
        color: #d70018;
    }
    .address-select {
        width: 100%;
        padding: 10px;
        border-radius: 6px;
        border: 1px solid #ced4da;
        background-color: #fff;
        font-size: 1rem;
    }
    .form-label {
        font-weight: 500;
        font-size: 0.9rem;
        margin-bottom: 5px;
    }
    .form-control-custom {
        border: 1px solid #ced4da;
        border-radius: 6px;
        padding: 8px 12px;
        width: 100%;
    }
    .modal-overlay {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, 0.5);
        z-index: 1100;
        display: none;
        justify-content: center;
        align-items: center;
    }
    .modal-box {
        background: white;
        width: 90%;
        max-width: 520px;
        border-radius: 12px;
        box-shadow: 0 10px 25px rgba(0,0,0,0.2);
        display: flex;
        flex-direction: column;
        max-height: 90vh;
    }
    .modal-header {
        padding: 15px 20px;
        border-bottom: 1px solid #eee;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
    .modal-title {
        font-weight: 700;
        font-size: 1.1rem;
        color: #333;
    }
    .btn-close-modal {
        background: none;
        border: none;
        font-size: 1.5rem;
        color: #999;
        cursor: pointer;
    }
    .modal-body {
        padding: 20px;
        overflow-y: auto;
        background-color: #f8f9fa;
    }
    .modal-footer {
        padding: 15px 20px;
        border-top: 1px solid #eee;
        display: flex;
        justify-content: flex-end;
        gap: 10px;
        background: white;
    }
    .voucher-card {
        background: white;
        border: 1px solid #ddd;
        border-radius: 8px;
        padding: 15px;
        margin-bottom: 15px;
        display: flex;
        align-items: center;
        transition: all 0.2s;
        cursor: pointer;
    }
    .voucher-card:hover {
        border-color: #0d6efd;
        box-shadow: 0 2px 8px rgba(13, 110, 253, 0.1);
    }
    .voucher-img {
        width: 60px;
        height: 60px;
        background-color: #e6f0ff;
        color: #0d6efd;
        display: flex;
        align-items: center;
        justify-content: center;
        border-radius: 6px;
        font-size: 1.5rem;
        margin-right: 15px;
        flex-shrink: 0;
    }
    .voucher-info {
        flex-grow: 1;
    }
    .voucher-code-tag {
        font-weight: bold;
        color: #d70018;
        font-size: 0.9rem;
    }
    .voucher-desc {
        font-size: 0.85rem;
        color: #666;
        margin-top: 2px;
    }
</style>

<div class="checkout-container py-4">
    <div class="container">
        <form method="post" action="${ctx}/ThanhToan/PlaceOrder" id="form-checkout">
            <input type="hidden" name="selectedIdsString" value="${fn:escapeXml(checkout.selectedIdsString)}">
            <input type="hidden" name="MaCodeVoucher" id="hdfMaCodeVoucher" value="">

            <div class="row">
                <div class="col-md-7">
                    <div class="checkout-box">
                        <h5 class="section-title">Thông tin nhận hàng</h5>

                        <div class="mb-3">
                            <label class="form-label text-muted">Sổ địa chỉ:</label>
                            <c:choose>
                                <c:when test="${not empty checkout.addresses}">
                                    <select class="address-select form-select" id="ddlAddress" name="diaChiGiaoHang" onchange="onAddressChange(this)">
                                        <c:forEach var="addr" items="${checkout.addresses}">
                                            <option value="${fn:escapeXml(addr.fullAddress)}"
                                                    data-name="${fn:escapeXml(addr.tenNguoiNhan)}"
                                                    data-phone="${fn:escapeXml(addr.soDienThoai)}">
                                                <c:out value="${addr.tenNguoiNhan}" /> - <c:out value="${addr.soDienThoai}" /> (<c:out value="${addr.fullAddress}" />)
                                            </option>
                                        </c:forEach>
                                    </select>
                                    <div class="mt-2 text-end">
                                        <a href="${ctx}/InfoAddress" class="small text-decoration-none">
                                            <i class="bi bi-plus-circle"></i> Quản lý sổ địa chỉ
                                        </a>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="alert alert-warning mb-3">
                                        Bạn chưa có địa chỉ lưu sẵn. Hãy nhập địa chỉ nhận hàng thủ công bên dưới.
                                    </div>
                                    <input type="text"
                                           class="form-control mb-2"
                                           name="diaChiGiaoHang"
                                           id="txtDiaChiGiaoHang"
                                           placeholder="Nhập địa chỉ giao hàng cụ thể..."
                                           value="${fn:escapeXml(checkout.diaChiGiaoHang)}"
                                           required>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label class="form-label">Người nhận <span class="text-danger">*</span></label>
                                <input name="nguoiNhan"
                                       id="txtNguoiNhan"
                                       class="form-control-custom"
                                       value="${fn:escapeXml(checkout.nguoiNhan)}"
                                       placeholder="Họ tên người nhận"
                                       required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Số điện thoại <span class="text-danger">*</span></label>
                                <input name="soDienThoai"
                                       id="txtSoDienThoai"
                                       class="form-control-custom"
                                       value="${fn:escapeXml(checkout.soDienThoai)}"
                                       placeholder="Số điện thoại liên hệ"
                                       required>
                            </div>
                        </div>
                    </div>

                    <div class="checkout-box">
                        <h5 class="section-title">Phương thức thanh toán</h5>
                        <label class="payment-option">
                            <input type="radio" name="phuongThucThanhToan" value="VietQR" ${checkout.phuongThucThanhToan == 'VietQR' ? 'checked' : ''}>
                            <div class="d-flex flex-column ms-2">
                                <span class="fw-bold">Thanh toán VietQR</span>
                                <span class="small text-muted">Quét mã QR qua ứng dụng ngân hàng</span>
                            </div>
                            <i class="bi bi-qr-code fs-4 text-primary ms-auto"></i>
                        </label>
                        <label class="payment-option">
                            <input type="radio" name="phuongThucThanhToan" value="COD" ${checkout.phuongThucThanhToan != 'VietQR' ? 'checked' : ''}>
                            <div class="d-flex flex-column ms-2">
                                <span class="fw-bold">Thanh toán khi nhận hàng (COD)</span>
                                <span class="small text-muted">Thanh toán tiền mặt khi giao hàng</span>
                            </div>
                            <i class="bi bi-cash-coin text-success fs-3 ms-auto"></i>
                        </label>
                    </div>
                </div>

                <div class="col-md-5">
                    <div class="checkout-box">
                        <h5 class="section-title mb-2">Đơn hàng của bạn</h5>
                        <hr class="my-2">

                        <div class="checkout-product-list mb-4" style="max-height: 300px; overflow-y: auto;">
                            <c:forEach var="item" items="${checkout.cart.items}">
                                <div class="product-item d-flex align-items-center mb-3 pt-2">
                                    <img src="${empty item.productImage ? 'https://via.placeholder.com/60?text=NoImage' : item.productImage}"
                                         class="me-3"
                                         alt="${fn:escapeXml(item.productName)}"
                                         onerror="this.src='https://via.placeholder.com/60?text=Error'">
                                    <div class="flex-grow-1">
                                        <h6 class="mb-0 text-truncate" style="max-width: 200px;" title="${fn:escapeXml(item.productName)}">
                                            <c:out value="${item.productName}" />
                                        </h6>
                                        <div class="text-muted small">x <c:out value="${item.quantity}" /></div>
                                    </div>
                                    <div class="fw-bold"><fmt:formatNumber value="${item.total}" pattern="#,##0" />₫</div>
                                </div>
                            </c:forEach>
                        </div>

                        <hr>

                        <div class="mb-4">
                            <label class="form-label fw-bold"><i class="bi bi-ticket-perforated text-warning me-1"></i> WebPC Voucher</label>
                            <div class="d-flex gap-2">
                                <input type="text" class="form-control" placeholder="Chưa chọn mã" id="displayVoucherInput" readonly style="background-color: white;">
                                <button type="button" class="btn btn-outline-primary" onclick="openVoucherModal()">
                                    <i class="bi bi-list-ul"></i> Chọn mã
                                </button>
                            </div>
                            <div id="appliedVoucherInfo" class="mt-2 p-2 bg-light border border-success rounded text-success small" style="display:none;"></div>
                        </div>

                        <hr>

                        <div class="d-flex justify-content-between mb-2">
                            <span class="text-muted">Tạm tính</span>
                            <span id="subTotal"><fmt:formatNumber value="${checkout.tamTinh}" pattern="#,##0" />₫</span>
                        </div>
                        <div class="d-flex justify-content-between mb-2">
                            <span class="text-muted">Phí vận chuyển</span>
                            <span id="shippingFee"><fmt:formatNumber value="${checkout.phiVanChuyen}" pattern="#,##0" />₫</span>
                        </div>
                        <div class="d-flex justify-content-between mb-2">
                            <span class="text-success">Giảm giá</span>
                            <span class="text-success fw-bold" id="discountAmount">-0₫</span>
                        </div>

                        <div class="d-flex justify-content-between align-items-center mt-3 pt-3 border-top">
                            <span class="fs-5 fw-bold">Tổng thanh toán</span>
                            <span class="total-row" id="finalTotal"><fmt:formatNumber value="${checkout.tongThanhToan}" pattern="#,##0" />₫</span>
                        </div>

                        <button type="submit" class="btn btn-primary w-100 py-3 mt-4 text-uppercase fw-bold fs-6">
                            Đặt hàng ngay
                        </button>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>

<div id="voucherModal" class="modal-overlay">
    <div class="modal-box">
        <div class="modal-header">
            <div class="modal-title">Ví Voucher của bạn</div>
            <button class="btn-close-modal" type="button" onclick="closeVoucherModal()">&times;</button>
        </div>
        <div class="modal-body">
            <c:choose>
                <c:when test="${not empty checkout.danhSachKhuyenMai}">
                    <c:forEach var="km" items="${checkout.danhSachKhuyenMai}">
                        <label class="voucher-card">
                            <div class="voucher-img">
                                <i class="bi bi-gift-fill text-primary"></i>
                            </div>
                            <div class="voucher-info">
                                <div class="voucher-code-tag"><c:out value="${km.maCodeKM}" /></div>
                                <div class="fw-bold"><c:out value="${km.tenChuongTrinh}" /></div>
                                <div class="voucher-desc">
                                    <c:choose>
                                        <c:when test="${km.loaiGiam == 'DIRECT'}">
                                            <span>Giảm trực tiếp <fmt:formatNumber value="${km.giaTriGiam}" pattern="#,##0" />đ</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span>Giảm <fmt:formatNumber value="${km.giaTriGiam}" pattern="#,##0" />% (Tối đa <fmt:formatNumber value="${km.giamToiDa}" pattern="#,##0" />đ)</span>
                                        </c:otherwise>
                                    </c:choose>
                                    <br>
                                    <span class="text-muted" style="font-size: 11px;">
                                        Đơn tối thiểu: <fmt:formatNumber value="${km.donHangToiThieu}" pattern="#,##0" />đ
                                    </span>
                                </div>
                            </div>

                            <input type="radio"
                                   name="modalVoucherSelect"
                                   class="voucher-radio"
                                   value="${km.maCodeKM}"
                                   data-code="${km.maCodeKM}"
                                   data-type="${km.loaiGiam}"
                                   data-value="${km.giaTriGiam}"
                                   data-max="${empty km.giamToiDa ? 0 : km.giamToiDa}">
                        </label>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="text-center py-5">
                        <p class="mt-3 text-muted fw-bold">Chưa có voucher phù hợp với đơn hàng này.</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="modal-footer">
            <button class="btn btn-secondary" type="button" onclick="closeVoucherModal()">Đóng</button>
            <button class="btn btn-primary px-4" type="button" onclick="applyVoucher()">Áp dụng</button>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/layout-end.jspf" %>
<script>
    var originalSubTotal = parseFloat('${checkout.tamTinh}');
    var shippingFee = parseFloat('${checkout.phiVanChuyen}');

    function onAddressChange(selectElement) {
        if (!selectElement || selectElement.selectedIndex < 0) {
            return;
        }
        var selectedOption = selectElement.options[selectElement.selectedIndex];
        var name = selectedOption.getAttribute('data-name');
        var phone = selectedOption.getAttribute('data-phone');
        if (name) {
            var txtName = document.getElementById('txtNguoiNhan');
            if (txtName) {
                txtName.value = name;
            }
        }
        if (phone) {
            var txtPhone = document.getElementById('txtSoDienThoai');
            if (txtPhone) {
                txtPhone.value = phone;
            }
        }
    }

    function openVoucherModal() {
        var modal = document.getElementById('voucherModal');
        if (modal) {
            modal.style.display = 'flex';
            document.body.style.overflow = 'hidden';
        }
    }

    function closeVoucherModal() {
        var modal = document.getElementById('voucherModal');
        if (modal) {
            modal.style.display = 'none';
            document.body.style.overflow = 'auto';
        }
    }

    function formatCurrency(amount) {
        return new Intl.NumberFormat('vi-VN', { maximumFractionDigits: 0 }).format(amount) + '₫';
    }

    function applyVoucher() {
        var radios = document.getElementsByName('modalVoucherSelect');
        var selectedRadio = null;
        for (var i = 0; i < radios.length; i += 1) {
            if (radios[i].checked) {
                selectedRadio = radios[i];
                break;
            }
        }
        if (!selectedRadio) {
            alert('Vui lòng chọn một mã giảm giá.');
            return;
        }

        var maCode = selectedRadio.getAttribute('data-code');
        var type = (selectedRadio.getAttribute('data-type') || '').toUpperCase();
        var value = parseFloat((selectedRadio.getAttribute('data-value') || '0').replace(',', '.')) || 0;
        var maxDiscount = parseFloat((selectedRadio.getAttribute('data-max') || '0').replace(',', '.')) || 0;

        var discountValue = 0;
        if (type === 'DIRECT') {
            discountValue = value;
        } else {
            discountValue = (originalSubTotal * value) / 100;
            if (maxDiscount > 0 && discountValue > maxDiscount) {
                discountValue = maxDiscount;
            }
        }
        if (discountValue > originalSubTotal) {
            discountValue = originalSubTotal;
        }

        var finalTotal = originalSubTotal + shippingFee - discountValue;
        if (finalTotal < 0) {
            finalTotal = 0;
        }

        document.getElementById('displayVoucherInput').value = maCode;
        document.getElementById('hdfMaCodeVoucher').value = maCode;
        document.getElementById('discountAmount').innerText = '-' + formatCurrency(discountValue);
        document.getElementById('finalTotal').innerText = formatCurrency(finalTotal);

        var infoDiv = document.getElementById('appliedVoucherInfo');
        if (infoDiv) {
            infoDiv.innerHTML = '<i class="bi bi-check-circle"></i> Đã áp dụng <b>' + maCode + '</b>: Giảm -' + formatCurrency(discountValue);
            infoDiv.style.display = 'block';
        }
        closeVoucherModal();
    }

    document.addEventListener('DOMContentLoaded', function () {
        var ddlAddress = document.getElementById('ddlAddress');
        if (ddlAddress && ddlAddress.options.length > 0) {
            onAddressChange(ddlAddress);
        }

        var voucherModal = document.getElementById('voucherModal');
        window.addEventListener('click', function (event) {
            if (event.target === voucherModal) {
                closeVoucherModal();
            }
        });
    });
</script>
</body>
</html>
