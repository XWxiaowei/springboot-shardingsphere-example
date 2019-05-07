package com.jay.config;

import com.jay.model.ShardConfig;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.List;

/**
 * Created by xiang.wei on 2019/5/6
 *
 * @author xiang.wei
 */
@Data
public  class AbstractShardingAlgorithm   implements ComplexKeysShardingAlgorithm<String> {
    private String username0;
    private String url0;
    private String password0;

    public AbstractShardingAlgorithm() {
    }

    public AbstractShardingAlgorithm(String username0, String url0, String password0) {
        this.username0 = username0;
        this.url0 = url0;
        this.password0 = password0;
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
            System.out.println("url0, username0, password0={}"+username0);
            statement = conn.prepareStatement(sql);
            statement.setString(1, configkey);
            resultSet = statement.executeQuery();
            System.out.println("---->分页设置，参数={}"+configkey);
            if (resultSet != null) {
                while (resultSet.next()) {
                    System.out.println("---->分页设置，参数={}"+resultSet.next());
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
        return null;
    }
}
