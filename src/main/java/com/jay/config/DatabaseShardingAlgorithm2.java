package com.jay.config;

import com.zjaisino.order.dao.ShardConfigMapper;
import com.zjaisino.order.model.ShardConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * 数据库分片策略
 * @author jay.xiang
 * @create 2019/4/29 19:56
 */
@Data
@Slf4j
@Service("preciseModuloDatabaseShardingAlgorithm")
public class DatabaseShardingAlgorithm2 implements PreciseShardingAlgorithm<String> {

    /**
     * TODO 需要指定主库
     */
    @Autowired
    private ShardConfigMapper shardConfigMapper;

    /**
     * 
     * @param collection
     * @param preciseShardingValue
     * @return
     */
    // TODO: 2019/5/8 需要调整
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<String> preciseShardingValue) {
        String physicDatabase = null;
        String subValue = preciseShardingValue.getValue().substring(0, 7).replace("-", "");
        ShardConfig shardConfig = shardConfigMapper.selectByPrimaryKey(subValue);
        if (shardConfig != null) {
            physicDatabase = shardConfig.getConfigValue().split(",")[0];
        }
        if (StringUtils.isBlank(physicDatabase)) {
            // TODO: 2019/5/8 需要调整
            log.info("----->该分片键值找不到对应的分库,默认取第一个库，分片键是={}，逻辑表是={},分片值是={}",preciseShardingValue.getColumnName(),preciseShardingValue.getLogicTableName(),preciseShardingValue.getValue());
            for (String value : collection) {
                physicDatabase = value;
                break;
            }
        }
        return physicDatabase;
    }
}
