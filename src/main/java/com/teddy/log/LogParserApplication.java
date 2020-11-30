package com.teddy.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * package com.teddy.log <br/>
 * description: 启动类 <br/>
 *
 * @author fanzhongwei
 * @date 20-3-25
 */
@SpringBootApplication
@EnableCaching
public class LogParserApplication {

    /**
     * 应用入口
     *
     * @param args 入口参数
     */
    public static void main(String[] args) {
        SpringApplication.run(LogParserApplication.class, args);
    }
}
