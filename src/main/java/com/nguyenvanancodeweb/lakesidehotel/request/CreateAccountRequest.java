package com.nguyenvanancodeweb.lakesidehotel.request;

import lombok.Data;

@Data
public class CreateAccountRequest {
    private String fullName;
    private String email;
    private String phoneNumber;
    private Long roleId;
}
