package com.teddy.log.service;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.teddy.log.layout.LogLayout;

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

    @Autowired
    private LayoutService layoutService;

    /**
     * 处理发往ES的日志
     *
     * @param param 参数
     * @return 处理后的参数
     */
    public String processLog(String param) {
        logger.debug("处理前的日志：{}", param);
        String[] params = param.split("\n");
        String processed = Arrays.stream(params)
            .parallel()
            .map(this::process)
            .collect(Collectors.joining("\n"));
        // The bulk request must be terminated by a newline [\\n]"
        processed += "\n";
        logger.debug("处理后的日志：{}", processed);
        return processed;
    }

    private String process(String jsonStr){
        JSONObject json = JSON.parseObject(jsonStr);
        if (json.containsKey("message")) {
            logger.debug("处理日志：{}", jsonStr);
            String pattern = (String)JSONPath.eval(json, "$.fields.log_pattern");
            String message = (String)JSONPath.eval(json, "$.message");
            LogLayout layout = layoutService.getLayout(pattern);
            Map<String, String> parsedLog = layout.parseLogInfo(message);
            // 替换message
            json.put("message", parsedLog.remove("Message"));
            json.put("logInfo", parsedLog);
            // 替换采集时间为日志打印时间
            json.put("@timestamp", layoutService.getLogPrintTime(layout, parsedLog));
            return json.toString();
        }
        return jsonStr;
    }


}
