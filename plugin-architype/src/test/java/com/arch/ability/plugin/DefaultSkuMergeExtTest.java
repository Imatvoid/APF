package com.arch.ability.plugin;



import com.arch.ability.identity.BusinessIdentity;
import com.arch.ability.PluginContext;
import com.arch.ability.plugin.cmd.SkuMergeKeyCmd;
import com.arch.ability.plugin.impl.DefaultSkuMergePlugin;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring-main.xml"})
public class DefaultSkuMergeExtTest {


    @Resource
    private PluginContext pluginContext;

    @Test
    public void validate1() {

        SkuMergeAbilityPoint ext = pluginContext.load(SkuMergeAbilityPoint.class, BusinessIdentity.build().withBusinessUnitNo("BUSINESS_UNIT_123456789"));
        assertEquals(DefaultSkuMergePlugin.class, ext.getClass());

        SkuMergeKeyCmd cmd = new SkuMergeKeyCmd();
        cmd.setBusinessUnitNo("BUSINESS_UNIT_123456789");
        cmd.setGoodsNo("goodsNo");
        cmd.setGoodsLevel("goodsLevel");
        cmd.setOrderLine("orderLine");


        ext.generateSkuMergeKeyReceiveSaleOrder(cmd);

        assertTrue(cmd.getReply().isOk());
        assertTrue(cmd.getReply().getSkuMergeKey().endsWith("_orderLine"));
    }

    @Test
    public void validate2() {

        SkuMergeAbilityPoint ext = pluginContext.load(SkuMergeAbilityPoint.class, BusinessIdentity.build().withBusinessUnitNo("BUSINESS_UNIT_4398046515771"));
        assertEquals(DefaultSkuMergePlugin.class, ext.getClass());

        SkuMergeKeyCmd cmd = new SkuMergeKeyCmd();
        cmd.setBusinessUnitNo("BUSINESS_UNIT_4398046515771");
        cmd.setGoodsNo("goodsNo");
        cmd.setGoodsLevel("goodsLevel");
        cmd.setOrderLine("orderLine");


        ext.generateSkuMergeKeyReceiveSaleOrder(cmd);

        assertTrue(cmd.getReply().isOk());
        assertFalse(cmd.getReply().getSkuMergeKey().endsWith("_orderLine"));
    }

}
