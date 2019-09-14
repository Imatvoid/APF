package com.arch.ability.plugin.validate;


import com.arch.ability.plugin.cmd.SkuMergeKeyCmd;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class ValidateUtils {

    public static Boolean validateParam(SkuMergeKeyCmd cmd) {
        if (StringUtils.isBlank(cmd.getBusinessUnitNo())) {
            log.error("[订单明细合并] 事业部编码不能为空!");
            cmd.getReply().fail("xxxKey", "事业部编码不能为空");
            return Boolean.FALSE;
        }

        if (StringUtils.isBlank(cmd.getGoodsNo())) {
            log.error("[订单明细合并] 商品编码不能为空!");
            cmd.getReply().fail("xxxKey", "商品编码不能为空");
            return Boolean.FALSE;
        }

        if (StringUtils.isBlank(cmd.getGoodsLevel())) {
            log.error("{}[订单明细合并] 商品等级不能为空!", cmd.getGoodsNo());
            cmd.getReply().fail("xxxKey", "商品等级不能为空");
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }
}
