package com.teddy.log.split;

import com.teddy.log.layout.LogLayout;
import com.teddy.log.uitl.LogPartternUtils;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 日志拆分，每条日志一行记录
 */
public class LogSplit {
    private static final Logger log = LoggerFactory.getLogger(LogSplit.class);

    /**
     * 分割日志文件，将其拆分为一条一条
     *
     * @param logFile 日志文件
     * @param logLayout 日志格式
     * @return 拆分后的日志文件
     */
    public static List<String> split(File logFile, LogLayout logLayout, String charsetName) {
        List<String> result = new ArrayList();
        try (FileInputStream inputStream = new FileInputStream(logFile);
             InputStreamReader reader = new InputStreamReader(inputStream, charsetName)){
            BufferedReader bufferedReader = new BufferedReader(reader);

            StringBuilder sb = new StringBuilder();
            String patternStr = String.format("^.*%s.*$", LogPartternUtils.getLiteralsPatterns(logLayout.getConverter()));
            Pattern pattern = Pattern.compile(patternStr);
            for(String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                if (StringUtils.isBlank(line)) {
                    continue;
                }
                // 如果该行满足定义的日志格式，则表示为新的行
                if (pattern.matcher(line).matches()) {
                    // 将上条日志记录
                    if (sb.length() > 0) {
                        result.add(sb.toString());
                    }
                    // 记录下一条日志
                    sb = new StringBuilder();
                    sb.append(line);
                } else {
                    // 否则可能是堆栈信息等
                    sb.append("\n").append(line);
                }
            }
            if (sb.length() > 0) {
                result.add(sb.toString());
            }
        } catch (Exception e) {
            log.error("解析日志文件【{}】失败", logFile, e);
        }
        return result;
    }

}
