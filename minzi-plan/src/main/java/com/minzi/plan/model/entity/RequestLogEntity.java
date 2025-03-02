package com.minzi.plan.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("request_log")
public class RequestLogEntity implements Serializable {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id; // ID

    /**
     * 请求时间
     */
    private Integer requestTime; // 请求时间

    /**
     * 请求方式
     */
    private String requestMethod; // 请求方式

    /**
     * 完整的地址（JSON）
     */
    private String requestUrl; // 完整的地址（JSON）

    /**
     *  请求参数（JSON）
     */
    private String requestParams; // 请求参数（JSON）

    /**
     * 请求头（JSON）
     */
    private String requestHeaders; // 请求头（JSON）

    /**
     * 请求体（JSON）
     */
    private String requestBody; // 请求体（JSON）

    /**
     * 状态码
     */
    private Integer responseStatus; // 状态码

    /**
     * 返回数据（JSON）
     */
    private String responseData; // 返回数据（JSON）

    /**
     * 响应时间
     */
    private Integer responseTime; // 响应时间

    /**
     * 请求用时
     */
    private Integer durationTime;

    /**
     * 客户端IP地址
     */
    private String clientIp; // 客户端IP地址

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT) // 插入时自动填充
    private Integer createTime; // 创建时间
}
