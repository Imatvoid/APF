package com.arch.ability.plugin.impl;


import com.arch.ability.Plugin;
import com.arch.ability.annotation.RegisterPlugin;
import com.arch.ability.plugin.SkuMergeAbilityPoint;
import com.arch.ability.plugin.cmd.SkuMergeKeyCmd;
import com.arch.ability.plugin.validate.ValidateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@RegisterPlugin
@Slf4j
public class DefaultSkuMergePlugin extends Plugin implements SkuMergeAbilityPoint {

    // 特殊行号处理的事业部
    private String mergeWithOrderLineBUs = "BUSINESS_UNIT_123456789";

    /**
     * 接单阶段
     * @param cmd
     */
    @Override
    public void generateSkuMergeKeyReceiveSaleOrder(SkuMergeKeyCmd cmd) {

        if (!ValidateUtils.validateParam(cmd)) {
            return;
        }

        //拼接商品去重的唯一key
        StringBuilder builder = new StringBuilder();
        builder.append("_").append(cmd.getGoodsNo());
        builder.append("_").append(cmd.getGoodsLevel());

        //接单阶段时, 合并key需拼接上主赠标识和行号
        if (isMergeWithOrderLine(cmd)) {
            builder.append("_").append(cmd.getOrderLine());

        }

        cmd.getReply().setSkuMergeKey(builder.toString());
    }

    //明细合并区分维度是否需要加上行号
    private boolean isMergeWithOrderLine(SkuMergeKeyCmd cmd) {
        if (StringUtils.isBlank(mergeWithOrderLineBUs)) {
            return false;
        }
        return mergeWithOrderLineBUs.contains(cmd.getBusinessUnitNo());
    }

    /**
     * 下发仓库阶段如何合并明细
     * @param cmd
     */
    @Override
    public void generateSkuMergeKeyOutboundToWarehouse(SkuMergeKeyCmd cmd){
        StringBuilder builder = new StringBuilder();
        builder.append("_").append(cmd.getGoodsNo());
        cmd.getReply().setSkuMergeKey(builder.toString());
    }

    /**
     * 预展库存阶段 如何合并明细
     * @param cmd
     */
    @Override
    public void generateSkuMergeKeyOccupyUsableStock(SkuMergeKeyCmd cmd){
        StringBuilder builder = new StringBuilder();
        builder.append("_").append(cmd.getGoodsNo());
        cmd.getReply().setSkuMergeKey(builder.toString());
    }


}
