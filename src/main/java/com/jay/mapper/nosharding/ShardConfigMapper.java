package com.jay.mapper.nosharding;

import com.jay.model.ShardConfig;

import java.util.List;

public interface ShardConfigMapper {
    /**
     * selectByPrimaryKey
     * @param configKey
     * @return com.jay.model.ShardConfig
     */
    ShardConfig selectByPrimaryKey(String configKey);

    /**
     * selectAll
     * @param keys
     * @return java.util.List<com.jay.model.ShardConfig>
     */
    List<ShardConfig> selectByKey(List<String> keys);
}