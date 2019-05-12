# 博客地址：
https://mp.weixin.qq.com/s/XW1vfGJEK0mRKOxyWM3PHQ
## 基本概念
这几天在研究分表分库的方案。综合了几种数据库方案。
![enter description here](./images/1557631154854.png)
最后选型Sharding-jdbc。它主要有如下几个优点。
1. 支持分布式事务
2. 适用于任何基于Java的ORM框架。
3. 对业务零侵入。
## 数据分片
数据分片是指按照某个维度将存放在单一数据库中的数据分散地存放至多个数据库或者表中以达到提升性能瓶颈以及可用性的效果。数据分片有效手段是对关系型数据库进行分库和分表。分表可以降低每个单表的数据阈值，同时还能够将分布式事务转化为本地事务的。分库可以有效的分散数据库单点的访问量。
### 分片键
用于分片的数据库字段，是将数据库（表）进行水平拆分的关键字段。例如：将订单表中的订单主键的尾数取模分批拿，则订单主键就是分片字段，SQL中如果没有分片字段，则执行全库路由，性能较差。Sharding-JDBC也支持多个字段进行分片。
### 分片策略和分片算法
Sharding-JDBC 中共有五种分片策略。1、标准分片策略；2、复合分片策略；3、行表达式分片策略；4、Hint分片策略；5、不分片策略；对应的有4种分片算法，1、精确分片算法；2、范围分片算法；3、复合分片算法 ；4、Hint分片算法；
分片算法：
Sharding-JDBC并没有提供内置分片算法，而是通过分片策略将各种场景提炼出来，提供更高层次的抽象，并提供接口让应用开发者自行实现分片算法。
分片策略：
包含分片键和分片算法，由于分片算法的独立性，将其独立抽离。真正可用于分片操作的是分片键+分片算法，也就是分片策略。
|    分片策略 | 概念    |  分片算法   | 概念    | 适用场景|
| --- | --- | --- | --- | --- |
|  标准分片策略  | 对应StandardShardingStrategy,只支持单分片键,提供对SQL语句中的=，IN和BETWEEN AND的分片操作支持,提供PreciseShardingAlgorithm和RangeShardingAlgorithm两个分片算法。    |  精确分片算法    |   对应PreciseShardingAlgorithm  | 适用于单分片键的= 和IN进行分片的场景。需要配合StandardShardingStrategy使用 |
|  标准分片策略  |     |  范围分片算法    | 对应RangeShardingAlgorithm    | 适用于单分片键的BETWEEN AND进行分片的场景，需要配合StandardShardingStrategy使用|
|  复合分片策略  | 对应ComplexShardingStrategy。听过对SQL语句中的=，IN的分片操作支持。由于多分片键之间的关系复杂，因此并未进行过多的封装，而是直接将分片键组合以及分片操作符透传至分片算法   |  复合分片算法    |   对应ComplexKeysShardingAlgorithm  | 适用于多分片键的情况，需要配合ComplexShardingStrategy使用 |
|  Hint分片策略  |   对应HintShardingStrategy。通过Hint而非SQL解析的方式分片的策略。  |  Hint分片算法    |  对应HintShardingAlgorithm   |适用于使用Hint分片的场景，需要配合HintShardingStrategy使用  |
|  Hint分片策略  |   对应InlineShardingStrategy。使用Groovy的表达式，提供对SQL语句中的=和IN的分片操作支持，只支持单分片键。对于简单的分片算法，可以通过简单的配置使用，从而避免繁琐的Java代码开发，如: t_user_$->{u_id % 8} 表示t_user表根据u_id模8，而分成8张表，表名称为t_user_0到t_user_7。  |      |     |  |

## Sharding-JDBC与SpringBoot整合策略

### 总体说明
本实例是结合相关项目来的，在该项目中订单id（orders_id）是一个核心的热点字段。然后，订单号是带有日期的，所以，本次分片的方案是安装时间分库分表，时间粒度可以年，月，季度。本demo中的时间粒度是年。
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
如上可以看出，我这里配置了两个分库shard_order_0和shard_order_1；然后，每个分库下面又配置了两个逻辑表orders和orders_detail，每个逻辑表下有两个物理表。数据库的分片键是adddate，逻辑表orders的分片键是id，逻辑表orders_detail的分片键是orders_id。
完成配置之后接着就是要定义数据库的分片策略和分片的策略以及初始化DataSource。因为本次在主库中加了一个路由表，在路由时动态查取该分片值所需要查找的分库分表，所以，需要再多配置一个数据源。**用于执行分库的算法时可以查询路由表**，项目结构如图所示：
![enter description here](./images/TIM图片20190509111326.jpg)
### 分库的数据源配置

```java
//*** DataSourceConfig
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
如上，可以看出orders和orders_detail 两个逻辑表都采用的标准分片策略，使用的是精确分片算法。分库的策略也是标准分片策略，使用的是精确分片算法。

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
### 路由表
接着我们来看看路由表，路由表是该分库分表方案中的核心表。表结构如下：
![enter description here](./images/1557632328955.png)
表的数据存储如下：
![enter description here](./images/1557632359198.png)
如上图所示，路由表中按照年份存放了，每个年份所对应的分库id，和分表id。所以当有分片键，进入分表策略时就可以根据年份找到对应的分库，分表。

### 多数据源配置
由于路由表是公共表，不参与分片，所以只在主库中存储了一份。当进入数据库的分库策略时就要查询路由表。如果不配置多数据源的话，此时路由表对应的shardConfigMapper为null。不能进行查询。过需要配置多数据源。

```java
@MapperScan(basePackages = "com.jay.mapper.nosharding", sqlSessionTemplateRef = "noshardingSqlSessionTemplate")
public class NoShardingDataSourceConfig {

    @Value("${spring.shardingsphere.datasource.shard_order_0.username}")
    private String username0;
    @Value("${spring.shardingsphere.datasource.shard_order_0.url}")
    private String url0;
    @Value("${spring.shardingsphere.datasource.shard_order_0.password}")
    private String password0;

    @Bean(name = "noshardingDataSource")
    public DataSource testDataSource() {
        BasicDataSource result = new BasicDataSource();
        result.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
        result.setUrl(url0);
        result.setUsername(username0);
        result.setPassword(password0);
        return result;
    }

    /**
     * 获取sqlSessionFactory实例
     *
     * @param shardingDataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "noshardingSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("noshardingDataSource") DataSource shardingDataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(shardingDataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/nosharding/*.xml"));
        return bean.getObject();
    }

```
## 总结
本文首先介绍了分库分表的相关概念，然后，对比了几种主流的分库分表中间件。接着重点介绍了分片策略和相关的算法。最后通过一个demo，实现了对Sharding-JDBC 数据分片的落地。
## 参考资料
[Sharding-JDBC官方文档](https://shardingsphere.apache.org/document/current/cn/features/sharding/)