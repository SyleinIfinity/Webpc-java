<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/head-start.jspf" %>
<link href="${ctx}/assets/css/build-pc.css" rel="stylesheet">
<%@ include file="/WEB-INF/views/common/layout-start.jspf" %>

<section class="config-header pt-4 pb-3 bg-white border-bottom sticky-top" style="top: 0; z-index: 100;">
    <div class="container">
        <div class="d-flex justify-content-between align-items-center">
            <h4 class="fw-bold m-0 text-primary"><i class="bi bi-pc-display-horizontal me-2"></i>Xây Dựng Cấu Hình</h4>
            <button class="btn btn-outline-danger btn-sm" onclick="resetConfig()">
                <i class="bi bi-arrow-counterclockwise"></i> Làm mới
            </button>
        </div>
    </div>
</section>

<section class="py-4 bg-light" style="min-height: 80vh;">
    <div class="container">
        <div class="row">
            <div class="col-lg-8 mb-4">
                <div class="card border-0 shadow-sm rounded-3">
                    <div class="card-body p-0">
                        <c:forEach var="cat" items="${categories}" varStatus="status">
                            <div class="config-item-row p-3 border-bottom transition-bg" id="slot-row-${status.index + 1}" data-category="${fn:escapeXml(cat)}">
                                <div class="d-flex align-items-center justify-content-between">
                                    <div class="d-flex align-items-center flex-grow-1">
                                        <div class="cat-icon-box bg-light rounded p-2 me-3 text-center border" style="width: 64px; height: 64px; display:flex; align-items:center; justify-content:center;">
                                            <i class="bi bi-cpu-fill fs-3 text-secondary opacity-50"></i>
                                        </div>
                                        <div class="flex-grow-1">
                                            <h6 class="fw-bold mb-1 text-uppercase text-secondary" style="font-size: 0.9rem;"><c:out value="${cat}" /></h6>
                                            <div class="state-empty text-muted small fst-italic">Vui lòng chọn linh kiện</div>
                                            <div class="state-selected" style="display: none;">
                                                <div class="d-flex align-items-center mt-1">
                                                    <img src="" class="selected-img rounded border me-2 bg-white" style="width: 48px; height: 48px; object-fit: cover;">
                                                    <div>
                                                        <div class="selected-name fw-bold text-dark text-truncate" style="max-width: 350px;"></div>
                                                        <div class="selected-price text-danger fw-bold small"></div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="action-box ms-3">
                                        <button class="btn btn-outline-primary btn-sm btn-select px-3 rounded-pill fw-bold" data-category="${fn:escapeXml(cat)}" data-slot-index="${status.index + 1}" onclick="openProductModal(this.dataset.slotIndex, this.dataset.category)">
                                            <i class="bi bi-plus-lg"></i> Chọn
                                        </button>
                                        <div class="btn-group btn-action-group" style="display: none;">
                                            <button class="btn btn-sm btn-outline-secondary" data-category="${fn:escapeXml(cat)}" data-slot-index="${status.index + 1}" onclick="openProductModal(this.dataset.slotIndex, this.dataset.category)"><i class="bi bi-arrow-repeat"></i></button>
                                            <button class="btn btn-sm btn-outline-danger" data-slot-index="${status.index + 1}" onclick="removeProduct(this.dataset.slotIndex)"><i class="bi bi-trash"></i></button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>

            <div class="col-lg-4">
                <div class="card border-0 shadow-sm sticky-top rounded-3" style="top: 100px; z-index: 30;">
                    <div class="card-header bg-white border-bottom py-3">
                        <h5 class="fw-bold m-0">Tổng kết cấu hình</h5>
                    </div>
                    <div class="card-body">
                        <div class="summary-list mb-3" style="max-height: 250px; overflow-y: auto;">
                            <div id="summary-text-default" class="text-center py-4 text-muted">
                                <i class="bi bi-pc-display fs-1 opacity-25"></i>
                                <p class="mt-2 small">Chưa có linh kiện nào.</p>
                            </div>
                            <ul id="summary-items" class="list-unstyled mb-0"></ul>
                        </div>
                        <div class="d-flex justify-content-between align-items-center mb-3 pt-3 border-top bg-light p-2 rounded">
                            <span class="fw-bold">Tạm tính:</span>
                            <span class="h4 text-danger fw-bold m-0" id="total-price">0₫</span>
                        </div>
                        <c:choose>
                            <c:when test="${cartEnabled}">
                                <button id="btn-checkout-config" class="btn btn-primary w-100 py-2 fw-bold text-uppercase shadow-sm mb-2" onclick="addToCartAll()">
                                    Thêm vào giỏ & Thanh toán
                                </button>
                            </c:when>
                            <c:otherwise>
                                <button id="btn-checkout-config" class="btn btn-primary w-100 py-2 fw-bold text-uppercase shadow-sm mb-2" type="button" disabled="disabled">
                                    Thêm vào giỏ & Thanh toán
                                </button>
                            </c:otherwise>
                        </c:choose>
                        <c:if test="${not cartEnabled}">
                            <div class="alert alert-warning small mb-0">Pha hiện tại chỉ bật chọn linh kiện và xem tổng hợp. Cart/checkout sẽ bật khi <code>BE</code> có module tương ứng.</div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<div class="modal fade" id="productModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-xl modal-dialog-scrollable">
        <div class="modal-content h-100">
            <div class="modal-header bg-primary text-white">
                <h5 class="modal-title fw-bold" id="modalTitle">Chọn linh kiện</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body bg-light">
                <div class="row g-3" id="modal-product-list"></div>
            </div>
        </div>
    </div>
</div>

<c:choose>
    <c:when test="${not empty sessionScope.UserToken or not empty sessionScope.User}">
        <c:set var="buildPcUserLoggedInJs" value="true" />
    </c:when>
    <c:otherwise>
        <c:set var="buildPcUserLoggedInJs" value="false" />
    </c:otherwise>
</c:choose>
<c:choose>
    <c:when test="${cartEnabled}">
        <c:set var="buildPcCartEnabledJs" value="true" />
    </c:when>
    <c:otherwise>
        <c:set var="buildPcCartEnabledJs" value="false" />
    </c:otherwise>
</c:choose>
<div id="build-config"
     data-is-user-logged-in="${buildPcUserLoggedInJs}"
     data-login-url="${ctx}/Login"
     data-cart-enabled="${buildPcCartEnabledJs}"
     hidden></div>
<%@ include file="/WEB-INF/views/common/layout-end.jspf" %>
<script>
    (function () {
        var buildConfigEl = document.getElementById('build-config');
        window.BuildConfig = {
            isUserLoggedIn: buildConfigEl ? buildConfigEl.dataset.isUserLoggedIn : 'false',
            loginUrl: buildConfigEl ? buildConfigEl.dataset.loginUrl : '',
            cartEnabled: buildConfigEl ? buildConfigEl.dataset.cartEnabled : 'false'
        };
    })();
</script>
<script src="${ctx}/assets/js/build-pc.js"></script>
</body>
</html>
