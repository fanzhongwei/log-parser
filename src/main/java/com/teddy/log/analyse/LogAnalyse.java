package com.teddy.log.analyse;

import com.teddy.log.bean.LogInfo;
import com.teddy.log.layout.LogLayout;
import com.teddy.log.split.LogSplit;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 日志分析
 */
public class LogAnalyse {
    private static final Logger log = LoggerFactory.getLogger(LogAnalyse.class);

    /**
     * 日志分析，将需要收集的级别的日志输出到文件
     *
     * @param fileName 日志文件路径
     * @param pattern 日志格式
     * @param outDir 输出目录，如果为空，则输出到日志所在目录
     * @param levels 需要收集的日志级别
     * @param charset 编码格式
     */
    public static void analyse(String fileName, String pattern, String outDir, String[] levels, String charset) {
        Map<String, List<LogInfo>> levelLogsMap = analyseLog(fileName, pattern, charset);

        levelLogsMap.forEach((level, logInfos) -> {
            if (ArrayUtils.contains(levels, level)) {
                // 根据相似度合并日志
                List<LogInfo> mergedLogInfo = mergeLogInfo(logInfos);
                writeToFile(fileName, outDir, logInfos, charset);
            }
        });
    }

    private static List<LogInfo> mergeLogInfo(List<LogInfo> logInfos) {
        // TODO https://zhuanlan.zhihu.com/p/91645988
        return logInfos;
    }

    private static void writeToFile(String logFileName, String outDir, List<LogInfo> logInfos, String charset) {
        String outFileName = FilenameUtils.getBaseName(logFileName) + "_" + logInfos.get(0).getLevel() + ".log";
        String outPath = StringUtils.defaultIfBlank(outDir, FilenameUtils.getFullPath(logFileName));
        File outFile = new File(outPath + File.separator + outFileName);
        try {
            FileUtils.forceMkdirParent(outFile);
            if (!outFile.exists() && !outFile.createNewFile()) {
                log.error("创建日志文件【{}】失败", outFile.getAbsolutePath());
                return;
            }
            List<String> logs = logInfos.stream().map(LogInfo::getLog).collect(Collectors.toList());
            FileUtils.writeLines(outFile, charset, logs);
        } catch (IOException e) {
            log.error("记录分析结果失败", e);
        }
    }

    private static Map<String, List<LogInfo>> analyseLog(String fileName, String pattern, String charset) {
        LogLayout logLayout = new LogLayout(pattern);
        List<String> splits = LogSplit.split(new File(fileName), logLayout, charset);
        if (CollectionUtils.isEmpty(splits)) {
            return Collections.emptyMap();
        }
        return splits.stream().map(s -> {
            try {
                return logLayout.parseLog(s);
            } catch (Exception e) {
                log.error("解析日志【{}】失败", s, e);
                return null;
            }
        }).filter(logInfo -> null != logInfo).collect(Collectors.groupingBy(LogInfo::getLevel));
    }
}
