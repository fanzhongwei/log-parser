package com.teddy.log.parser;

import ch.qos.logback.core.pattern.Converter;
import ch.qos.logback.core.pattern.LiteralConverter;
import com.teddy.log.exception.ParseErrorException;
import org.apache.commons.lang3.StringUtils;

/**
 * 日志级别解析 {@link ch.qos.logback.classic.pattern.LevelConverter}
 */
public class LevelParser extends NextLiteralParser {
    @Override
    public String parseLog(String log, Converter converter) {
        // 日志级别后面应该有分隔符，否则解析不出来
        return super.parseLog(log, converter);
    }
}
