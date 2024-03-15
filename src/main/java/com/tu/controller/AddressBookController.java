package com.tu.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.tu.common.BaseContext;
import com.tu.common.R;
import com.tu.entity.AddressBook;
import com.tu.service.IAddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 地址管理 前端控制器
 * </p>
 *
 * @author JiFeiYe
 * @since 2024-03-11
 */
@RestController
@RequestMapping("/addressBook")
@Slf4j
@Api(tags = "地址相关api")
public class AddressBookController {

    @Autowired
    private IAddressBookService addressBookService;

    /**
     * 查询用户地址列表
     *
     * @return List
     */
    @GetMapping("/list")
    @ApiOperation("查询用户地址列表")
    public R<List<AddressBook>> list() {
        log.info("查询用户地址列表");

        LambdaQueryWrapper<AddressBook> lqw = new LambdaQueryWrapper<>();
        lqw.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        lqw.orderByDesc(AddressBook::getIsDefault);
        List<AddressBook> addressBooks = addressBookService.list(lqw);

        return R.success(addressBooks);
    }

    /**
     * 新增地址
     *
     * @param addressBook 地址详情
     * @return String
     */
    @PostMapping
    @ApiOperation("新增地址")
    public R<String> addAddress(
            @ApiParam(name = "addressBook", value = "地址详情") @RequestBody AddressBook addressBook) {
        log.info("开始新增地址");

        Long userId = BaseContext.getCurrentId();
        addressBook.setUserId(userId);
        addressBook.setIsDefault(0);

        addressBookService.save(addressBook);
        return R.success("新增成功！");
    }

    /**
     * 查询单个地址
     *
     * @param id userId
     * @return AddressBook
     */
    @GetMapping("/{id}")
    @ApiOperation("查询单个地址")
    public R<AddressBook> addressFindOne(
            @ApiParam(name = "id", value = "用户id") @PathVariable Long id) {
        log.info("开始查询单个地址");

        LambdaQueryWrapper<AddressBook> lqw = new LambdaQueryWrapper<>();
        lqw.eq(AddressBook::getId, id);
        AddressBook address = addressBookService.getOne(lqw);
        return R.success(address);
    }

    /**
     * 修改地址
     *
     * @param addressBook 地址详情
     * @return String
     */
    @PutMapping
    @ApiOperation("修改地址")
    public R<String> updateAddress(
            @ApiParam(name = "addressBook", value = "地址详情") @RequestBody AddressBook addressBook) {
        log.info("开始修改地址");

        addressBookService.updateById(addressBook);
        return R.success("修改成功！");
    }

    /**
     * 设置默认地址
     *
     * @param addressBook 里面只有id
     * @return String
     */
    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    public R<String> setDefaultAddress(
            @ApiParam(name = "addressBook", value = "地址id") @RequestBody AddressBook addressBook) {
        log.info("开始设置默认地址");

        LambdaUpdateWrapper<AddressBook> luw = new LambdaUpdateWrapper<>();
        luw.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        luw.set(AddressBook::getIsDefault, 0);
        addressBookService.update(luw);

        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);

        return R.success("设置默认地址成功！");
    }

    /**
     * 删除地址
     *
     * @param ids 地址id
     * @return String
     */
    @DeleteMapping
    @ApiOperation("删除地址")
    public R<String> deleteAddress(
            @ApiParam(name = "ids", value = "地址id") @RequestParam Long ids) {
        log.info("开始删除地址 ids:{}", ids);

        addressBookService.removeById(ids);
        return R.success("删除成功！");
    }

    /**
     * 获取默认地址
     *
     * @return AddressBook
     */
    @GetMapping("/default")
    @ApiOperation("获取默认地址")
    public R<AddressBook> getDefaultAddress() {
        log.info("开始获取默认地址");

        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<AddressBook> lqw = new LambdaQueryWrapper<>();
        lqw.eq(AddressBook::getUserId, userId)
                .eq(AddressBook::getIsDefault, 1);
        AddressBook address = addressBookService.getOne(lqw);
        return R.success(address);
    }
}
