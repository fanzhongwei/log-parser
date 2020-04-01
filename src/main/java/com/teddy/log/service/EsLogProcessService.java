package com.teddy.log.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

    public String processLog(String param) {
        logger.debug("处理日志：{}", param);
        return param;
    }
}
