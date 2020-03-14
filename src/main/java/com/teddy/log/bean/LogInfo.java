package com.teddy.log.bean;

/**
 * 日志信息
 */
public class LogInfo {
    /**
     * 日志字符串
     */
    private String log;

    /** 日志级别 */
    private String level;

    /** 具体信息 */
    private String message;

    /** 日志出现的次数 */
    private Integer times;

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }
}
