package com.tu.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tu.common.R;
import com.tu.entity.Category;
import com.tu.service.ICategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 书籍分类 前端控制器
 * </p>
 *
 * @author JiFeiYe
 * @since 2024-03-06
 */
@Slf4j
@RestController
@RequestMapping("/category")
@Api(tags = "图书分类相关api")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    /**
     * 图书分类的分页查询
     *
     * @param page     当前页码
     * @param pageSize 页面大小
     * @return IPage
     */
    @GetMapping("/page")
    @ApiOperation("图书分类的分页查询")
    public R<IPage<Category>> getCategoryPage(
            @ApiParam(name = "page", value = "当前页码") Integer page,
            @ApiParam(name = "pageSize", value = "页面大小") Integer pageSize) {
        log.info("图书分类的分页查询, page:{}, pageSize:{}", page, pageSize);

        IPage<Category> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        p = categoryService.page(p, lqw);
        return R.success(p);
    }

    /**
     * 新增书籍分类
     *
     * @param category 图书分类详情
     * @return String
     */
    @PostMapping
    @ApiOperation("新增书籍分类")
    public R<String> addCategory(
            @ApiParam(name = "category", value = "图书分类详情") @RequestBody Category category) {
        log.info("新增书籍分类：{}", category);

        categoryService.save(category);
        return R.success("保存成功");
    }

    /**
     * 删除分类
     *
     * @param id 待删除分类id
     * @return R
     */
    @DeleteMapping
    @ApiOperation("删除分类")
    public R<String> deleteCategory(
            @ApiParam(name = "id", value = "待删除分类id") Long id) {
        log.info("开始删除分类：{}", id);

        categoryService.removeCategory(id);
        return R.success("删除成功！");
    }

    /**
     * 修改分类信息
     *
     * @param category 图书分类详情
     * @return R
     */
    @PutMapping
    @ApiOperation("修改分类信息")
    public R<String> editCategory(
            @ApiParam(name = "category", value = "图书分类详情") @RequestBody Category category) {
        log.info("修改分类信息：{}", category);
        categoryService.updateById(category);
        return R.success("修改成功！");
    }

    /**
     * 获取图书分类列表
     *
     * @param type 分类类型
     * @return List
     */
    @GetMapping("list")
    @ApiOperation("获取图书分类列表")
    public R<List<Category>> getCategoryList(
            @ApiParam(name = "type", value = "分类类型") Integer type) {
        log.info("获取图书分类列表，type: {}", type);

        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.eq(null != type, Category::getType, type).orderByDesc(Category::getUpdateTime);
        List<Category> lts = categoryService.list(lqw);
        return R.success(lts);
    }
}
