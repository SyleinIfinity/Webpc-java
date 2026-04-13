package com.webpc.fe.model.payment;

import com.webpc.fe.model.address.AddressViewModel;
import com.webpc.fe.model.cart.CartViewModel;
import com.webpc.fe.model.promotion.KhuyenMaiKhachHangResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CheckoutViewModel {

    private List<AddressViewModel> addresses = new ArrayList<>();
    private CartViewModel cart = new CartViewModel();
    private String selectedIdsString;
    private String phuongThucThanhToan = "COD";
    private String diaChiGiaoHang;
    private String nguoiNhan;
    private String soDienThoai;
    private List<KhuyenMaiKhachHangResponse> danhSachKhuyenMai = new ArrayList<>();
    private BigDecimal tamTinh = BigDecimal.ZERO;
    private BigDecimal phiVanChuyen = BigDecimal.ZERO;
    private BigDecimal tongThanhToan = BigDecimal.ZERO;
}
