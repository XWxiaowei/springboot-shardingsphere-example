CREATE DATABASE shard_order_0;
USE shard_order_0;

CREATE TABLE `orders_0` (
  `id` varchar(36) NOT NULL COMMENT '订单id',
  `parent_orders_uuid` varchar(36) DEFAULT NULL COMMENT '业务平台的订单id',
  `parent_orders_id` varchar(36) NOT NULL COMMENT '业务平台的订单编号',
  `order_origin` char(2) DEFAULT NULL COMMENT '订单来源1=PC',
  `order_type` char(2) DEFAULT NULL COMMENT '业务来源 1:A业务;2:B业务',
  `adddate` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单表_0';

CREATE TABLE `orders_1` (
  `id` varchar(36) NOT NULL COMMENT '订单id',
  `parent_orders_uuid` varchar(36) DEFAULT NULL COMMENT '业务平台的订单id',
  `parent_orders_id` varchar(36) NOT NULL COMMENT '业务平台的订单编号',
  `order_origin` char(2) DEFAULT NULL COMMENT '订单来源1=PC',
  `order_type` char(2) DEFAULT NULL COMMENT '业务来源 1:A业务;2:B业务',
  `adddate` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单表_1';

CREATE TABLE `orders_detail_0` (
  `id` varchar(36) NOT NULL COMMENT '主键',
  `orders_id` varchar(36) NOT NULL COMMENT '订单id',
  `goods_id` varchar(36) DEFAULT NULL COMMENT '商品id',
  `goods_name` varchar(100) DEFAULT NULL COMMENT '商品名称',
  `goods_kindname` varchar(300) DEFAULT NULL COMMENT '商品属性',
  PRIMARY KEY (`id`),
  KEY `index_orders_id` (`orders_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='子订单明细表_0';
CREATE TABLE `orders_detail_1` (
  `id` varchar(36) NOT NULL COMMENT '主键',
  `orders_id` varchar(36) NOT NULL COMMENT '订单id',
  `goods_id` varchar(36) DEFAULT NULL COMMENT '商品id',
  `goods_name` varchar(100) DEFAULT NULL COMMENT '商品名称',
  `goods_kindname` varchar(300) DEFAULT NULL COMMENT '商品属性',
  PRIMARY KEY (`id`),
  KEY `index_orders_id` (`orders_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='子订单明细表_1';

