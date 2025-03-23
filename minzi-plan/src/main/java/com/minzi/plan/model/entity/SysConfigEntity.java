package com.minzi.plan.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 配置表
 *
 * @author MinZi
 */
@Data
@TableName("sys_config")
public class SysConfigEntity {

    /**
     * ID
     */
				@TableId(value = "id", type = IdType.AUTO)
		private Long id;
    /**
     * 配置名
     */
				private String confName;
    /**
     * 配置内容
     */
				private String confContent;
    /**
     * 状态 1 正常 2 停用
     */
				private Integer status;
    /**
     * 删除时间
     */
		@TableLogic(value = "0", delval = "unix_timestamp()")
	@TableField(select = false)
				private Integer deleteTime;
}