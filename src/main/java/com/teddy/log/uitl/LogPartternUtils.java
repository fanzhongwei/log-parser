package com.teddy.log.uitl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import ch.qos.logback.classic.pattern.LineSeparatorConverter;
import ch.qos.logback.core.pattern.Converter;
import ch.qos.logback.core.pattern.LiteralConverter;

/**
 * 日志格式工具类
 */
public class LogPartternUtils {

    private static final Map<String, String> SPECIAL_MAP = new HashMap<>();
    static {
        SPECIAL_MAP.put("$", "\\$");
        SPECIAL_MAP.put("(", "\\(");
        SPECIAL_MAP.put(")", "\\)");
        SPECIAL_MAP.put("*", "\\*");
        SPECIAL_MAP.put("+", "\\+");
        SPECIAL_MAP.put(".", "\\.");
        SPECIAL_MAP.put("[", "\\[");
        SPECIAL_MAP.put("?", "\\?");
        SPECIAL_MAP.put("\\", "\\\\");
        SPECIAL_MAP.put("^", "\\^");
        SPECIAL_MAP.put("{", "\\{");
        SPECIAL_MAP.put("|", "\\|");
    }

    /**
     * 获取除开当前转换器，后面所有转换器的正则表达式
     *
     * @param converter Converter
     * @return 正则表达式
     */
    public static String getNextLiteralsPatterns(Converter converter) {
        return getPattern(converter.getNext());
    }

    private static String getPattern(Converter converter) {
        Converter next = converter;
        List<String> literals = new ArrayList<>();
        while (next != null && !(next instanceof LineSeparatorConverter)) {
            if (next instanceof LiteralConverter) {
                String literal = next.convert(null);
                literals.add(replaceSpecial(literal));
            }
            next = next.getNext();
        }
        // 最小匹配
        return StringUtils.join(literals, ".*?");
    }

    /**
     * 获取所有转换器的正则表达式
     *
     * @param converter Converter
     * @return 正则表达式
     */
    public static String getLiteralsPatterns(Converter converter) {
        return String.format("^.*%s.*$", getPattern(converter));
    }

    private static String replaceSpecial(String literal) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < literal.length(); i++) {
            String s = String.valueOf(literal.charAt(i));
            result.append(StringUtils.defaultIfBlank(SPECIAL_MAP.get(s), s));
        }
        return result.toString();
    }
}
