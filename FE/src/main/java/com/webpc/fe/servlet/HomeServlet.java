package com.webpc.fe.servlet;

import com.webpc.fe.common.BaseServlet;
import com.webpc.fe.model.catalog.ProductViewModel;
import com.webpc.fe.service.CatalogService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@WebServlet(urlPatterns = {"/Home", "/Home/Index", "/home", "/home/index"})
public class HomeServlet extends BaseServlet {

    private final CatalogService catalogService = new CatalogService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<ProductViewModel> latestProducts = catalogService.getProducts().stream()
            .sorted(Comparator.comparing(ProductViewModel::getMaSanPham, Comparator.nullsLast(Integer::compareTo)).reversed())
            .limit(4)
            .toList();

        request.setAttribute("pageTitle", "Trang Chủ");
        request.setAttribute("activeNav", "home");
        request.setAttribute("latestProducts", latestProducts);
        render(request, response, "home/index.jsp");
    }
}
