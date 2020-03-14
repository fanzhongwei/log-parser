package com.teddy.log.parser;

import ch.qos.logback.core.pattern.Converter;
import com.teddy.log.uitl.LogPartternUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * parse {@link ch.qos.logback.classic.pattern.MessageConverter}
 */
public class DefaultParser implements IParser {

    private static final Logger logger = LoggerFactory.getLogger(DefaultParser.class);

    @Override
    public String parseLog(String log, Converter converter) {
        String nextLiterals = LogPartternUtils.getNextLiteralsPatterns(converter);
        if (StringUtils.isBlank(nextLiterals)) {
            return log;
        }
        String nextLiteralsPattern = String.format("(%s)", nextLiterals);

        Pattern pattern = Pattern.compile(nextLiteralsPattern);
        Matcher matcher = pattern.matcher(log);

        // 找到最后一个匹配的非massage
        String next = null;
        while (matcher.find()) {
            next = matcher.group();
            logger.debug("find group {}", next);
        }
        return StringUtils.left(log, StringUtils.indexOf(log, next));
    }

}
