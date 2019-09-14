package com.arch.ability.mock;


import com.arch.ability.Plugin;
import com.arch.ability.annotation.RegisterPlugin;
import com.arch.ability.cmd.FooCmd;
import com.arch.ability.exception.PluginException;
import lombok.extern.slf4j.Slf4j;

@RegisterPlugin
@Slf4j
public class DefaultFooPlugin extends Plugin implements FooAbilityPoint {

//    @Require(beanId = "goodsService", phases = {PhaseEnum.InboundSaleOrder})
//    protected GoodsService goodsService;




    private String fooName = "defaultFooName";


    private String fooGender ="defaultFooGender";


    private String fooNotConfig = "defaultFooNotConfig";

    @Override
    public String hello() {
        log.info("Identifier {} {}", identifier.tenantCode(), identifier.bizCode());

        log.info("hello, my name is : {}", fooName);
        log.info("fooNotConfig : {}", fooNotConfig);

        if (getWrapper() == null) {
            throw new RuntimeException("wrapper not passed in");
        }

        if (!fooNotConfig.equals("defaultFooNotConfig")) {
            throw new RuntimeException("not as expected");
        }
        log.info("wrapper: {}", getWrapper());
        return "default";
    }

    @Override
    public void doFoo(FooCmd cmd) {
        log.info("执行 doFoo");
        log.info("i'm {}", fooGender);
        cmd.reply.orderMark1 = 1;
        cmd.reply.orderMark3 = 3;
    }

    @Override
    public void start() throws PluginException {
        this.log.info("started");
    }

    @Override
    public void stop() throws PluginException {
        this.log.info("stopped");
    }

}
