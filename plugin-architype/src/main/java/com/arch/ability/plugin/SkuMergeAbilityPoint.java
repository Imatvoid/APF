package com.arch.ability.plugin;

import com.arch.ability.plugin.cmd.SkuMergeKeyCmd;
import com.arch.ability.annotation.BusinessDomain;
import com.arch.ability.annotation.BusinessDomainEnum;
import com.arch.ability.annotation.Phase;

import static com.arch.ability.annotation.PhaseEnum.*;

/**
 * 订单明细合并的业务能力扩展点
 */
@BusinessDomain(catalog = BusinessDomainEnum.Order)
public interface SkuMergeAbilityPoint {

    @Phase({ReceiveSaleOrder})
    void generateSkuMergeKeyReceiveSaleOrder(SkuMergeKeyCmd cmd);

    @Phase({OccupyUsableStock})
    default  void generateSkuMergeKeyOccupyUsableStock(SkuMergeKeyCmd cmd) {};


    @Phase({OutboundToWarehouse})
    default  void generateSkuMergeKeyOutboundToWarehouse(SkuMergeKeyCmd cmd) {};




}
