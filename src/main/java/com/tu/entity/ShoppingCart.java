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
 * 购物车
 * </p>
 *
 * @author JiFeiYe
 * @since 2024-03-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("shopping_cart")
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 图片
     */
    @TableField("image")
    private String image;

    /**
     * 主键
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 图书id
     */
    @TableField("book_id")
    private Long bookId;

    /**
     * 套餐id（弃用）
     */
    @TableField("setmeal_id")
    private Long setmealId;

    /**
     * 口味（弃用）
     */
    @TableField("dish_flavor")
    private String dishFlavor;

    /**
     * 数量
     */
    @TableField("number")
    private Integer number;

    /**
     * 金额
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;


}
