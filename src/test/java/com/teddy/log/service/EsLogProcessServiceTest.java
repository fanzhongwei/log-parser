package com.teddy.log.service;

import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

public class EsLogProcessServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(EsLogProcessServiceTest.class);

    @Test
    @Ignore
    public void processLog() throws IOException {
        String logs = FileUtils.readFileToString(ResourceUtils.getFile("classpath:filebeat-to-es.json"), "UTF-8");
        EsLogProcessService processService = new EsLogProcessService();
        String processedLog = processService.processLog(logs);

        Assert.assertNotNull(processedLog);
    }
}
