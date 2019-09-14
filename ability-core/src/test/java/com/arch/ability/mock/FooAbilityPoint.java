package com.arch.ability.mock;


import com.arch.ability.annotation.BusinessDomain;
import com.arch.ability.annotation.BusinessDomainEnum;
import com.arch.ability.annotation.Phase;
import com.arch.ability.annotation.PhaseEnum;
import com.arch.ability.cmd.FooCmd;

@BusinessDomain(catalog = BusinessDomainEnum.Delivery)
public interface FooAbilityPoint {

    @Phase(PhaseEnum.OutboundToCarrier)
    String hello();

    @Phase(PhaseEnum.OutboundToCarrier)
    void doFoo(FooCmd cmd);

}
