package com.arch.ability;

import com.arch.ability.annotation.PhaseEnum;
import com.arch.ability.exception.PluginException;
import com.arch.ability.annotation.Require;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 为能力点注入其依赖的能力.
 */
class RequirementManager {

    private final ApplicationContext applicationContext;

    RequirementManager(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * @param phases 宿主系统可以处理的所有阶段
     * @param plugin 插件实现
     */
    void handleRequirement(Set<PhaseEnum> phases, final Plugin plugin) {
        Class<? extends Plugin> extClazz = plugin.getClass();
        // 获取当前类及所有父类声明的public、private和protected字段
        List<Field> fieldList = getAllFieldsList(extClazz);
        for (final Field field : fieldList) {
            if (Modifier.isStatic(field.getModifiers())) {
                // static属性忽略
                continue;
            }
            // 合法性检查：插件里的外部依赖不能使用@Resource/@Autowired注入，必须使用@Require
            if (field.isAnnotationPresent(Resource.class) || field.isAnnotationPresent(Autowired.class)) {
                throw new PluginException(plugin.getClass().getCanonicalName() + " field " + field.getName() + " cannot use @Resource or @Autowired, use @Require");
            }

            // Require与DccValue只能处理一个，他们是互斥的
            Require require = field.getAnnotation(Require.class);
            if (require == null || !intersect(phases, require.phases())) {
                // 该属性没有依赖需求，或者宿主系统的phases与该依赖的phases无重叠，忽略
                continue;
            }
            if (require.beanId().isEmpty()) {
                throw new PluginException(plugin.getClass().getCanonicalName() + " has empty beanId in @Require");
            }
            // 该bean已经被能力点注册了，宿主系统就应该配置到spring里
            // 如果宿主系统没有定义该bean，会抛出 NoSuchBeanDefinitionException
            try {
                Object springBean = applicationContext.getBean(require.beanId(), field.getType());
                field.setAccessible(true);
                // 把依赖的bean注入到插件
                field.set(plugin, springBean);
            } catch (NoSuchBeanDefinitionException e) {
                throw new PluginException(plugin.getClass().getCanonicalName() + " requires " + require.beanId() + ", but not found in spring");
            } catch (IllegalAccessException e) {
                throw new PluginException(e);
            }
        }
    }

    boolean intersect(Set<PhaseEnum> set, PhaseEnum[] arr) {
        for (PhaseEnum phaseEnum : arr) {
            if (PhaseEnum.All.equals(phaseEnum) || set.contains(phaseEnum)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 拷贝apache commons的实现：commons-lang3-3.8.1 FieldUtils#getAllFieldsList(java.lang.Class)
     * @param cls
     * @return
     */
    private List<Field> getAllFieldsList(Class<?> cls) {
        List<Field> allFields = new ArrayList<Field>();
        for (Class currentClass = cls; currentClass != null; currentClass = currentClass.getSuperclass()) {
            Field[] declaredFields = currentClass.getDeclaredFields();
            Collections.addAll(allFields, declaredFields);
        }
        return allFields;
    }


}
