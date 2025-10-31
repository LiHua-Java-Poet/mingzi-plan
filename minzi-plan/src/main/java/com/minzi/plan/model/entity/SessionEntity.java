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
     * ID
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
     * 更新时间
     */
    private Integer updateTime;

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