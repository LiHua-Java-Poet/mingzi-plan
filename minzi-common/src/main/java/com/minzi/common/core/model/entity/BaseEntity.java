package com.minzi.common.core.model.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

@Data
public class BaseEntity {

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT) // 插入时自动填充
    private Integer createTime;

    @TableLogic(value = "0", delval = "unix_timestamp()")
    @TableField(select = false)
    private Integer deletedTime;

}
