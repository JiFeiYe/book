package com.tu.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tu.common.BaseContext;
import com.tu.common.R;
import com.tu.dto.OrdersDto;
import com.tu.entity.OrderDetail;
import com.tu.entity.Orders;
import com.tu.service.IOrderDetailService;
import com.tu.service.IOrdersService;
import com.tu.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author JiFeiYe
 * @since 2024-03-11
 */
@RestController
@RequestMapping("/order")
@Slf4j
@Api(tags = "订单相关api")
public class OrdersController {

    @Autowired
    private IOrdersService ordersService;

    @Autowired
    private IOrderDetailService orderDetailService;

    @Autowired
    private IUserService userService;

    /**
     * 订单提交
     *
     * @param orders 订单详情
     * @return String
     */
    @PostMapping("/submit")
    @ApiOperation("订单提交")
    public R<String> submit(
            @ApiParam(name = "orders", value = "订单详情") @RequestBody Orders orders) {
        log.info("订单提交");

        ordersService.submit(orders);
        return R.success("订单提交成功！");
    }

    /**
     * 分页获取PC端订单列表
     *
     * @param page     当前页面
     * @param pageSize 页面大小
     * @return IPage
     */
    @GetMapping("/page")
    @ApiOperation("分页获取pc端订单列表")
    public R<IPage<Orders>> orderPaging(
            @ApiParam(name = "page", value = "当前页码") Integer page,
            @ApiParam(name = "pageSize", value = "页面大小") Integer pageSize,
            @ApiParam(name = "number", value = "查询订单号关键字") Long number,
            @ApiParam(name = "beginTime", value = "开始时间") String beginTime,
            @ApiParam(name = "endTime", value = "结束时间") String endTime) {
        log.info("开始分页获取pc端订单列表");

        IPage<Orders> ordersIPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ordersLambdaQueryWrapper
                .like(number != null, Orders::getId, number)
                .ge(StringUtils.isNotEmpty(beginTime), Orders::getOrderTime, beginTime)
                .le(StringUtils.isNotEmpty(endTime), Orders::getOrderTime, endTime)
                .orderByAsc(Orders::getStatus);
        ordersService.page(ordersIPage, ordersLambdaQueryWrapper);

//        IPage<OrdersDto> ordersDtoIPage = new Page<>();
//        BeanUtils.copyProperties(ordersIPage, ordersDtoIPage, "records");
//
//        List<Orders> orders = ordersIPage.getRecords();
//        List<OrdersDto> ordersDtos = orders.stream().map((order) -> {
//            OrdersDto ordersDto = new OrdersDto();
//            User user = userService.getById(order.getUserId());
//            if (user != null) {
//                ordersDto.setUserNamee(user.getName());
//            }
//            return ordersDto;
//        }).toList();
//
//        ordersDtoIPage.setRecords(ordersDtos);
        return R.success(ordersIPage);
    }


    /**
     * 分页获取移动端订单列表
     *
     * @param page     当前页面
     * @param pageSize 页面大小
     * @return IPage
     */
    @GetMapping("/userPage")
    @ApiOperation("分页获取移动端订单列表")
    public R<IPage<OrdersDto>> userOrderPaging(
            @ApiParam(name = "page", value = "当前页码") Integer page,
            @ApiParam(name = "pageSize", value = "页面大小") Integer pageSize) {
        log.info("开始分页获取移动端订单列表 page:{}, pageSize:{}", page, pageSize);

        IPage<Orders> ordersIPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ordersLambdaQueryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId())
                .orderByAsc(Orders::getStatus)
                .orderByDesc(Orders::getCheckoutTime);
        ordersService.page(ordersIPage, ordersLambdaQueryWrapper);

        IPage<OrdersDto> ordersDtoIPage = new Page<>();
        BeanUtils.copyProperties(ordersIPage, ordersDtoIPage, "records");
        List<Orders> orders = ordersIPage.getRecords();
        List<OrdersDto> OrdersDto = orders.stream().map((order) -> {
            LambdaQueryWrapper<OrderDetail> orderDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
            orderDetailLambdaQueryWrapper.eq(OrderDetail::getOrderId, order.getId());
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(order, ordersDto);
            ordersDto.setOrderDetails(orderDetailService.list(orderDetailLambdaQueryWrapper));
            return ordersDto;
        }).toList();
        ordersDtoIPage.setRecords(OrdersDto);

        log.info("ordersIPage: {}", ordersIPage.getRecords());
        log.info("ordersDtoIPage: {}", ordersDtoIPage.getRecords());
        return R.success(ordersDtoIPage);
    }

    /**
     * PC端订单状态修改
     *
     * @param orders 订单详情
     * @return String
     */
    @PutMapping
    @ApiOperation("PC端订单状态修改")
    public R<String> editOrderDetail(
            @ApiParam(name = "orders", value = "订单详情") @RequestBody Orders orders) {
        log.info("开始修改PC端订单状态");

        LambdaUpdateWrapper<Orders> ordersLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        ordersLambdaUpdateWrapper
                .eq(Orders::getId, orders.getId())
                .set(Orders::getStatus, orders.getStatus());
        ordersService.update(ordersLambdaUpdateWrapper);
        return R.success("修改成功！");
    }

    @PostMapping("/again")
    public R<String> again() {
        return R.success("再来一单");
    }
}
