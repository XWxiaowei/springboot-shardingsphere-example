package com.jay.config;

import com.jay.model.ShardConfig;

import java.util.Collection;

/**
 * @author jay.xiang
 * @create 2019/4/29 19:56
 */
public class DatabaseShardingAlgorithm extends CommonTableShardingAlgorithm {

    public DatabaseShardingAlgorithm(String username0, String url0, String password0, String tablePrefix) {
        super(username0, url0, password0, tablePrefix);
    }

    @Override
    protected void setCollect(Collection<String> result, Collection<String> values) {
        for (String value : values) {
            String substring = value.substring(0, 4);
            ShardConfig config = getConfig(substring);
            if (config != null) {
                System.out.println("DemoTableSharding.each(分库值)" + config);
                String[] split = config.getConfigValue().split(",");
                result.add(split[0]);
            }
        }
    }
}
