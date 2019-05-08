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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;

/**
 * 数据库分片策略
 * @author jay.xiang
 * @create 2019/4/29 19:56
 */
@Data
@Slf4j
@Service("preciseModuloDatabaseShardingAlgorithm")
public class DatabaseShardingAlgorithm implements PreciseShardingAlgorithm<Timestamp>{

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//定义格式，不显示毫秒
    @Autowired
    private ShardConfigMapper shardConfigMapper;

    /**
     * 
     * @param collection
     * @param preciseShardingValue
     * @return
     */
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Timestamp> preciseShardingValue) {
        String physicDatabase = null;
        Timestamp valueTime = preciseShardingValue.getValue();
        String orgValue = df.format(valueTime);
        String subValue = orgValue.substring(0, 4).replace("-", "");
        physicDatabase = getShardConfig(physicDatabase, subValue);
        if (StringUtils.isBlank(physicDatabase)) {
            log.info("----->该分片键值找不到对应的分库,默认取第一个库，分片键是={}，逻辑表是={},分片值是={}",preciseShardingValue.getColumnName(),preciseShardingValue.getLogicTableName(),preciseShardingValue.getValue());
            for (String value : collection) {
                physicDatabase = value;
                break;
            }
        }
        return physicDatabase;
    }


    public String getShardConfig(String physicDatabase ,String subValue ) {
        ShardConfig shardConfig = shardConfigMapper.selectByPrimaryKey(subValue);
        if (shardConfig != null) {
            physicDatabase = shardConfig.getConfigValue().split(",")[0];
        }
        return physicDatabase;

    }
}
