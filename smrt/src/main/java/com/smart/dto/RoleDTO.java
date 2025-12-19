package com.smart.dto;

import lombok.Data;
import java.util.Set;

@Data
public class RoleDTO {
    private String id;
    private String name;
    private Set<PermissionDTO> permissions;
}
