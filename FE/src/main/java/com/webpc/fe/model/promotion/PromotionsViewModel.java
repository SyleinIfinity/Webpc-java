package com.webpc.fe.model.promotion;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PromotionsViewModel {

    private List<KhuyenMaiResponse> tatCaKhuyenMai = new ArrayList<>();
    private List<Integer> khoKhuyenMaiDaLuu = new ArrayList<>();
}
