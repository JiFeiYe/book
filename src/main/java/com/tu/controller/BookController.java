package com.tu.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tu.common.R;
import com.tu.dto.BookDto;
import com.tu.entity.Book;
import com.tu.entity.Category;
import com.tu.entity.ShoppingCart;
import com.tu.service.IBookService;
import com.tu.service.ICategoryService;
import com.tu.service.IShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 书籍管理 前端控制器
 * </p>
 *
 * @author JiFeiYe
 * @since 2024-03-06
 */
@RestController
@RequestMapping("/book")
@Slf4j
@Api(tags = "图书相关api")
public class BookController {

    @Autowired
    private IBookService bookService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IShoppingCartService shoppingCartService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增图书
     *
     * @param book 图书
     * @return String
     */
    @PostMapping
    @ApiOperation("新增图书")
    public R<String> addBook(
            @ApiParam(name = "book", value = "图书") @RequestBody Book book) {
        log.info("新增图书 book: {}", book);

        bookService.save(book);
        String key = "book_" + book.getCategoryId() + "_1";
        redisTemplate.delete(key);

        return R.success("保存成功");
    }

    /**
     * 图书查询分页
     *
     * @param page     当前页面
     * @param pageSize 页面大小
     * @param name     模糊查询
     * @return IPage
     */
    @GetMapping("/page")
    @ApiOperation("图书查询分页")
    public R<IPage<BookDto>> getBookPage(
            @ApiParam(name = "page", value = "当前页码") Integer page,
            @ApiParam(name = "pageSize", value = "页面大小") Integer pageSize,
            @ApiParam(name = "name", value = "查询名字关键字") String name) {
        log.info("图书查询分页");
        log.info("page:{}, pageSize:{}, name:{}", page, pageSize, name);

        // 获取主表分页信息
        IPage<Book> bookIPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Book> lqw = new LambdaQueryWrapper<>();
        lqw.like(StringUtils.isNotEmpty(name), Book::getName, name)
                .orderByDesc(Book::getUpdateTime);
        bookService.page(bookIPage, lqw);

        // 将主表分页信息拷贝到BookDto中
        IPage<BookDto> bookDtoIPage = new Page<>();
        BeanUtils.copyProperties(bookIPage, bookDtoIPage, "records");
        // 遍历主表records， 往BookDto中补充categoryName
        List<Book> records = bookIPage.getRecords();
        List<BookDto> bookDtos = records.stream().map((item) -> {
            BookDto bookDto = new BookDto();
            BeanUtils.copyProperties(item, bookDto);
            // 查询出categoryName，若非空则将其放进bookDto
            Category category = categoryService.getById(item.getCategoryId());
            if (category != null) {
                bookDto.setCategoryName(category.getName());
            }
            return bookDto;
        }).toList();
        bookDtoIPage.setRecords(bookDtos);

        return R.success(bookDtoIPage);
    }

    /**
     * 图书查询
     *
     * @param id 图书id
     * @return Book
     */
    @GetMapping("/{id}")
    @ApiOperation("图书查询")
    public R<Book> getBook(
            @ApiParam(name = "id", value = "图书id") @PathVariable Long id) {
        log.info("图书查询 id:{}", id);

        Book book = bookService.getById(id);
        return R.success(book);
    }

    /**
     * 图书修改
     *
     * @param book 图书
     * @return String
     */
    @PutMapping
    @ApiOperation("图书修改")
    public R<String> editBook(
            @ApiParam(name = "book", value = "图书") @RequestBody Book book) {
        log.info("书籍多表修改book：{}", book);

        bookService.updateById(book);
        String key = "book_" + book.getCategoryId() + "_1";
        redisTemplate.delete(key);

        return R.success("修改成功！");
    }

    /**
     * 删除图书
     *
     * @param ids BookId列表
     * @return String
     */
    @Transactional
    @DeleteMapping
    @ApiOperation("删除图书")
    public R<String> deleteBook(
            @ApiParam(name = "ids", value = "BookId列表") @RequestParam List<Long> ids) {
        log.info("删除图书 bookIds:{}", ids);

        // 删除缓存
        LambdaQueryWrapper<Book> bookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        bookLambdaQueryWrapper.in(Book::getId, ids);
        List<Book> bookes = bookService.list(bookLambdaQueryWrapper);
        for (Book book : bookes) {
            Long categoryId = book.getCategoryId();
            String key = "book_" + categoryId + "_1";
            log.info("key:{}", key);
            redisTemplate.delete(key);
        }

        bookService.removeBatchByIds(ids);
        return R.success("删除成功");
    }

    /**
     * 单独/批量停售
     *
     * @param ids BookId列表
     * @return String
     */
    @PostMapping("/status/0")
    @ApiOperation("单独/批量停售")
    public R<String> bookStatusByStatus1(
            @ApiParam(name = "ids", value = "BookId列表") @RequestParam List<Long> ids) {
        log.info("单独/批量停售 BookIds:{}", ids);

        LambdaQueryWrapper<Book> lqw = new LambdaQueryWrapper<>();
        lqw.in(Book::getId, ids);
        Book book = new Book();
        book.setStatus(0);
        bookService.update(book, lqw);
        return R.success("停售成功！");
    }

    /**
     * 单独/批量启售
     *
     * @param ids BookId列表
     * @return String
     */
    @PostMapping("/status/1")
    @ApiOperation("单独/批量启售")
    public R<String> bookStatusByStatus2(
            @ApiParam(name = "ids", value = "BookId列表") @RequestParam List<Long> ids) {
        log.info("单独/批量启售 BookIds:{}", ids);

        LambdaQueryWrapper<Book> lqw = new LambdaQueryWrapper<>();
        lqw.in(Book::getId, ids);
        Book book = new Book();
        book.setStatus(1);
        bookService.update(book, lqw);
        return R.success("启售成功！");
    }

    /**
     * 图书分类查询对应的图书数据
     *
     * @param categoryId 图书分类ID
     * @return List-Book
     */
    @GetMapping("/list")
    @ApiOperation("图书分类查询对应的图书数据")
    public R<List<BookDto>> queryBookList(
            @ApiParam(name = "categoryId", value = "图书分类ID") Long categoryId) {
        log.info("图书分类查询对应的图书数据 categoryId: {}", categoryId);

        String key = "book_" + categoryId + "_1";
        List<BookDto> bookDtos = (List<BookDto>) redisTemplate.opsForValue().get(key);
        if (bookDtos != null) {
            return R.success(bookDtos);
        }

        LambdaQueryWrapper<Book> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Book::getCategoryId, categoryId)
                .eq(Book::getStatus, 1);
        List<Book> bookes = bookService.list(lqw);

        bookDtos = bookes.stream().map((book) -> {
            BookDto bookDto = new BookDto();
            BeanUtils.copyProperties(book, bookDto);

            Category category = categoryService.getById(book.getCategoryId());
            bookDto.setCategoryName(category.getName());

            LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getBookId, book.getId());
            ShoppingCart shoppingCart = shoppingCartService.getOne(shoppingCartLambdaQueryWrapper);
            if (shoppingCart != null) {
                bookDto.setNumber(shoppingCart.getNumber());
            }

            return bookDto;
        }).toList();

        redisTemplate.opsForValue().set(key, bookDtos, 30, TimeUnit.MINUTES);
        return R.success(bookDtos);
    }
}
