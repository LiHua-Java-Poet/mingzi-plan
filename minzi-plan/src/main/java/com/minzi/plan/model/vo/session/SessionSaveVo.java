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
	 * 标题
	*/
	private String title;

	/**
	 * 唯一码
	*/
	private String uniqueCode;

}