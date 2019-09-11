package com.arch.ability.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 业务领域,注解在业务扩展点上
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface BusinessDomain {

    /**
     * 该业务能力属于哪些业务域
     * @return
     */
    BusinessDomainEnum[] catalog();

}
