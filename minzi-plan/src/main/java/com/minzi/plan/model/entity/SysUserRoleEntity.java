package com.minzi.plan.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.minzi.common.annotation.OneToOne;
import com.minzi.common.core.model.entity.UserEntity;
import com.minzi.plan.service.UserService;
import lombok.Data;

/**
 * 用户角色授权表
 *
 * @author MinZi
 */
@Data
@TableName("sys_user_role")
public class SysUserRoleEntity {


    /**
     * ID
     */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

    /**
     * 用户ID
     */
	private Long userId;

	@OneToOne(localKey = "user_id", foreignKey = "id", targetService = UserService.class)
	@TableField(select = false)
	private UserEntity userEntity;

    /**
     * 角色ID
     */
	private Long roleId;

    /**
     * 创建时间
     */
	@TableField(fill = FieldFill.INSERT)
	private Integer createTime;

    /**
     * 删除时间
     */
	@TableLogic(value = "0", delval = "unix_timestamp()")
	@TableField(select = false)
	private Integer deleteTime;
}