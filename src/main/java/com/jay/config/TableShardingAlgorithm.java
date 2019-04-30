package com.jay.config;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * @author jay.xiang
 * @create 2019/4/29 19:57
 */
public class TableShardingAlgorithm  implements PreciseShardingAlgorithm<String> {
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<String> preciseShardingValue) {
        for (String each : collection) {
            if (each.endsWith(Long.parseLong(preciseShardingValue.getValue()) % 2+"")) {
                System.out.println("DemoTableSharding.each -> " + each + ";       preciseShardingValue-> " + preciseShardingValue.getValue());
                return each;
            }
        }
        throw new IllegalArgumentException();
    }

}
