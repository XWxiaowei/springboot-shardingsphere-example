## 前言
这几天在研究分表分库的方案。综合了几种数据库方案。最后选型Sharding-jdbc。它主要有如下几个优点。

1. 支持分布式事务
2. 适用于任何基于Java的ORM框架，

## springboot-shardingsphere-example
shardingsphere 与springboot的整合
## 整合策略
### 引入依赖
```xml
    <properties>
     <sharding-sphere.version>4.0.0-RC1</sharding-sphere.version>
    </properties>
     <!--shardingsphere -->
        <dependency>
            <groupId>org.apache.shardingsphere</groupId>
            <artifactId>sharding-core-api</artifactId>
            <version>${sharding-sphere.version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.shardingsphere</groupId>
            <artifactId>sharding-jdbc-core</artifactId>
            <version>${sharding-sphere.version}</version>
        </dependency>
```
配置好依赖之后，接着就是配置数据源，以及数据的分片键，分片策略。在application.yml中配置如下
### 配置

```yml
mybatis:
  mapper-locations: classpath:mapper/sharding/*.xml
#端口号
server:
  port: 8283
#数据库连接
spring:
  shardingsphere:
    datasource:
      names: shard_order_0,shard_order_1
      shard_order_0:
          driver-class-name: com.mysql.jdbc.Driver
          url: jdbc:mysql://localhost:3306/shard_order_0
          username: root
          password: admin
      shard_order_1:
          driver-class-name: com.mysql.jdbc.Driver
          url: jdbc:mysql://localhost:3306/shard_order_1
          username: root
          password: admin
    props:
      sql:
         show: true  #打印sql
# 如下对orders表和orders_detail都做了分片配置，分片键分别是id，orders_id，
    sharding:
      tables:
        orders:
          actualDataNodes: shard_order_$->{0..1}.orders_$->{0..1}
          databaseStrategy:
            inline:
              shardingColumn:  adddate
          tableStrategy:
              inline:
                shardingColumn: id
        orders_detail:
          actualDataNodes: shard_order_$->{0..1}.orders_detail_$->{0..1}
          tableStrategy:
            inline:
              shardingColumn: orders_id     
```
完成配置之后接着就是要定义数据库的分片策略和分片的策略以及初始化DataSource。因为本次在主库中加了一个路由表，在路由时动态查取该分片值多需要查找的分库分表，所以，需要再多配置一个数据源。**用于执行分库的算法时可以查询路由表**，项目结构如图所示：
![enter description here](./images/TIM图片20190509111326.jpg)
### 分库的数据源配置

```java
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
        shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(new StandardShardingStrategyConfiguration(databaseShardingColumn, preciseModuloDatabaseShardingAlgorithm));
        shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(new StandardShardingStrategyConfiguration(ordersShardingColumn,preciseModuloTableShardingAlgorithm));

        //设置默认数据库
        shardingRuleConfig.setDefaultDataSourceName(defaultDataSource);

        return ShardingDataSourceFactory.createDataSource(createDataSourceMap(), shardingRuleConfig, new Properties());
    }


    /**
     * 获取sqlSessionFactory实例
     * @param shardingDataSource
     * @return
     * @throws Exception
     */
    @Bean
    @Primary
    public SqlSessionFactory sqlSessionFactory(DataSource shardingDataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(shardingDataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
        return bean.getObject();
    }
	
	    TableRuleConfiguration getOrderTableRuleConfiguration() {
        TableRuleConfiguration orderTableRuleConfig=new TableRuleConfiguration(ordersLogicTable, ordersActualDataNodes);
        orderTableRuleConfig.setDatabaseShardingStrategyConfig(new StandardShardingStrategyConfiguration(databaseShardingColumn, preciseModuloDatabaseShardingAlgorithm));
        orderTableRuleConfig.setTableShardingStrategyConfig(new StandardShardingStrategyConfiguration(ordersShardingColumn,preciseModuloTableShardingAlgorithm));
        return orderTableRuleConfig;
    }
    TableRuleConfiguration getOrderDetailTableRuleConfiguration() {
        TableRuleConfiguration orderDetailTableRuleConfig=new TableRuleConfiguration(ordersDetailLogicTable, ordersDetailActualDataNodes);
        orderDetailTableRuleConfig.setDatabaseShardingStrategyConfig(new StandardShardingStrategyConfiguration(databaseShardingColumn, preciseModuloDatabaseShardingAlgorithm));
        orderDetailTableRuleConfig.setTableShardingStrategyConfig(new StandardShardingStrategyConfiguration(ordersDetailShardingColumn, orderDetailTableShardingAlgorithm));
        return orderDetailTableRuleConfig;
    }
	
```

### 数据库的分库策略

```java
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

```
如上分片键是adddate，当SQL中含有adddate字段时会执行分片策略。如果SQL中adddate 字段是BETWEEN  AND 需要执行复合分片算法。否则会全库查询。因为是按照年份来分库的，所以先截取当前的年份，然后去路由表中查。对应的分库id。分表策略也是类似的。