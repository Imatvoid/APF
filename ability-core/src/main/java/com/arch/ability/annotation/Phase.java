package com.arch.ability.annotation;

import com.arch.ability.Plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 能力点中具体功能点(能力扩展点的一个方法)的执行阶段，即它体现在链路上的哪几个环节.
 * <p>Notes: 把该注解应用到扩展点的接口方法，而不是{@link Plugin}的实现类方法上</p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface Phase {

    /**
     * 该功能点执行的阶段.
     *
     * @return
     */
    PhaseEnum[] value();

}
