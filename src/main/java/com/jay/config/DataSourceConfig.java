package com.jay.config;


import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author jay.xiang
 * @create 2019/4/29 19:56
 */
@Configuration
@MapperScan(basePackages = {"com.jay.mapper"})
public class DataSourceConfig {
    

    @Bean(name = "shardingDataSource")
    DataSource getShardingDataSource() throws SQLException {
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(new TableRuleConfiguration("orders", "shard_order_${0..1}.orders${0..1}"));
        shardingRuleConfig.getTableRuleConfigs().add(new TableRuleConfiguration("orders_detail", "shard_order_${0..1}.orders_detail${0..1}"));
        shardingRuleConfig.getBindingTableGroups().add("orders, orders_detail");
        shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(new StandardShardingStrategyConfiguration("id", new DatabaseShardingAlgorithm()));
        shardingRuleConfig.setDefaultTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("id", new TableShardingAlgorithm()));
        return ShardingDataSourceFactory.createDataSource(createDataSourceMap(), shardingRuleConfig, new Properties());
    }


    @Bean
    @Primary
    public SqlSessionFactory sqlSessionFactory(DataSource shardingDataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(shardingDataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        return bean.getObject();
    }

    /**
     * 需要手动配置事务管理器
     *
     * @param shardingDataSource
     * @return
     */
    @Bean
    public DataSourceTransactionManager transactitonManager(DataSource shardingDataSource) {
        return new DataSourceTransactionManager(shardingDataSource);
    }

    private DataSource createDataSource(final String dataSourceName) {
        BasicDataSource result = new BasicDataSource();
        result.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
        result.setUrl(String.format("jdbc:mysql://localhost:3306/%s", dataSourceName));
        result.setUsername("root");
        result.setPassword("admin");
        return result;
    }

    private Map<String, DataSource> createDataSourceMap() {
        Map<String, DataSource> result = new HashMap<>();
        result.put("shard_order_0", createDataSource("shard_order_0"));
        result.put("shard_order_1", createDataSource("shard_order_1"));
        return result;
    }
}
