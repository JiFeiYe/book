package com.tu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tu.entity.Book;
import com.tu.mapper.BookMapper;
import com.tu.service.IBookService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 书籍管理 服务实现类
 * </p>
 *
 * @author JiFeiYe
 * @since 2024-03-06
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements IBookService {

//    @Autowired
//    private IDishFlavorService dishFlavorService;
//
//    /**
//     * 保存Dish，连带保存DishFlavor
//     * Dish : DishFlavor = 1 : m （一对多表）
//     *
//     * @param bookDto 主从混合表
//     */
//    @Transactional
//    @Override
//    public void saveWithFlavor(BookDto bookDto) {
//        // 先保存主表记录（多余无关列不影响）
//        this.save(bookDto);
//
//        // 再保存从表记录
//        // 先从主表查询id放到从表中
//        Long dishId = bookDto.getId();
//        List<DishFlavor> flavors = bookDto.getFlavors();
//        // 遍历flavors
//        flavors = flavors.stream().map((item) -> {
//            item.setDishId(dishId);
//            return item;
//        }).toList(); // todo: .collect(Collectors.toList()) 与 .toList() ?
//        dishFlavorService.saveBatch(flavors);
//    }
//
//    /**
//     * 修改Dish与DishFlavor
//     *
//     * @param bookDto 主从混合表
//     */
//    @Transactional
//    @Override
//    public void updateWithFlavor(BookDto bookDto) {
//        // 先更新主表
//        this.updateById(bookDto);
//
//        // 再更新从表（删除对应字段，重新插入新字段）
//        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, bookDto.getId());
//        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);
//
//        List<DishFlavor> flavors = bookDto.getFlavors();
//        flavors = flavors.stream().map((item) -> {
//            item.setDishId(bookDto.getId());
//            return item;
//        }).toList();
//
//        dishFlavorService.saveBatch(flavors);
//    }


}
