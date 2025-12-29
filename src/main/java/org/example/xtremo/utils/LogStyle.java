
package org.example.xtremo.utils;

/**
 *
 * @author Abdelrahman
 */
public enum LogStyle {
    TIMESTAMP("log-timestamp"),
    INFO("log-info"),
    WARN("log-warn"),
    SUCCESS("log-success"),
    ERROR("log-error");

    private final String cssClass;

    LogStyle(String cssClass) {
        this.cssClass = cssClass;
    }

    public String getCssClass() {
        return cssClass;
    }
}