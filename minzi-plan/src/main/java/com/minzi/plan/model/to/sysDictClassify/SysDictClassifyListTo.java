package com.minzi.plan.model.to.sysDictClassify;

import com.baomidou.mybatisplus.annotation.*;
import com.minzi.plan.model.to.sysRole.SysRoleListTo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 字典分类表
 *
 * @author MinZi
 */
@Data
public class SysDictClassifyListTo implements Serializable {

	private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	private Long id;

    /**
     * 父节点ID
     */
	private Long parentId;

    /**
     * 字典分类名称
     */
	private String classifyName;

    /**
     * 排序
     */
	private Integer sort;

    /**
     * 备注说明
     */
	private String remark;

	List<SysDictClassifyListTo> children;

    /**
     * 创建时间
     */
	private Integer createTime;

    /**
     * 更新时间
     */
	private Integer updateTime;

    /**
     * 删除时间
     */
	private Integer deleteTime;

}