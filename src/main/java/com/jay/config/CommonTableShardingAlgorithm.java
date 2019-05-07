package com.jay.config;

import com.jay.model.ShardConfig;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by xiang.wei on 2019/5/6
 *
 * @author xiang.wei
 */
@Data
public class CommonTableShardingAlgorithm implements ComplexKeysShardingAlgorithm<String> {
    private String username0;
    private String url0;
    private String password0;
    private String tablePrefix;


    public CommonTableShardingAlgorithm(String username0, String url0, String password0, String tablePrefix) {
        this.username0 = username0;
        this.url0 = url0;
        this.password0 = password0;
        this.tablePrefix = tablePrefix;
    }

    public ShardConfig getConfig(String configkey) {
        ShardConfig shardConfig = null;

        String sql = "select config_key, config_value from shard_config where config_key=?";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String driverName = "com.mysql.jdbc.Driver";
            Class.forName(driverName);
            conn = DriverManager.getConnection(url0, username0, password0);
            System.out.println("url0, username0, password0={}" + username0);
            statement = conn.prepareStatement(sql);
            statement.setString(1, configkey);
            resultSet = statement.executeQuery();
            System.out.println("---->分页设置，参数={}" + configkey);
            if (resultSet != null) {
                while (resultSet.next()) {
                    shardConfig = new ShardConfig();
                    shardConfig.setConfigKey(resultSet.getString("config_key"));
                    shardConfig.setConfigValue(resultSet.getString("config_value"));
                }
            }
        } catch (Exception e) {

        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return shardConfig;

    }

    @Override
    public Collection<String> doSharding(Collection<String> collection, ComplexKeysShardingValue<String> complexKeysShardingValue) {
        Collection<String> result = new LinkedHashSet<>();
        Map<String, Collection<String>> columnNameAndShardingValuesMap = complexKeysShardingValue.getColumnNameAndShardingValuesMap();
        if (columnNameAndShardingValuesMap.containsKey("id")) {
            Collection<String> values = columnNameAndShardingValuesMap.get("id");
            setCollect(result, values);
        } else if (columnNameAndShardingValuesMap.containsKey("adddate")) {
            Collection<String> values = columnNameAndShardingValuesMap.get("adddate");
            setCollect(result, values);
        } else if (columnNameAndShardingValuesMap.containsKey("orders_id")) {
            Collection<String> values = columnNameAndShardingValuesMap.get("adddate");
            setCollect(result, values);
        }
        if (CollectionUtils.isEmpty(result)) {
            result = collection;
        }
        return result;
    }

    /**
     * @param result
     * @param values
     */
    protected void setCollect(Collection<String> result,Collection<String> values) {
        for (String value : values) {
            String substring = value.substring(0, 4);
            ShardConfig config = getConfig(substring);
            if (config != null) {
                System.out.println("DemoTableSharding.each(分表值)" + config);
                String[] split = config.getConfigValue().split(",");
                result.add(tablePrefix+"_"+ split[1]);
            }
        }
    }
}
