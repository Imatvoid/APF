package com.arch.ability.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 插件由于外部依赖而向宿主系统提出的要求
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Require {

    /**
     * 表示该外部依赖只在这些phases才依赖，其他的phase不依赖
     * @return
     */
    PhaseEnum[] phases();

    /**
     * Spring bean id.
     * <p>该bean已经由业务能力插件在注册中心进行了注册，表示它对宿主系统的需求</p>
     *
     * @return
     */
    String beanId();
}
