package com.arch.ability.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 在扩展点的实现类(插件)上标注，表示注册一个全链路唯一的扩展点的实现
 * <ul>
 * <li>必须要有一个默认实现({@code @RegisterExt} 不包含任何businessUnitKeys和sellerNoKeys配置)</li>
 * <li>@RegisterExt注解支持businessUnitKeys和sellerNoKeys两个条件进行一级路由(类级别)，如果能力点中有需要根据其他条件有不同实现的情况，能力点(方法级别)自行实现二级路由</li>
 * <li>通过业务身份{@code BusinessIdentity}进行路由匹配时优先匹配businessUnitKeys，如果匹配不上则使用sellerNoKeys，如果还没匹配到则使用默认实现</li>
 * <li>businessUnitKeys和sellerNoKey的值需要在配置文件properties中定义，解决不同环境下值不同的问题</li>
 * <li>配置文件的命名规则为：properties/ext/ + 能力点接口类名 + .properties，多环境采用多配置</li>
 * </ul>
 * <p>
 * <p>Notes: 在扩展点声明(接口)上不需要标注{@code RegisterExt}</p>
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RegisterPlugin {

    /**
     * 扩展点匹配的事业部编号，多个编号使用{@code 或} 运算
     * <p>
     * <p>值需要在配置文件properties中定义，解决不同环境下值不同的问题</p>
     */
    String[] businessUnitNoKeys() default {};

    /**
     * 扩展点匹配的商家编号，多个编号使用{@code 或} 运算
     * <p>
     * <p>值需要在配置文件properties中定义，解决不同环境下值不同的问题</p>
     */
    String[] sellerNoKeys() default {};

}
