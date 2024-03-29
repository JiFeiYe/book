package com.tu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author: jfy
 * @Date: 2024/3/4
 */
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
@Slf4j
public class BookAppllication {

    public static void main(String[] args) {
        SpringApplication.run(BookAppllication.class, args);
        log.info("项目启动");
        
    }
}
