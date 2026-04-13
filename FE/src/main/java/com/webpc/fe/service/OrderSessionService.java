package com.webpc.fe.service;

import com.webpc.fe.common.SessionKeys;
import com.webpc.fe.model.cart.CartItemViewModel;
import com.webpc.fe.model.order.ChiTietDonHangViewModel;
import com.webpc.fe.model.order.GiaoDichViewModel;
import com.webpc.fe.model.order.LichSuDonHangViewModel;
import com.webpc.fe.model.promotion.KhuyenMaiKhachHangResponse;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class OrderSessionService {

    private static final BigDecimal DEFAULT_SHIPPING_FEE = new BigDecimal("30000");

    public List<LichSuDonHangViewModel> getOrders(HttpSession session) {
        if (session == null) {
            return new ArrayList<>();
        }
        Object value = session.getAttribute(SessionKeys.ORDERS);
        if (value instanceof List<?> raw) {
            List<LichSuDonHangViewModel> result = new ArrayList<>();
            for (Object item : raw) {
                if (item instanceof LichSuDonHangViewModel row) {
                    result.add(row);
                }
            }
            return result;
        }
        List<LichSuDonHangViewModel> init = new ArrayList<>();
        session.setAttribute(SessionKeys.ORDERS, init);
        return init;
    }

    public List<LichSuDonHangViewModel> getOrdersByCustomer(HttpSession session, Integer maKhachHang) {
        return getOrders(session).stream()
            .filter(item -> item.getMaKhachHang() != null && item.getMaKhachHang().equals(maKhachHang))
            .sorted(Comparator.comparing(LichSuDonHangViewModel::getNgayDat, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
            .toList();
    }

    public LichSuDonHangViewModel getOrderById(HttpSession session, Integer maDonHang, Integer maKhachHang) {
        if (maDonHang == null || maKhachHang == null) {
            return null;
        }
        return getOrders(session).stream()
            .filter(item -> maDonHang.equals(item.getMaDonHang()) && maKhachHang.equals(item.getMaKhachHang()))
            .findFirst()
            .orElse(null);
    }

    public LichSuDonHangViewModel createOrder(
        HttpSession session,
        Integer maKhachHang,
        String nguoiNhan,
        String soDienThoai,
        String diaChiGiaoHang,
        String phuongThucThanhToan,
        List<CartItemViewModel> selectedItems,
        KhuyenMaiKhachHangResponse voucher
    ) {
        List<LichSuDonHangViewModel> orders = getOrders(session);

        int orderId = nextOrderId(orders);
        String orderCode = "DH" + String.format(Locale.ROOT, "%06d", orderId);
        BigDecimal subtotal = selectedItems.stream()
            .map(CartItemViewModel::getTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal discount = calculateDiscount(subtotal, voucher);
        BigDecimal shipping = DEFAULT_SHIPPING_FEE;
        BigDecimal total = subtotal.subtract(discount).add(shipping);
        if (total.compareTo(BigDecimal.ZERO) < 0) {
            total = BigDecimal.ZERO;
        }

        LichSuDonHangViewModel order = new LichSuDonHangViewModel();
        order.setMaDonHang(orderId);
        order.setMaCodeDonHang(orderCode);
        order.setMaKhachHang(maKhachHang);
        order.setNgayDat(LocalDateTime.now());
        order.setTrangThai("VietQR".equalsIgnoreCase(phuongThucThanhToan) ? "ChoThanhToan" : "ChoXacNhan");
        order.setPhuongThucThanhToan(phuongThucThanhToan);
        order.setNguoiNhan(nguoiNhan);
        order.setSoDienThoaiGiao(soDienThoai);
        order.setDiaChiGiaoHang(diaChiGiaoHang);
        order.setPhiVanChuyen(shipping);
        order.setGiamGia(discount);
        order.setTongTien(total);

        List<ChiTietDonHangViewModel> details = selectedItems.stream().map(item -> {
            ChiTietDonHangViewModel detail = new ChiTietDonHangViewModel();
            detail.setMaSanPham(item.getProductId());
            detail.setTenSanPham(item.getProductName());
            detail.setHinhAnh(item.getProductImage());
            detail.setSoLuong(item.getQuantity());
            detail.setDonGiaLucMua(item.getPrice());
            detail.setThanhTien(item.getTotal());
            return detail;
        }).toList();
        order.setChiTiet(new ArrayList<>(details));

        if ("COD".equalsIgnoreCase(phuongThucThanhToan)) {
            GiaoDichViewModel tx = new GiaoDichViewModel();
            tx.setMaGiaoDich(nextTransactionId(order));
            tx.setNgayGiaoDich(LocalDateTime.now());
            tx.setSoTien(total);
            tx.setTrangThai("Pending");
            tx.setPhuongThuc("COD");
            order.getGiaoDichs().add(tx);
        }

        orders.add(order);
        session.setAttribute(SessionKeys.ORDERS, orders);

        if (voucher != null) {
            voucher.setDaSuDung(true);
        }

        return order;
    }

    public boolean markPaid(HttpSession session, Integer maDonHang, Integer maKhachHang) {
        LichSuDonHangViewModel order = getOrderById(session, maDonHang, maKhachHang);
        if (order == null) {
            return false;
        }
        if ("Huy".equals(order.getTrangThai()) || "HoanThanh".equals(order.getTrangThai())) {
            return false;
        }
        order.setTrangThai("DangGiao");

        GiaoDichViewModel tx = new GiaoDichViewModel();
        tx.setMaGiaoDich(nextTransactionId(order));
        tx.setNgayGiaoDich(LocalDateTime.now());
        tx.setSoTien(order.getTongTien());
        tx.setTrangThai("Success");
        tx.setPhuongThuc("VietQR");
        order.getGiaoDichs().add(tx);
        return true;
    }

    public boolean confirmReceived(HttpSession session, Integer maDonHang, Integer maKhachHang) {
        LichSuDonHangViewModel order = getOrderById(session, maDonHang, maKhachHang);
        if (order == null) {
            return false;
        }
        if (!"DangGiao".equals(order.getTrangThai())) {
            return false;
        }
        order.setTrangThai("HoanThanh");
        return true;
    }

    public boolean cancelOrder(HttpSession session, Integer maDonHang, Integer maKhachHang) {
        LichSuDonHangViewModel order = getOrderById(session, maDonHang, maKhachHang);
        if (order == null) {
            return false;
        }
        if (!"ChoXacNhan".equals(order.getTrangThai()) && !"ChoThanhToan".equals(order.getTrangThai())) {
            return false;
        }
        order.setTrangThai("Huy");
        return true;
    }

    public String paymentStatus(HttpSession session, Integer maDonHang, Integer maKhachHang) {
        LichSuDonHangViewModel order = getOrderById(session, maDonHang, maKhachHang);
        if (order == null) {
            return "ERROR";
        }
        if ("DangGiao".equals(order.getTrangThai()) || "DaThanhToan".equals(order.getTrangThai()) || "HoanThanh".equals(order.getTrangThai())) {
            return "PAID";
        }
        return "PENDING";
    }

    private int nextOrderId(List<LichSuDonHangViewModel> orders) {
        return orders.stream()
            .map(LichSuDonHangViewModel::getMaDonHang)
            .filter(id -> id != null)
            .max(Comparator.naturalOrder())
            .orElse(0) + 1;
    }

    private int nextTransactionId(LichSuDonHangViewModel order) {
        return order.getGiaoDichs().stream()
            .map(GiaoDichViewModel::getMaGiaoDich)
            .filter(id -> id != null)
            .max(Comparator.naturalOrder())
            .orElse(0) + 1;
    }

    private BigDecimal calculateDiscount(BigDecimal subtotal, KhuyenMaiKhachHangResponse voucher) {
        if (voucher == null || subtotal == null || subtotal.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        if (voucher.getDonHangToiThieu() != null && subtotal.compareTo(voucher.getDonHangToiThieu()) < 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal discount;
        if ("PERCENT".equalsIgnoreCase(voucher.getLoaiGiam())) {
            discount = subtotal.multiply(voucher.getGiaTriGiam()).divide(new BigDecimal("100"));
            if (voucher.getGiamToiDa() != null && discount.compareTo(voucher.getGiamToiDa()) > 0) {
                discount = voucher.getGiamToiDa();
            }
        } else {
            discount = voucher.getGiaTriGiam();
        }

        if (discount.compareTo(subtotal) > 0) {
            return subtotal;
        }
        return discount.max(BigDecimal.ZERO);
    }
}
