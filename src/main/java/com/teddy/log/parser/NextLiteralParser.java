package com.teddy.log.parser;

import ch.qos.logback.core.pattern.Converter;
import ch.qos.logback.core.pattern.LiteralConverter;
import com.teddy.log.exception.ParseErrorException;
import org.apache.commons.lang3.StringUtils;

/**
 * 解析线程
 */
public class NextLiteralParser implements IParser {
    @Override
    public String parseLog(String log, Converter converter) {
        Converter next = converter.getNext();
        if (!(next instanceof LiteralConverter)) {
            throw new ParseErrorException("该解析项后不存在分割符，无法解析，对应日志为：【{}】", log);
        }
        String nextStr = next.convert(null);
        return StringUtils.substringBefore(log, nextStr);
    }
}
