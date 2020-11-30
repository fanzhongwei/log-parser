package com.teddy.log.service;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.teddy.log.layout.LogLayout;

/**
 * package com.teddy.log.service <br/>
 * description: LayoutService <br/>
 *
 * @author fanzhongwei
 * @date 20-4-20
 */
@Service
public class LayoutService {

    private static final Logger logger = LoggerFactory.getLogger(LayoutService.class);

    /**
     * 获取layout，如果有缓存，则直接从缓存中获取
     *
     * @param pattern log pattern
     * @return LogLayout
     */
    @Cacheable(value = "layout", key = "#pattern")
    public LogLayout getLayout(String pattern) {
        return new LogLayout(pattern);
    }

    /**
     * 获取日志打印时间，解析失败时返回当前时间
     *
     * @param layout LogLayout
     * @param parsedLog 解析后的日志
     * @return 打印时间
     */
    public String getLogPrintTime(LogLayout layout, Map<String, String> parsedLog) {
        try {
            String pattern = layout.getDatePattern();
            Date datetime = DateUtils.parseDate(parsedLog.get("Date"), layout.getDatePattern(), "yyyy-MM-dd HH:mm:ss,SSS");
            return DateFormatUtils.format(datetime, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        } catch (ParseException e) {
            logger.error("获取日志打印时间失败", e);
            return DateFormatUtils.format(new Date(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        }
    }
}
