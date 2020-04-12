package com.teddy.log.layout;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.pattern.*;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.pattern.Converter;
import ch.qos.logback.core.pattern.ConverterUtil;
import ch.qos.logback.core.pattern.LiteralConverter;
import ch.qos.logback.core.pattern.parser.Parser;
import com.teddy.log.bean.LogInfo;
import com.teddy.log.parser.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.*;

/**
 * log layout
 */
public class LogLayout {

    private static final Logger log = LoggerFactory.getLogger(LogLayout.class);

    private Converter head;

    /** **Converter 对应的解析器 */
    public static final Map<Class, IParser> PARSER_MAP = new HashMap<>();

    private static final DefaultParser defaultParser = new DefaultParser();

    static {
        PARSER_MAP.put(DateConverter.class, new DateParser());
        PARSER_MAP.put(LevelConverter.class, new LevelParser());
        PARSER_MAP.put(ThreadConverter.class, new NextLiteralParser());
    }

    /**
     * 初始化日志pattern
     *
     * @param pattern 日志格式
     */
    public LogLayout(String pattern) {
        log.debug("初始化日志格式【{}】，开始...", pattern);
        try {
            Parser parser = new Parser(pattern);
            parser.setContext(new LoggerContext());
            this.head = parser.compile(parser.parse(), PatternLayout.defaultConverterMap);
            log.debug("Converter is {}", this.head);
            ConverterUtil.startConverters(this.head);
        } catch (Exception e) {
            log.error("初始化日志格式【{}】，失败",pattern, e);
            throw new IllegalArgumentException("pattern is illegal.");
        }
        log.debug("初始化日志格式【{}】，结束...", pattern);
    }

    /**
     * get converter head
     *
     * @return converter
     */
    public Converter getConverter() {
        return this.head;
    }

    /**
     * 解析日志
     *
     * @param logStr 日志
     * @return 日志信息
     */
    public LogInfo parseLog(String logStr) {
        Map<String, String> logMap = this.parseLogInfo(logStr);
        LogInfo logInfo = new LogInfo();
        logInfo.setLevel(logMap.get(StringUtils.removeEnd(LevelConverter.class.getSimpleName(), "Converter")));
        logInfo.setMessage(logMap.get(StringUtils.removeEnd(MessageConverter.class.getSimpleName(), "Converter")));
        logInfo.setLog(logStr);
        return logInfo;
    }

    /**
     * 获取日期格式
     *
     * @return 日期格式
     */
    public String getDatePattern() {
        Converter next = head;
        while (next != null) {
            if (next instanceof DateConverter) {
                return getDatePattern((DateConverter) next);
            }
        }
        return "yyyy-MM-dd HH:mm:ss,SSS";
    }

    private String getDatePattern(DateConverter next) {
        String datePattern = next.getFirstOption();
        if (datePattern == null) {
            datePattern = "yyyy-MM-dd HH:mm:ss,SSS";
        }

        if (datePattern.equals("ISO8601")) {
            datePattern = "yyyy-MM-dd HH:mm:ss,SSS";
        }
        return datePattern;
    }

    /**
     * 解析日志
     *
     * @param logStr 日志
     * @return 日志具体信息
     */
    public Map<String, String> parseLogInfo(String logStr) {
        // %d{yyyy-MM-dd HH:mm:ss:SSS , GMT+8} %m [%c:%L]-[%p] %n
        // 2020-03-14 15:06:11:214 初始化日志格式【%d{yyyy-MM-dd HH:mm:ss:SSS , GMT+8} %m [%c:%L]-[%p] %n】，开始... [com.teddy.log.layout.LogLayout:32]-[DEBUG]
        Converter next = head;
        Map<String, String> result = new LinkedHashMap<>();
        String currLog = logStr;
        while (next != null) {
            String parseLog;
            if (next instanceof LiteralConverter) {
                parseLog = next.convert(null);
            } else if (next instanceof LineSeparatorConverter) {
                parseLog = CoreConstants.LINE_SEPARATOR;
            } else {
                IParser parser = PARSER_MAP.getOrDefault(next.getClass(), defaultParser);
                parseLog = parser.parseLog(currLog, next);
                result.put(StringUtils.removeEnd(next.getClass().getSimpleName(), "Converter"), parseLog);
            }

            currLog = StringUtils.removeStart(currLog, parseLog);
            next = next.getNext();
        }
        log.debug("parse result:\n{}", result);
        return result;
    }
}
