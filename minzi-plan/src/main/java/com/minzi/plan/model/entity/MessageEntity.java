package com.minzi.plan.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.minzi.common.annotation.OneToOne;
import com.minzi.plan.service.SessionService;
import lombok.Data;

import java.io.Serializable;

/**
 * 消息表
 *
 * @author MinZi
 */
@Data
@TableName("message")
public class MessageEntity implements Serializable {

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会话ID
     */
    private Long sessionId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 收藏类型 1 文本 2 文件
     */
    private Integer collectType;

    /**
     * 内容
     */
    private String content;

    /**
     * 类型 1 仅记录 2 知识库存储 3 Ai回复
     */
    private Integer messageType;

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

    @TableField(exist = false)
    @OneToOne(localKey = "session_id",foreignKey = "id",targetService = SessionService.class)
    private SessionEntity sessionEntity;
}