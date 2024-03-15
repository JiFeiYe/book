package com.tu.dto;

import com.tu.entity.Book;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author JiFeiYe
 * @since 2024/3/7
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BookDto extends Book {

    private String categoryName;

    // 记录购物车里面有多少
    private Integer Number;
}
