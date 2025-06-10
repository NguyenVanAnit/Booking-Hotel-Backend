package com.nguyenvanancodeweb.lakesidehotel.request;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
}
