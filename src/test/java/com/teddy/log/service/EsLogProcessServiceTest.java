package com.teddy.log.service;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.IOException;

import static org.junit.Assert.*;

public class EsLogProcessServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(EsLogProcessServiceTest.class);

    @Test
    public void processLog() throws IOException {
        String logs = FileUtils.readFileToString(ResourceUtils.getFile("classpath:filebeat-to-es.json"), "UTF-8");
        EsLogProcessService processService = new EsLogProcessService();
        String processedLog = processService.processLog(logs);

        Assert.assertNotNull(processedLog);
    }
}