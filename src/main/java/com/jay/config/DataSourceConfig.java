package com.jay.config;


import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.ComplexShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
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
@MapperScan(basePackages = "com.jay.mapper", sqlSessionTemplateRef = "testSqlSessionTemplate")
public class DataSourceConfig {
    @Value("${spring.shardingsphere.sharding.tables.orders.actualDataNodes}")
    private String ordersActualDataNodes;
    @Value("${spring.shardingsphere.sharding.tables.orders_detail.actualDataNodes}")
    private String ordersDetailActualDataNodes;

    private String ordersLogicTable="orders";

    private String ordersDetailLogicTable="orders_detail";

    private String defaultDataSource="shard_order_0";

    private String shardOrder1DataSource="shard_order_1";

    @Value("${mybatis.mapper-locations}")
    private String mapperLocations;

    @Value("${spring.shardingsphere.datasource.names}")
    private String names;

    @Value("${spring.shardingsphere.datasource.shard_order_0.username}")
    private String username0;
    @Value("${spring.shardingsphere.datasource.shard_order_0.url}")
    private String url0;
    @Value("${spring.shardingsphere.datasource.shard_order_0.password}")
    private String password0;

    @Value("${spring.shardingsphere.datasource.shard_order_1.username}")
    private String username1;
    @Value("${spring.shardingsphere.datasource.shard_order_1.url}")
    private String url1;
    @Value("${spring.shardingsphere.datasource.shard_order_1.password}")
    private String password1;
    @Value("${spring.shardingsphere.sharding.tables.orders.databaseStrategy.inline.shardingColumn}")
    private String databaseShardingColumn;
    @Value("${spring.shardingsphere.sharding.tables.orders.tableStrategy.inline.shardingColumn}")
    private String ordersShardingColumn;
    @Value("${spring.shardingsphere.sharding.tables.orders_detail.tableStrategy.inline.shardingColumn}")
    private String ordersDetailShardingColumn;

    /**
     * 设置数据源
     * @return
     * @throws SQLException
     */
    @Bean(name = "shardingDataSource")
    DataSource getShardingDataSource() throws SQLException {
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getBindingTableGroups().add(ordersLogicTable);
        shardingRuleConfig.getBindingTableGroups().add(ordersDetailLogicTable);
//       配置Orders表规则
        shardingRuleConfig.getTableRuleConfigs().add(getOrderTableRuleConfiguration());
        //配置ordersItem表规则
        shardingRuleConfig.getTableRuleConfigs().add(getOrderDetailTableRuleConfiguration());
        shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(new ComplexShardingStrategyConfiguration(databaseShardingColumn, new DatabaseShardingAlgorithm(username0,url0,password0,null)));
        shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(new ComplexShardingStrategyConfiguration(ordersShardingColumn,new CommonTableShardingAlgorithm(username0,url0,password0,ordersLogicTable)));

        //设置默认数据库
        shardingRuleConfig.setDefaultDataSourceName(defaultDataSource);

        return ShardingDataSourceFactory.createDataSource(createDataSourceMap(), shardingRuleConfig, new Properties());
    }


    @Bean
    @Primary
    public SqlSessionFactory sqlSessionFactory(DataSource shardingDataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(shardingDataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
        return bean.getObject();
    }

    @Bean
    @Primary
    public SqlSessionTemplate testSqlSessionTemplate(SqlSessionFactory sqlSessionFactory)  {
        return new SqlSessionTemplate(sqlSessionFactory);
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

    TableRuleConfiguration getOrderTableRuleConfiguration() {
        TableRuleConfiguration orderTableRuleConfig=new TableRuleConfiguration(ordersLogicTable, ordersActualDataNodes);
        orderTableRuleConfig.setDatabaseShardingStrategyConfig(new ComplexShardingStrategyConfiguration(databaseShardingColumn, new DatabaseShardingAlgorithm(username0,url0,password0,null)));
        orderTableRuleConfig.setTableShardingStrategyConfig(new ComplexShardingStrategyConfiguration(ordersShardingColumn,new CommonTableShardingAlgorithm(username0,url0,password0,ordersLogicTable)));
        return orderTableRuleConfig;
    }
    TableRuleConfiguration getOrderDetailTableRuleConfiguration() {
        TableRuleConfiguration orderDetailTableRuleConfig=new TableRuleConfiguration(ordersDetailLogicTable, ordersDetailActualDataNodes);
        orderDetailTableRuleConfig.setDatabaseShardingStrategyConfig(new ComplexShardingStrategyConfiguration(databaseShardingColumn, new DatabaseShardingAlgorithm(username0,url0,password0,null)));
        orderDetailTableRuleConfig.setTableShardingStrategyConfig(new ComplexShardingStrategyConfiguration(ordersDetailShardingColumn, new CommonTableShardingAlgorithm(username0,url0,password0,ordersLogicTable)));
        return orderDetailTableRuleConfig;
    }

    private DataSource createDataSource(String url,String username,String password) {
        BasicDataSource result = new BasicDataSource();
        result.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
        result.setUrl(url);
        result.setUsername(username);
        result.setPassword(password);
        return result;
    }

    private Map<String, DataSource> createDataSourceMap() {
        Map<String, DataSource> result = new HashMap<>();
        result.put(defaultDataSource, createDataSource(url0,username0,password0));
        result.put(shardOrder1DataSource, createDataSource(url1,username1,password1));
        return result;
    }
}
