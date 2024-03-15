package com.tu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tu.entity.Book;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 书籍管理 Mapper 接口
 * </p>
 *
 * @author JiFeiYe
 * @since 2024-03-06
 */
@Mapper
public interface BookMapper extends BaseMapper<Book> {

}
