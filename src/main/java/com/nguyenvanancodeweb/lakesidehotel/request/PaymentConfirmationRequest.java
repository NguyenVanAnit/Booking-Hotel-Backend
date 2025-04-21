package com.nguyenvanancodeweb.lakesidehotel.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaymentConfirmationRequest {
    private String vnp_TxnRef;
    private String vnp_ResponseCode;
    private String vnp_SecureHash;
}
