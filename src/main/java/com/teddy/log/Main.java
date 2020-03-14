package com.teddy.log;

import com.teddy.log.analyse.LogAnalyse;

/**
 * 入口
 */
public class Main {

    /**
     * 程序入口
     * @param args 参数
     */
    public static void main(String[] args) {
        String fileName = "/home/code/log-parser/src/test/resources/test_log.log";
        String pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%-5level] [%thread] %logger{20} - %msg%n";
        String outDir = "";
        String[] levels = new String[]{"DEBUG", "ERROR"};
        String charset = "UTF-8";
        LogAnalyse.analyse(fileName, pattern, outDir, levels, charset);
    }
}
