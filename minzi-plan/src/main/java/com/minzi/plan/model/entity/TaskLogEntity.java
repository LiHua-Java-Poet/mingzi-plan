package com.minzi.plan.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 任务日志表
 *
 * @author MinZi
 */
@Data
@TableName("task_log")
public class TaskLogEntity {


    /**
     * ID
     */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

    /**
     * 任务id
     */
	private Long taskId;

    /**
     * 日志内容
     */
	private String logContent;

    /**
     * 日志附件
     */
	private String annexFile;

    /**
     * 创建时间
     */
	@TableField(fill = FieldFill.INSERT)
	private Integer createTime;

    /**
     * 更新时间
     */
	private Integer updateTime;

    /**
     * 删除时间
     */
	@TableLogic(value = "0", delval = "unix_timestamp()")
	@TableField(select = false)
	private Integer deleteTime;
}