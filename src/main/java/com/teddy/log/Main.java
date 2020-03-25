package com.teddy.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 入口
 */
public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    /**
     * 程序入口
     * @param args 参数
     */
    public static void main(String[] args) throws InterruptedException {
        int i = 1;
        while (true) {
            log.error("这是第【{}】条测试日志", i++);
            Thread.sleep(2000);
        }

        // String fileName
        // =
        // "/home/code/CD_FY_AJBLXT/ajblxt/logs/ajblxt_stdout_172.16.69.1_192.168.202.1_172.17.0.1_172.25.8.130_.2020-02-27.log";
        // String pattern = "%d{yyyy-MM-dd HH:mm:ss} [%-5level] [%-5thread] %logger{20} - %msg%n";
        // String outDir = "";
        // String[] levels = new String[]{"DEBUG", "ERROR"};
        // String charset = "UTF-8";
        // LogAnalyse.analyse(fileName, pattern, outDir, levels, charset);
    }
}
