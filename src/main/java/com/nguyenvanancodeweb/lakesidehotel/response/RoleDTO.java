package com.nguyenvanancodeweb.lakesidehotel.response;

import lombok.Data;

@Data
public class RoleDTO {
    private Long id;
    private String name;

    public RoleDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters & setters
}
