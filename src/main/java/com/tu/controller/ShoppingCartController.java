package com.tu.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.tu.common.BaseContext;
import com.tu.common.R;
import com.tu.entity.Book;
import com.tu.entity.ShoppingCart;
import com.tu.service.IBookService;
import com.tu.service.IShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 购物车 前端控制器
 * </p>
 *
 * @author JiFeiYe
 * @since 2024-03-11
 */
@RestController
@RequestMapping("/shoppingCart")
@Slf4j
@Api(tags = "购物车相关api")
public class ShoppingCartController {

    @Autowired
    private IShoppingCartService shoppingCartService;

    @Autowired
    private IBookService bookService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 保存到购物车
     *
     * @param shoppingCart 购物车
     * @return ShoppingCart
     */
    @PostMapping("/add")
    @ApiOperation("保存到购物车")
    public R<ShoppingCart> addCart(
            @ApiParam(name = "shoppingCart", value = "购物车详情") @RequestBody ShoppingCart shoppingCart) {
        log.info("开始保存到购物车 shoppingCart:{}", shoppingCart);

        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        if (shoppingCart.getBookId() != null) {
            shoppingCart.setSetmealId(shoppingCart.getBookId());
        } else if (shoppingCart.getSetmealId() != null) {
            shoppingCart.setBookId(shoppingCart.getSetmealId());
        }

        // 填充数据并查询购物车是否空
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, userId);
        Long bookId = shoppingCart.getBookId();
        lqw.eq(ShoppingCart::getBookId, bookId);
        ShoppingCart shoppingCart1 = shoppingCartService.getOne(lqw);

        // 当前物品购物车空的
        if (shoppingCart1 == null) {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            shoppingCart1 = shoppingCart;
        } else {
            shoppingCart1.setNumber(shoppingCart1.getNumber() + 1);
            shoppingCartService.updateById(shoppingCart1);
        }

        LambdaQueryWrapper<Book> bookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        bookLambdaQueryWrapper.eq(Book::getId, shoppingCart1.getBookId());
        Book book = bookService.getOne(bookLambdaQueryWrapper);
        String key = "book_" + book.getCategoryId() + "_1";
        // todo 缓存负优化
        redisTemplate.delete(key);
        return R.success(shoppingCart1);
    }

    /**
     * 获取购物车列表
     *
     * @return ShoppingCarts
     */
    @GetMapping("list")
    @ApiOperation("获取购物车列表")
    public R<List<ShoppingCart>> cartList() {
        log.info("获取购物车列表");

        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, userId)
                .orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(lqw);
        log.info("查询到的购物车：{}", shoppingCarts);
        return R.success(shoppingCarts);
    }

    /**
     * 购物车某一商品数量减一
     *
     * @param map 图书id
     * @return String
     */
    @PostMapping("/sub")
    @ApiOperation("购物车某一商品数量减一")
    public R<ShoppingCart> subCart(
            @ApiParam(name = "id", value = "图书id") @RequestBody Map map) {
        log.info("购物车某一商品数量减一 map:{}", map);

        Long userId = BaseContext.getCurrentId();
        log.info("userId:{}", userId);
        LambdaUpdateWrapper<ShoppingCart> lqw = new LambdaUpdateWrapper<>();
        lqw.eq(ShoppingCart::getUserId, userId);
        if (map.get("bookId") != null) {
            lqw.eq(ShoppingCart::getBookId, map.get("dishId"));
        } else {
            lqw.eq(ShoppingCart::getSetmealId, map.get("setmealId"));
        }
        ShoppingCart shoppingCart = shoppingCartService.getOne(lqw);
        if (shoppingCart.getNumber() > 1) {
            lqw.setSql("number = number - 1");
            shoppingCartService.update(lqw);
            shoppingCart = shoppingCartService.getOne(lqw);
        } else if (shoppingCart.getNumber() == 1) {
            shoppingCartService.removeById(shoppingCart);
            shoppingCart.setNumber(0);
        }

        LambdaQueryWrapper<Book> bookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        bookLambdaQueryWrapper.eq(Book::getId, shoppingCart.getBookId());
        Book book = bookService.getOne(bookLambdaQueryWrapper);
        String key = "book_" + book.getCategoryId() + "_1";

        // todo 缓存负优化
        redisTemplate.delete(key);

        return R.success(shoppingCart);
    }

    /**
     * 清空购物车
     *
     * @return String
     */
    @DeleteMapping("clean")
    @ApiOperation("清空购物车")
    public R<String> clearCart() {
        log.info("开始清空购物车");

        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, userId);
        shoppingCartService.remove(lqw);
        return R.success("清空购物车成功！");
    }
}
