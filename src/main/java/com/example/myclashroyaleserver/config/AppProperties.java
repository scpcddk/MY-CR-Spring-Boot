package com.example.myclashroyaleserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String name = "my-clash-royale"; // 默认值
    // 你可以继续加字段，比如：
    // private int tickInterval;

    // 必须要有 getter / setter
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}