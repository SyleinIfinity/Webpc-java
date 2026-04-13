package com.webpc.fe.servlet;

import com.webpc.fe.common.BaseServlet;
import com.webpc.fe.model.promotion.PromotionsViewModel;
import com.webpc.fe.service.PromotionSessionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {
    "/Promotions",
    "/Promotions/Index",
    "/Promotions/Saved",
    "/Promotions/LuuMaKhuyenMai",
    "/Promotions/XoaMaKhuyenMai"
})
public class PromotionsServlet extends BaseServlet {

    private final PromotionSessionService promotionService = new PromotionSessionService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/Promotions/Saved".equals(path)) {
            showSaved(request, response);
            return;
        }
        showIndex(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String path = request.getServletPath();
        switch (path) {
            case "/Promotions/LuuMaKhuyenMai" -> collectPromotion(request, response);
            case "/Promotions/XoaMaKhuyenMai" -> removePromotion(request, response);
            default -> response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void showIndex(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PromotionsViewModel model = new PromotionsViewModel();
        model.setTatCaKhuyenMai(promotionService.getAllPromotions());
        model.setKhoKhuyenMaiDaLuu(promotionService.getSavedPromotionIds(request.getSession()));

        request.setAttribute("pageTitle", "Khuyến mãi");
        request.setAttribute("activeNav", "promotions");
        request.setAttribute("promotionModel", model);
        render(request, response, "promotions/index.jsp");
    }

    private void showSaved(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!requireLogin(request, response)) {
            return;
        }

        request.setAttribute("pageTitle", "Kho khuyến mãi");
        request.setAttribute("activeNav", "promotions");
        request.setAttribute("savedPromotions", promotionService.getSavedView(request.getSession()));
        render(request, response, "promotions/saved.jsp");
    }

    private void collectPromotion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (currentUser(request) == null) {
            flashInfo(request, "Bạn cần đăng nhập để lưu mã.");
            redirect(request, response, "/Login?returnUrl=%2FPromotions");
            return;
        }

        Integer maKhuyenMai = parseNullableInt(request.getParameter("maKhuyenMai"));
        boolean ok = promotionService.savePromotion(
            request.getSession(),
            currentUser(request).getMaKhachHang(),
            maKhuyenMai
        );

        if (ok) {
            flashSuccess(request, "Đã lưu mã khuyến mãi thành công.");
        } else {
            flashError(request, "Không thể lưu mã khuyến mãi này.");
        }
        redirect(request, response, "/Promotions");
    }

    private void removePromotion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!requireLogin(request, response)) {
            return;
        }

        Integer maKMKH = parseNullableInt(request.getParameter("maKMKH"));
        boolean removed = promotionService.removeSavedPromotion(request.getSession(), maKMKH);
        if (removed) {
            flashSuccess(request, "Đã xóa mã khuyến mãi khỏi kho của bạn.");
        } else {
            flashError(request, "Không thể xóa mã khuyến mãi.");
        }
        redirect(request, response, "/Promotions/Saved");
    }
}
