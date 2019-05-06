package com.jay.config;

import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * @author jay.xiang
 * @create 2019/5/6 9:21
 */
public class OrderDetailShardingAlgorithm implements ComplexKeysShardingAlgorithm<String> {

    @Override
    public Collection<String> doSharding(Collection<String> collection, ComplexKeysShardingValue<String> complexKeysShardingValue) {
        Collection<String> eachSet = new LinkedHashSet<>();
        Map<String, Collection<String>> nameAndShardingValuesMap = complexKeysShardingValue.getColumnNameAndShardingValuesMap();
//        if () {
//        }
        return null;
    }
}
