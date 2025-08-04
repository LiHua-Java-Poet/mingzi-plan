package com.minzi.plan.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 会话表
 *
 * @author MinZi
 */
@Data
@TableName("session")
public class SessionEntity {

    /**
     * id
     */
				@TableId(value = "id", type = IdType.AUTO)
		private Long id;
    /**
     * 标题
     */
				private String title;
    /**
     * 用户ID
     */
				private Long userId;
    /**
     * 创建时间
     */
			@TableField(fill = FieldFill.INSERT)
			private Long createTime;
    /**
     * 删除时间
     */
				private Long deteleId;
}