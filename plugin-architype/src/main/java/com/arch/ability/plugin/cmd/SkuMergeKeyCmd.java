package com.arch.ability.plugin.cmd;


import com.arch.ability.cmd.BaseCmd;
import com.arch.ability.cmd.BaseReply;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SkuMergeKeyCmd extends BaseCmd {

    /**
     * 事业部编码
     */
    private String BusinessUnitNo;

    /**
     * 商品编码
     */
    private String goodsNo;

    /**
     * 商品等级
     */
    private String goodsLevel;

    /**
     * 行号
     */
    private String orderLine;

    /**
     * 批次属性,比如生产日期
     */
    private String batchAttribute;


    private Reply reply = new Reply();

    @Data
    public static class Reply extends BaseReply {

        /**
         * 明细合并key
         */
        private String skuMergeKey;

    }
}
