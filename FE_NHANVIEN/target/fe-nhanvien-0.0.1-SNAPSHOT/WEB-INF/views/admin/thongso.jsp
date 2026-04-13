<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/head-start.jspf" %>
<%@ include file="/WEB-INF/views/common/layout-start.jspf" %>
<div class="section-card mb-4">
    <form method="get" action="${ctx}/Admin/ThongSo" class="row g-3 align-items-end">
        <div class="col-lg-8">
            <label class="form-label fw-bold">Chọn sản phẩm để xem thông số</label>
            <select class="form-select" name="productId">
                <option value="">-- Chọn sản phẩm --</option>
                <c:forEach var="product" items="${products}">
                    <c:choose>
                        <c:when test="${selectedProductId == product.maSanPham}">
                            <option value="${product.maSanPham}" selected>
                                ${product.maSanPham} - <c:out value="${product.tenSanPham}" />
                            </option>
                        </c:when>
                        <c:otherwise>
                            <option value="${product.maSanPham}">
                                ${product.maSanPham} - <c:out value="${product.tenSanPham}" />
                            </option>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </select>
        </div>
        <div class="col-lg-4">
            <button type="submit" class="btn btn-primary">Xem thông số</button>
        </div>
    </form>
</div>

<div class="section-card">
    <h4 class="mb-3">Thông số kỹ thuật</h4>
    <table class="table table-striped table-hover">
        <thead>
            <tr><th>Mã</th><th>Tên thông số</th><th>Giá trị</th></tr>
        </thead>
        <tbody>
            <c:forEach var="item" items="${items}">
                <tr>
                    <td>${item.maThongSo}</td>
                    <td><c:out value="${item.tenThongSo}" /></td>
                    <td><c:out value="${item.giaTri}" /></td>
                </tr>
            </c:forEach>
            <c:if test="${empty items}">
                <tr><td colspan="3" class="text-center text-muted">Chọn sản phẩm để tải danh sách thông số.</td></tr>
            </c:if>
        </tbody>
    </table>
</div>
<%@ include file="/WEB-INF/views/common/layout-end.jspf" %>
