package com.tu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 订单明细表
 * </p>
 *
 * @author JiFeiYe
 * @since 2024-03-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("order_detail")
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 名字
     */
    @TableField("name")
    private String name;

    /**
     * 图片
     */
    @TableField("image")
    private String image;

    /**
     * 订单id
     */
    @TableField("order_id")
    private Long orderId;

    /**
     * 书本id
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


}
