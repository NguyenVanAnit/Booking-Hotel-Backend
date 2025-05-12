package com.nguyenvanancodeweb.lakesidehotel.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginStaffRequest {
    private String phoneNumber;
    private String password;
}
