package com.teddy.log.parser;

import ch.qos.logback.classic.pattern.DateConverter;
import ch.qos.logback.core.pattern.Converter;
import org.apache.commons.lang3.StringUtils;

/**
 * 日期解析，对应{@link ch.qos.logback.classic.pattern.DateConverter}
 */
public class DateParser implements IParser {

    @Override
    public String parseLog(String log, Converter converter) {
        String datePattern = ((DateConverter) converter).getFirstOption();
        return StringUtils.left(log, datePattern.length());
    }
}
