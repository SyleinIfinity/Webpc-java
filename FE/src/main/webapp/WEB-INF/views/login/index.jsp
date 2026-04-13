<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/head-start.jspf" %>
<%@ include file="/WEB-INF/views/common/layout-start.jspf" %>
<section class="py-5 bg-light" style="min-height: 60vh;">
    <div class="container py-5 text-center">
        <h2 class="fw-bold">Đăng nhập tài khoản</h2>
        <p class="text-muted">Trang này dùng để mở lại login modal khi đăng nhập thất bại hoặc người dùng muốn đăng nhập trực tiếp.</p>
        <button class="btn btn-primary btn-lg" data-bs-toggle="modal" data-bs-target="#loginModal">Mở form đăng nhập</button>
    </div>
</section>
<%@ include file="/WEB-INF/views/common/layout-end.jspf" %>
<c:if test="${openLoginModal}">
    <script>
        document.addEventListener('DOMContentLoaded', function () {
            var modalElement = document.getElementById('loginModal');
            if (modalElement) {
                bootstrap.Modal.getOrCreateInstance(modalElement).show();
            }
        });
    </script>
</c:if>
</body>
</html>
