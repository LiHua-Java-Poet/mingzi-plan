package com.minzi.plan.model.vo.sysRoleMenu;


import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class PermissionVo {

    @NotNull(message = "菜单ID不能为空")
    private Long menuId;

    @NotNull(message = "角色ID不能为空")
    private Long roleId;
}
