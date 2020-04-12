package com.teddy.log.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.teddy.log.layout.LogLayout;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * package com.teddy.log.service <br/>
 * description: process filebeat send to elasticsearch logs <br/>
 *
 * @author fanzhongwei
 * @date 20-3-31
 */
@Service
public class EsLogProcessService {
    private static final Logger logger = LoggerFactory.getLogger(EsLogProcessService.class);

    /**
     * 处理发往ES的日志
     *
     * @param param 参数
     * @return 处理后的参数
     */
    public String processLog(String param) {
        logger.debug("处理前的日志：{}", param);
        String[] params = param.split("\n");
        String processed = Arrays.stream(params).map(this::process).collect(Collectors.joining("\n"));
        // The bulk request must be terminated by a newline [\\n]"
        processed += "\n";
        logger.debug("处理后的日志：{}", processed);
        return processed;
    }

    private String process(String jsonStr){
        JSONObject json = JSONObject.parseObject(jsonStr);
        if (json.containsKey("message")) {
            logger.debug("处理日志：{}", jsonStr);
            String pattern = (String) JSONPath.eval(json, "$.fields.pattern");
            String message  = (String) JSONPath.eval(json, "$.message");
            LogLayout layout = new LogLayout(pattern);
            Map<String, String> parsedLog = layout.parseLogInfo(message);
            json.put("logInfo", parsedLog);
            // 替换采集时间为日志打印时间
            json.put("@timestamp", getTimestamp(layout, parsedLog));
            return json.toString();
        }
        return jsonStr;
    }

    private String getTimestamp(LogLayout layout, Map<String, String> parsedLog) {
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
