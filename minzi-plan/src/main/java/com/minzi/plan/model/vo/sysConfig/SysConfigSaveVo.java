package com.minzi.plan.model.vo.sysConfig;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 配置表
 *
 * @author MinZi
 */
@Data
public class SysConfigSaveVo implements Serializable {

	private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	    /**
     * 配置名
     */
			/**
		 * 配置名
		 */
		private String confName;
	    /**
     * 配置内容
     */
			/**
		 * 配置内容
		 */
		private String confContent;
	    /**
     * 状态 1 正常 2 停用
     */
			/**
		 * 状态 1 正常 2 停用
		 */
		private Integer status;
	    /**
     * 删除时间
     */
			/**
		 * 删除时间
		 */
		private Integer deleteTime;
	}