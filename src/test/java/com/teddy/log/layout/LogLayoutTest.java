package com.teddy.log.layout;

import ch.qos.logback.classic.pattern.DateConverter;
import com.teddy.log.split.LogSplit;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class LogLayoutTest {

    private static final Logger logger = LoggerFactory.getLogger(LogLayoutTest.class);

    /**
     * %d{yyyy-MM-dd HH:mm:ss:SSS , GMT+8} %m [%c:%L]-[%p] %n
     */
    @Test
    public void test_pattern_head_is_date_converters(){
        LogLayout layout = new LogLayout("%d{yyyy-MM-dd HH:mm:ss:SSS , GMT+8} %m [%c:%L]-[%p] %n");
        assertEquals(DateConverter.class, layout.getConverter().getClass());
    }

    @Test
    public void reg_pattern_test(){
        String log = "2020-03-14 15:06:11:214 初始化日志格式【%d{yyyy-MM-dd HH:mm:ss:SSS , GMT+8} %m [%c:%L]-[%p] %n】，开始... [com.teddy.log.layout.LogLayout:32]-[DEBUG]";
        Pattern pattern = Pattern.compile("( \\[.*:.*])");
        Matcher matcher = pattern.matcher(log);

        logger.debug("is find:{}", matcher.find());
        logger.debug("last gourp is:{}", matcher.group(1));
    }

    @Test
    public void test_log_parse() {
        String log = "2020-03-14 15:06:11:214 初始化日志格式【%d{yyyy-MM-dd HH:mm:ss:SSS , GMT+8} %m [%c:%L]-[%p] %n】，开始... [com.teddy.log.layout.LogLayout:32]-[DEBUG] ";
        LogLayout layout = new LogLayout("%d{yyyy-MM-dd HH:mm:ss:SSS , GMT+8} %m [%c:%L]-[%p] %n");
        Map<String, String> logs = layout.parseLogInfo(log);

        assertArrayEquals(new String[]{
                "2020-03-14 15:06:11:214",
                "初始化日志格式【%d{yyyy-MM-dd HH:mm:ss:SSS , GMT+8} %m [%c:%L]-[%p] %n】，开始...",
                "com.teddy.log.layout.LogLayout",
                "32",
                "DEBUG"
        }, logs.values().toArray());
    }

    @Test
    public void test_log_parse_1(){
        String log = "2020-03-14 14:48:43.915 [main] DEBUG com.teddy.log.layout.LogLayout - 初始化日志格式【%d{yyyy-MM-dd HH:mm:ss:SSS , GMT+8} %m [%c:%L]-[%p] %n】，结束...";
        LogLayout layout = new LogLayout("%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n");
        Map<String, String> logs = layout.parseLogInfo(log);

        assertArrayEquals(new String[]{
                "2020-03-14 14:48:43.915",
                "main",
                "DEBUG",
                "com.teddy.log.layout.LogLayout",
                "初始化日志格式【%d{yyyy-MM-dd HH:mm:ss:SSS , GMT+8} %m [%c:%L]-[%p] %n】，结束..."
        }, logs.values().toArray());
    }

    @Test
    public void test_log_parse_2(){
        String log = "2020-03-14 20:24:11.345 [DEBUG] [main] c.t.l.l.LogLayout - 初始化日志格式【%d{yyyy-MM-dd HH:mm:ss:SSS , GMT+8} %m [%c:%L]-[%p] %n】，结束...";
        LogLayout layout = new LogLayout("%d{yyyy-MM-dd HH:mm:ss.SSS} [%-5level] [%thread] %logger{20} - %msg%n");
        Map<String, String> logs = layout.parseLogInfo(log);

        assertArrayEquals(new String[]{
                "2020-03-14 20:24:11.345",
                "DEBUG",
                "main",
                "c.t.l.l.LogLayout",
                "初始化日志格式【%d{yyyy-MM-dd HH:mm:ss:SSS , GMT+8} %m [%c:%L]-[%p] %n】，结束..."
        }, logs.values().toArray());
    }

    @Test
    public void test_log_parse_3(){
        String log = "2020-03-14 20:25:52.251 [DEBUG] [main] c.t.l.l.LogLayout - 初始化日志格式【%d{yyyy-MM-dd HH:mm:ss.SSS} [%-5level] [%thread] %logger{20} - %msg%n】，结束...";
        LogLayout layout = new LogLayout("%d{yyyy-MM-dd HH:mm:ss.SSS} [%-5level] [%thread] %logger{20} - %msg%n");
        Map<String, String> logs = layout.parseLogInfo(log);

        assertArrayEquals(new String[]{
                "2020-03-14 20:25:52.251",
                "DEBUG",
                "main",
                "c.t.l.l.LogLayout",
                "初始化日志格式【%d{yyyy-MM-dd HH:mm:ss.SSS} [%-5level] [%thread] %logger{20} - %msg%n】，结束..."
        }, logs.values().toArray());
    }

    @Test
    public void test_file_have_39_logs(){
        File logFile = new File("/home/code/log-parser/src/test/resources/test_log.log");
        LogLayout layout = new LogLayout("%d{yyyy-MM-dd HH:mm:ss.SSS} [%-5level] [%thread] %logger{20} - %msg%n");
        List<String> splitLogs = LogSplit.split(logFile, layout,"UTF-8");

        assertEquals(39, splitLogs.size());
    }
}