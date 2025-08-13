package com.minzi.plan.model.to.message;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 消息表
 *
 * @author MinZi
 */
@Data
public class MessageListTo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
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
     * 回话名称
     */
    private String sessionTitle;

    /**
     * 创建时间
     */
    private Integer createTime;

    /**
     * 删除时间
     */
    private Integer deleteTime;

}