package com.jay.model;

public class ShardConfig {
    /**
     * 配置键
     */
    private String configKey;

    /**
     * 配置值
     */
    private String configValue;

    /**
     * 配置键
     * @return config_key 配置键
     */
    public String getConfigKey() {
        return configKey;
    }

    /**
     * 配置键
     * @param configKey 配置键
     */
    public void setConfigKey(String configKey) {
        this.configKey = configKey == null ? null : configKey.trim();
    }

    /**
     * 配置值
     * @return config_value 配置值
     */
    public String getConfigValue() {
        return configValue;
    }

    /**
     * 配置值
     * @param configValue 配置值
     */
    public void setConfigValue(String configValue) {
        this.configValue = configValue == null ? null : configValue.trim();
    }

    @Override
    public String toString() {
        return "ShardConfig{" +
                "configKey='" + configKey + '\'' +
                ", configValue='" + configValue + '\'' +
                '}';
    }
}