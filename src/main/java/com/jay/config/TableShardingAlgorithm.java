package com.jay.config;

import com.jay.model.ShardConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.hint.HintShardingValue;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;

/**
 * @author jay.xiang
 * @create 2019/4/29 19:57
 */
public class TableShardingAlgorithm  extends AbstractShardingAlgorithm implements HintShardingAlgorithm<String> {


    @Override
    public Collection<String> doSharding(Collection<String> collection, HintShardingValue<String> hintShardingValue) {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        System.out.println("----->当前的年份是：" + year);
        ShardConfig config = getConfig(String.valueOf(year));
        System.out.println("---->当前配置是={}"+config);
        return null;
    }
}
