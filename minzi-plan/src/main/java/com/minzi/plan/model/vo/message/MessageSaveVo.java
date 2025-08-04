package com.minzi.plan.model.vo.message;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 消息表
 *
 * @author MinZi
 */
@Data
public class MessageSaveVo implements Serializable {

	private static final long serialVersionUID = 1L;

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
	private Integer createTime;
			/**
	 * 删除时间
	*/
	private Integer deleteTime;
	}