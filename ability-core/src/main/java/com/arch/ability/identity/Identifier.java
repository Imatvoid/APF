package com.arch.ability.identity;


/**
 * 业务身份标识.
 */
public interface Identifier {


    /**
     * 抽象的租户码，you name it
     *
     * @return
     */
    String tenantCode();

    /**
     * 抽象的业务标识码，you name it
     *
     * @return
     */
    String bizCode();


}
