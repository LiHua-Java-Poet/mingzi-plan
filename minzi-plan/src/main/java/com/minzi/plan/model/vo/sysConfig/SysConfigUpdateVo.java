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
public class SysConfigUpdateVo implements Serializable {

	private static final long serialVersionUID = 1L;

    /**
     * ID
     */
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
	private Integer deleteTime;
}