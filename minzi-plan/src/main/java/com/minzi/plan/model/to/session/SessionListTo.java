package com.minzi.plan.model.to.session;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 会话表
 *
 * @author MinZi
 */
@Data
public class SessionListTo implements Serializable {

	private static final long serialVersionUID = 1L;

    /**
     * ID
     */
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
	private Long createTime;
    /**
     * 删除时间
     */
	private Long deleteTime;
}