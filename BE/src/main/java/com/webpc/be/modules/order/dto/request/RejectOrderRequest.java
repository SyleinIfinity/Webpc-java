package com.webpc.be.modules.order.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RejectOrderRequest(
    @NotBlank(message = "Vui long cung cap ly do tu choi (lyDoTuChoi).")
    String lyDoTuChoi
) {
}

