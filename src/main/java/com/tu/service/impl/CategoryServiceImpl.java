package com.tu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tu.common.CustomerException;
import com.tu.entity.Book;
import com.tu.entity.Category;
import com.tu.mapper.CategoryMapper;
import com.tu.service.IBookService;
import com.tu.service.ICategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 书籍分类 服务实现类
 * </p>
 *
 * @author JiFeiYe
 * @since 2024-03-06
 */
@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Autowired
    private IBookService bookService;

    @Override
    public void removeCategory(Long id) {

        log.info("检查子表book连接状态");
        LambdaQueryWrapper<Book> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Book::getCategoryId, id);
        long count1 = bookService.count(dishLambdaQueryWrapper);
        if (count1 > 0) {
            throw new CustomerException("该分类下仍有书籍，无法删除！");
        }

        super.removeById(id);
    }
}
