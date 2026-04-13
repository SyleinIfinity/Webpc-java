package com.webpc.fe.model.payment;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiStatusResponse {

    private String status;
    private String message;
    private boolean shouldRedirect;
}
