package com.jay.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by xiang.wei on 2019/5/8
 *
 * @author xiang.wei
 */
@Data
@Slf4j
@Service("orderDetailTableShardingAlgorithm")
public class OrderDetailTableShardingAlgorithm extends CommonTableShardingAlgorithm {

    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<String> preciseShardingValue) {
        return super.doSharding(collection, preciseShardingValue);
    }
}
