package com.jay.config;


import com.jay.mapper.nosharding.ShardConfigMapper;
import com.jay.model.ShardConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by xiang.wei on 2019/5/6
 *
 * @author xiang.wei
 */
@Data
@Slf4j
@Service("preciseModuloTableShardingAlgorithm")
public class CommonTableShardingAlgorithm implements PreciseShardingAlgorithm<String> {
    @Autowired
    private ShardConfigMapper shardConfigMapper;


    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<String> preciseShardingValue) {
        String physicsTable = null;
        physicsTable=setValue(preciseShardingValue);
        if (StringUtils.isBlank(physicsTable)) {
            log.info("----->该分片键值找不到对应的分表,默认取第一个表，分片键是={}，逻辑表是={},分片值是={}",preciseShardingValue.getColumnName(),preciseShardingValue.getLogicTableName(),preciseShardingValue.getValue());
            for (String value : collection) {
                physicsTable = value;
                break;
            }
        }
        log.info("----->该分片键值找到对应的分表，分片键是={}，逻辑表是={},分片值是={}",preciseShardingValue.getColumnName(),preciseShardingValue.getLogicTableName(),preciseShardingValue.getValue());
        return physicsTable;
    }

    /**
     * @param preciseShardingValue
     * @return
     */
    protected String setValue(PreciseShardingValue<String> preciseShardingValue) {
        String substring = preciseShardingValue.getValue().substring(0, 4);
        ShardConfig config = shardConfigMapper.selectByPrimaryKey(substring);
        if (config != null) {
            // TODO: 2019/5/8 需要调整
            String[] split = config.getConfigValue().split(",");
            return preciseShardingValue.getLogicTableName() + "_" + split[1];
        }
        return null;
    }

}
