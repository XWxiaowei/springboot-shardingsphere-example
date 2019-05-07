package com.jay.config;

import com.jay.model.ShardConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;
import org.apache.shardingsphere.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.hint.HintShardingValue;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * @author jay.xiang
 * @create 2019/4/29 19:57
 */
public class TableShardingAlgorithm  extends AbstractShardingAlgorithm implements ComplexKeysShardingAlgorithm<String> {

    public TableShardingAlgorithm() {
    }

    public TableShardingAlgorithm(String username0, String url0, String password0) {
        super(username0, url0, password0);
    }

    @Override
    public Collection<String> doSharding(Collection<String> collection, ComplexKeysShardingValue<String> complexKeysShardingValue) {
        Collection<String> result = new LinkedHashSet<>();
        Map<String, Collection<String>> columnNameAndShardingValuesMap = complexKeysShardingValue.getColumnNameAndShardingValuesMap();
        if (columnNameAndShardingValuesMap.containsKey("id")
                &&!columnNameAndShardingValuesMap.containsKey("adddate")) {
            Collection<String> values = columnNameAndShardingValuesMap.get("id");
            for (String value : values) {
                String substring = value.substring(0, 4);
                ShardConfig config = getConfig(substring);
                if (config != null) {
                    System.out.println("DemoTableSharding.each(分表值)"+config);
                    result.add(config.getConfigValue());
                }
            }
        }
        return result;
    }


//    @Override
//    public Collection<String> doSharding(Collection<String> collection, HintShardingValue<String> hintShardingValue) {
//        Calendar now = Calendar.getInstance();
//        int year = now.get(Calendar.YEAR);
//        System.out.println("----->当前的年份是：" + year);
//        ShardConfig config = getConfig(String.valueOf(year));
//        System.out.println("---->当前配置是={}"+config);
//        return null;
//    }
}
