package com.govtech.notification.application.configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "notifications")
public class NotificationProperties {

    private Map<String, Template> templates = new HashMap<>();

    @Getter
    @Setter
    public static class Template {
        private String title;
        private String message;
    }
}