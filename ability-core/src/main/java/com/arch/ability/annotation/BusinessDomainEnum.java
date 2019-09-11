package com.arch.ability.annotation;

/**
 * 业务领域,业务能力的一级分类
 * <p>目前业务能力模型只分2层,叶子节点就是具体的业务能力扩展点</p>
 */
public enum BusinessDomainEnum {

    Finance("财务"),
    Order("订单"),
    Delivery("配送"),
    Stock("库存"),
    Warehouse("仓储"),
    PromiseTime("时效产品"),
    Print("打印"),
    AfterSale("售后"),
    BasicData("基础信息"),
    ;

    BusinessDomainEnum(String name) {

    }
}
