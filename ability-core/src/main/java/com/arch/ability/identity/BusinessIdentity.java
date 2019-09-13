package com.arch.ability.identity;

import com.arch.ability.PluginContext;

/**
 * 统一的业务身份标识，{@link PluginContext}通过它来加载对应的插件
 * <p>
 * <p>业务身份，就是业务标签 tag，不同的业务身份代表着不同的业务方</p>
 * <p>插件的方法内部，根据需要，可以自行实现再细分的路由策略</p>
 */
public final class BusinessIdentity implements Identifier {

    /**
     * 商家编码
     */
    private String sellerNo;

    /**
     * 事业部编码 一个商家一般有多个事业部
     */
    private String businessUnitNo;

    // 不允许实例化，只能通过build方法构造
    private BusinessIdentity() {
    }

    public static BusinessIdentity build() {
        return new BusinessIdentity();
    }

    public String getSellerNo() {
        return sellerNo;
    }

    public BusinessIdentity withSellerNo(String sellerNo) {
        this.sellerNo = sellerNo;
        return this;
    }

    public String getBusinessUnitNo() {
        return businessUnitNo;
    }

    public BusinessIdentity withBusinessUnitNo(String businessUnitNo) {
        this.businessUnitNo = businessUnitNo;
        return this;
    }

    public BusinessIdentity withSellerAndBusinessUnitNo(String sellerNo, String businessUnitNo) {
        this.sellerNo = sellerNo;
        this.businessUnitNo = businessUnitNo;
        return this;
    }

    @Override
    public String tenantCode() {
        return sellerNo;
    }

    @Override
    public String bizCode() {
        return businessUnitNo;
    }


    @Override
    public String toString() {
        return "BusinessIdentity {sellerNo='" + sellerNo + "', businessUnitNo='" + businessUnitNo + "'}";
    }

}
