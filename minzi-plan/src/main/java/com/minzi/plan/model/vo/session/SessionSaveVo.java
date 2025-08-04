package com.minzi.plan.model.vo.session;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 会话表
 *
 * @author MinZi
 */
@Data
public class SessionSaveVo implements Serializable {

	private static final long serialVersionUID = 1L;

    /**
     * id
     */
	    /**
     * 标题
     */
			/**
		 * 标题
		 */
		private String title;
	    /**
     * 用户ID
     */
			/**
		 * 用户ID
		 */
		private Long userId;
	    /**
     * 创建时间
     */
			/**
		 * 创建时间
		 */
		private Long createTime;
	    /**
     * 删除时间
     */
			/**
		 * 删除时间
		 */
		private Long deteleId;
	}