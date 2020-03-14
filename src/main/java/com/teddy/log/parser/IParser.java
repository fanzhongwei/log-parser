package com.teddy.log.parser;

import ch.qos.logback.core.pattern.Converter;

/**
 * 日志解析接口
 */
public interface IParser {

    /**
     * 解析日志
     *
     * @param log 日志
     * @param converter 转换器
     * @return 解析出来的内容
     */
    String parseLog(String log, Converter converter);
}
