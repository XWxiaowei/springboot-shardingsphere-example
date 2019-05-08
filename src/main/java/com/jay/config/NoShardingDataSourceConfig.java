package com.jay.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * Created by xiang.wei on 2019/5/8
 *
 * @author xiang.wei
 */
@Configuration
@MapperScan(basePackages = "com.jay.mapper.nosharding", sqlSessionTemplateRef = "noshardingSqlSessionTemplate")
public class NoShardingDataSourceConfig {


    @Bean(name = "noshardingDataSource")
    @ConfigurationProperties(prefix = "spring.shardingsphere.datasource.shard_order_0")
    public DataSource testDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 获取sqlSessionFactory实例
     * @param shardingDataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "noshardingSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(DataSource shardingDataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(shardingDataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/nosharding/*.xml"));
        return bean.getObject();
    }

    /**
     * 配置事务
     * @param dataSource
     * @return
     */
    @Bean(name = "noshardingTransactionManager")
    public DataSourceTransactionManager testTransactionManager(@Qualifier("noshardingDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "noshardingSqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("noshardingSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
