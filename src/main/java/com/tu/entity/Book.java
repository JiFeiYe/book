package com.tu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 图书管理
 * </p>
 *
 * @author JiFeiYe
 * @since 2024-03-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("book")
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 图书名称
     */
    @TableField("name")
    private String name;

    /**
     * 图书分类id
     */
    @TableField("category_id")
    private Long categoryId;

    /**
     * 图书价格
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 商品码
     */
    @TableField("code")
    private String code;

    /**
     * 图片
     */
    @TableField("image")
    private String image;

    /**
     * 描述信息
     */
    @TableField("description")
    private String description;

    /**
     * 0 停售 1 起售
     */
    @TableField("status")
    private Integer status;

    /**
     * 顺序
     */
    @TableField("sort")
    private Integer sort;

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
