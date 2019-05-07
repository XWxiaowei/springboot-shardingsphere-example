package com.jay.config;

import com.jay.model.ShardConfig;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xiang.wei on 2019/5/6
 *
 * @author xiang.wei
 */
public abstract class AbstractShardingAlgorithm {
    private static HashMap hashMap;
    private String url0 = (String) hashMap.get("spring.shardingsphere.datasource.shard_order_0.url");
    private String username0 = (String) hashMap.get("spring.shardingsphere.datasource.shard_order_0.username");
    private String password0 = (String) hashMap.get("spring.shardingsphere.datasource.shard_order_0.password");

    static {
        Yaml yaml = new Yaml();
        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.yml");
        hashMap = yaml.loadAs(resourceAsStream, HashMap.class);

    }

    public ShardConfig getConfig(String configkey) {
        ShardConfig shardConfig = new ShardConfig();

        String sql = "select config_key, config_value from shard_config where config_key=?";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String driverName = "com.mysql.jdbc.Driver";
            Class.forName(driverName);
            conn = DriverManager.getConnection(url0, username0, password0);
            statement = conn.prepareStatement(sql);
            List<String> params = new ArrayList<>();
            params.add(configkey);
            String paramStr = StringUtils.join(params, ",");
            statement.setString(1, paramStr);
            resultSet = statement.executeQuery();
            if (resultSet != null) {
                while (resultSet.next()) {
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
}
