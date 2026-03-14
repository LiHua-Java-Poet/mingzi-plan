package com.minzi.plan.model.to.taskLog;

import com.baomidou.mybatisplus.annotation.*;
import com.minzi.common.core.model.AnnexFile;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 任务日志表
 *
 * @author MinZi
 */
@Data
public class TaskLogListTo implements Serializable {

	private static final long serialVersionUID = 1L;

    /**
     * ID
     */
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
	private List<AnnexFile> annexFileList;

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