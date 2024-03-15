package com.tu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 地址管理
 * </p>
 *
 * @author JiFeiYe
 * @since 2024-03-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("address_book")
public class AddressBook implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 收货人
     */
    @TableField("consignee")
    private String consignee;

    /**
     * 性别 0 女 1 男
     */
    @TableField("sex")
    private Integer sex;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 省级区划编号
     */
    @TableField("province_code")
    private String provinceCode;

    /**
     * 省级名称
     */
    @TableField("province_name")
    private String provinceName;

    /**
     * 市级区划编号
     */
    @TableField("city_code")
    private String cityCode;

    /**
     * 市级名称
     */
    @TableField("city_name")
    private String cityName;

    /**
     * 区级区划编号
     */
    @TableField("district_code")
    private String districtCode;

    /**
     * 区级名称
     */
    @TableField("district_name")
    private String districtName;

    /**
     * 详细地址
     */
    @TableField("detail")
    private String detail;

    /**
     * 标签
     */
    @TableField("label")
    private String label;

    /**
     * 默认 0 否 1是
     */
    @TableField("is_default")
    private Integer isDefault;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改人
     */
    @TableField(value = "update_user", fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 是否删除
     */
    @TableField("is_deleted")
    @TableLogic
    private Integer isDeleted;


}


