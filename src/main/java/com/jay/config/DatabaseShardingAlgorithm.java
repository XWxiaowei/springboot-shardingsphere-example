package com.jay.config;

import com.jay.model.ShardConfig;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * @author jay.xiang
 * @create 2019/4/29 19:56
 */
public class DatabaseShardingAlgorithm extends CommonTableShardingAlgorithm {

    public DatabaseShardingAlgorithm(String username0, String url0, String password0, String tablePrefix) {
        super(username0, url0, password0, tablePrefix);
    }

    @Override
    public Collection<String> doSharding(Collection<String> collection, ComplexKeysShardingValue<String> complexKeysShardingValue) {
        Collection<String> result = new LinkedHashSet<>();
        Map<String, Collection<String>> columnNameAndShardingValuesMap = complexKeysShardingValue.getColumnNameAndShardingValuesMap();
        if (columnNameAndShardingValuesMap.containsKey("id")) {
            Collection<String> values = columnNameAndShardingValuesMap.get("id");
            setCollect(result, values);
        }
        if (columnNameAndShardingValuesMap.containsKey("adddate") && CollectionUtils.isEmpty(result)) {
            Collection<String> values = columnNameAndShardingValuesMap.get("adddate");
            String[] valueStrs = values.toArray(new String[values.size()]);
//            有时间段的，要将时间段内所有的年份都要找出来
            if (valueStrs.length == 2) {
                String startTime = valueStrs[0].substring(0,4);
                String endTime = valueStrs[1].substring(0, 4);
                int stepLength = Integer.valueOf(endTime) - Integer.valueOf(startTime);
                for (int i = 0; i < stepLength+1; i++) {
                    ShardConfig config = getConfig(String.valueOf(Integer.valueOf(startTime) + i));
                    if (config != null) {
                        String[] split = config.getConfigValue().split(",");
                        result.add(split[0]);
                    }
                }
            }
            //单个时间点的
            else if (valueStrs.length == 1) {
                String startTime = valueStrs[0].substring(0, 4);
                ShardConfig config = getConfig(startTime);
                if (config != null) {
                    String[] split = config.getConfigValue().split(",");
                    result.add(split[0]);
                }
            }
        }
        if (columnNameAndShardingValuesMap.containsKey("orders_id") && CollectionUtils.isEmpty(result)) {
            Collection<String> values = columnNameAndShardingValuesMap.get("orders_id");
            setCollect(result, values);
        }
        if (CollectionUtils.isEmpty(result)) {
            result = collection;
        }
        System.out.println("DemoTableSharding.each(分库值)" + result);
        return result;
    }

    @Override
    protected void setCollect(Collection<String> result, Collection<String> values) {
        for (String value : values) {
            if (value.length() >= 4) {
                String substring = value.substring(0, 4);
                ShardConfig config = getConfig(substring);
                if (config != null) {
                    String[] split = config.getConfigValue().split(",");
                    result.add(split[0]);
                }
            }
        }
    }
}
