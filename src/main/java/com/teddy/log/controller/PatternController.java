package com.teddy.log.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teddy.log.layout.LogLayout;
import com.teddy.log.uitl.LogPartternUtils;

/**
 * package com.teddy.log.controller <br/>
 * description: 日志格式解析 <br/>
 *
 * @author fanzhongwei
 * @date 20-3-25
 */
@RestController
public class PatternController {

    /**
     * 解析日志格式，转换为正则表达式
     *
     * @param pattern 日志格式
     * @return 正则
     */
    @GetMapping("/api/v1/parse/pattern")
    public ResponseEntity<String> parsePattern(@RequestParam("pattern") String pattern) {
        final String logPattern = LogPartternUtils.getLiteralsPatterns(new LogLayout(pattern).getConverter());
        return ResponseEntity.ok(logPattern);
    }
}
