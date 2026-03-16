package com.myanime.domain.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoleModel {
    private String name;
    private String description;
    private List<PermissionModel> permissions;
}
