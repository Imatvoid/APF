package com.arch.ability.annotation;

/**
 * 执行阶段类型
 */
public enum PhaseEnum {
    Financial("财务"),
    ReceiveSaleOrder("接单校验"),
    OccupyUsableStock("预占可用库存"),

    Promise("时效"),

    OutboundToCarrier("下发配送承运商"),
    ReturnFromCarrier("承运商回传"),

    OutboundToWarehouse("下发仓库"),
    ReturnFromWarehouse("仓库回传"),
    ReduceRealStock("扣减实际库存"),

    ReturnToSeller("回传商家"),


    All("所有执行阶段"),
    ;

    PhaseEnum(String name) {

    }

}
