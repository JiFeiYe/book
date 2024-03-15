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
import java.time.LocalDateTime;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author JiFeiYe
 * @since 2024-03-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("orders")
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 订单号
     */
    @TableField("number")
    private String number;

    /**
     * 订单状态 1待付款，2待派送，3已派送，4已完成，5已取消
     */
    @TableField("status")
    private Integer status;

    /**
     * 下单用户
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 地址id
     */
    @TableField("address_book_id")
    private Long addressBookId;

    /**
     * 下单时间
     */
    @TableField("order_time")
    private LocalDateTime orderTime;

    /**
     * 结账时间
     */
    @TableField("checkout_time")
    private LocalDateTime checkoutTime;

    /**
     * 支付方式 1微信,2支付宝
     */
    @TableField("pay_method")
    private Integer payMethod;

    /**
     * 实收金额
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    @TableField("phone")
    private String phone;

    @TableField("address")
    private String address;

    @TableField("user_name")
    private String userName;

    @TableField("consignee")
    private String consignee;


}
