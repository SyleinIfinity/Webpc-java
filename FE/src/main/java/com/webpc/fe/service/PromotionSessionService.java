package com.webpc.fe.service;

import com.webpc.fe.common.SessionKeys;
import com.webpc.fe.model.promotion.KhuyenMaiDaLuuViewModel;
import com.webpc.fe.model.promotion.KhuyenMaiKhachHangResponse;
import com.webpc.fe.model.promotion.KhuyenMaiResponse;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PromotionSessionService {

    public List<KhuyenMaiResponse> getAllPromotions() {
        LocalDateTime now = LocalDateTime.now();
        List<KhuyenMaiResponse> items = new ArrayList<>();

        items.add(buildPromotion(1, "NEWBIE50", "Giảm 50.000đ cho khách mới", new BigDecimal("50000"), "DIRECT",
            new BigDecimal("500000"), null, now.minusDays(5), now.plusDays(25), 500));
        items.add(buildPromotion(2, "PC10", "Giảm 10% đơn build PC", new BigDecimal("10"), "PERCENT",
            new BigDecimal("3000000"), new BigDecimal("400000"), now.minusDays(2), now.plusDays(15), 200));
        items.add(buildPromotion(3, "SALE200", "Giảm trực tiếp 200.000đ", new BigDecimal("200000"), "DIRECT",
            new BigDecimal("4000000"), null, now.minusDays(1), now.plusDays(10), 100));
        items.add(buildPromotion(4, "RAM5", "Giảm 5% linh kiện RAM/SSD", new BigDecimal("5"), "PERCENT",
            new BigDecimal("1000000"), new BigDecimal("150000"), now.minusDays(3), now.plusDays(20), 250));
        items.add(buildPromotion(5, "SHIPFREE", "Giảm 30.000đ tương đương freeship", new BigDecimal("30000"), "DIRECT",
            new BigDecimal("800000"), null, now.minusDays(7), now.plusDays(30), 1000));

        return items;
    }

    public List<Integer> getSavedPromotionIds(HttpSession session) {
        return getSavedPromotions(session).stream()
            .map(KhuyenMaiKhachHangResponse::getMaKhuyenMai)
            .filter(id -> id != null)
            .toList();
    }

    public List<KhuyenMaiKhachHangResponse> getSavedPromotions(HttpSession session) {
        if (session == null) {
            return new ArrayList<>();
        }
        Object value = session.getAttribute(SessionKeys.SAVED_PROMOTIONS);
        if (value instanceof List<?> raw) {
            List<KhuyenMaiKhachHangResponse> result = new ArrayList<>();
            for (Object item : raw) {
                if (item instanceof KhuyenMaiKhachHangResponse row) {
                    result.add(row);
                }
            }
            return result;
        }
        List<KhuyenMaiKhachHangResponse> init = new ArrayList<>();
        session.setAttribute(SessionKeys.SAVED_PROMOTIONS, init);
        return init;
    }

    public boolean savePromotion(HttpSession session, Integer maKhachHang, Integer maKhuyenMai) {
        KhuyenMaiResponse promo = findPromotion(maKhuyenMai);
        if (promo == null) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        if (promo.getSoLuongConLai() <= 0 || promo.getNgayBatDau().isAfter(now) || promo.getNgayKetThuc().isBefore(now)) {
            return false;
        }

        List<KhuyenMaiKhachHangResponse> saved = getSavedPromotions(session);
        boolean exists = saved.stream().anyMatch(x -> x.getMaKhuyenMai() != null && x.getMaKhuyenMai().equals(maKhuyenMai));
        if (exists) {
            return true;
        }

        KhuyenMaiKhachHangResponse row = new KhuyenMaiKhachHangResponse();
        row.setMaKMKH(nextSavedId(saved));
        row.setMaKhachHang(maKhachHang);
        row.setMaKhuyenMai(promo.getMaKhuyenMai());
        row.setMaCodeKM(promo.getMaCodeKM());
        row.setTenChuongTrinh(promo.getTenChuongTrinh());
        row.setDaSuDung(false);
        row.setNgayThuThap(now);
        row.setGiaTriGiam(promo.getGiaTriGiam());
        row.setLoaiGiam(promo.getLoaiGiam());
        row.setDonHangToiThieu(promo.getDonHangToiThieu());
        row.setGiamToiDa(promo.getGiamToiDa());
        row.setNgayBatDau(promo.getNgayBatDau());
        row.setNgayKetThuc(promo.getNgayKetThuc());
        saved.add(row);
        session.setAttribute(SessionKeys.SAVED_PROMOTIONS, saved);
        return true;
    }

    public boolean removeSavedPromotion(HttpSession session, Integer maKMKH) {
        List<KhuyenMaiKhachHangResponse> saved = getSavedPromotions(session);
        boolean removed = saved.removeIf(x -> x.getMaKMKH() != null && x.getMaKMKH().equals(maKMKH));
        session.setAttribute(SessionKeys.SAVED_PROMOTIONS, saved);
        return removed;
    }

    public List<KhuyenMaiDaLuuViewModel> getSavedView(HttpSession session) {
        return getSavedPromotions(session).stream()
            .map(item -> {
                KhuyenMaiDaLuuViewModel vm = new KhuyenMaiDaLuuViewModel();
                vm.setMaKMKH(item.getMaKMKH());
                vm.setMaKhuyenMai(item.getMaKhuyenMai());
                vm.setMaCodeKM(item.getMaCodeKM());
                vm.setTenChuongTrinh(item.getTenChuongTrinh());
                vm.setLoaiGiam(item.getLoaiGiam());
                vm.setGiaTriGiam(item.getGiaTriGiam());
                vm.setDonHangToiThieu(item.getDonHangToiThieu());
                vm.setGiamToiDa(item.getGiamToiDa());
                vm.setNgayKetThuc(item.getNgayKetThuc());
                return vm;
            })
            .sorted(Comparator.comparing(KhuyenMaiDaLuuViewModel::getNgayKetThuc, Comparator.nullsLast(Comparator.naturalOrder())))
            .toList();
    }

    public List<KhuyenMaiKhachHangResponse> getApplicablePromotions(HttpSession session, BigDecimal orderSubtotal) {
        LocalDateTime now = LocalDateTime.now();
        return getSavedPromotions(session).stream()
            .filter(item -> !item.isDaSuDung())
            .filter(item -> item.getNgayBatDau() != null && !item.getNgayBatDau().isAfter(now))
            .filter(item -> item.getNgayKetThuc() != null && !item.getNgayKetThuc().isBefore(now))
            .filter(item -> orderSubtotal.compareTo(item.getDonHangToiThieu()) >= 0)
            .toList();
    }

    public KhuyenMaiKhachHangResponse getByCode(HttpSession session, String code) {
        if (code == null || code.isBlank()) {
            return null;
        }
        return getSavedPromotions(session).stream()
            .filter(item -> code.equalsIgnoreCase(item.getMaCodeKM()))
            .findFirst()
            .orElse(null);
    }

    private KhuyenMaiResponse buildPromotion(
        int id,
        String code,
        String title,
        BigDecimal value,
        String type,
        BigDecimal minOrder,
        BigDecimal maxDiscount,
        LocalDateTime start,
        LocalDateTime end,
        int quantity
    ) {
        KhuyenMaiResponse item = new KhuyenMaiResponse();
        item.setMaKhuyenMai(id);
        item.setMaCodeKM(code);
        item.setTenChuongTrinh(title);
        item.setGiaTriGiam(value);
        item.setLoaiGiam(type);
        item.setDonHangToiThieu(minOrder);
        item.setGiamToiDa(maxDiscount);
        item.setNgayBatDau(start);
        item.setNgayKetThuc(end);
        item.setSoLuongConLai(quantity);
        item.setCoTheLuu(true);
        return item;
    }

    private KhuyenMaiResponse findPromotion(Integer maKhuyenMai) {
        if (maKhuyenMai == null) {
            return null;
        }
        return getAllPromotions().stream()
            .filter(item -> item.getMaKhuyenMai() != null && item.getMaKhuyenMai().equals(maKhuyenMai))
            .findFirst()
            .orElse(null);
    }

    private int nextSavedId(List<KhuyenMaiKhachHangResponse> saved) {
        return saved.stream()
            .map(KhuyenMaiKhachHangResponse::getMaKMKH)
            .filter(id -> id != null)
            .max(Comparator.naturalOrder())
            .orElse(0) + 1;
    }
}
