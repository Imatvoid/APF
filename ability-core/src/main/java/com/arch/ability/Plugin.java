package com.arch.ability;

import com.arch.ability.exception.PluginException;
import com.arch.ability.identity.Identifier;
import com.arch.ability.utils.AopTargetUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 插件抽象类,所有插件实现继承
 */
@Slf4j
public abstract class Plugin {

    /**
     * 上下文的包装类
     */
    private PluginContextWrapper wrapper;

    /**
     * 本次请求的业务身份标识
     */
    protected Identifier identifier;


    void setWrapper(final PluginContextWrapper wrapper) {
        if (wrapper == null) {
            throw new IllegalArgumentException("Wrapper cannot be null");
        }

        this.wrapper = wrapper;
    }

    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }

    /**
     * 插件的真实类名
     * <p>
     * <p>插件无论是否通过AOP拦截，返回的都是相同的插件类名</p>
     * @return e, g. com.arch.ability.plugin.sign_bill_return.impl.DefaultSkuMergeExt
     */
    public String canonicalName() {

        Plugin plugin = AopTargetUtils.getTargetPlugin(this);
        return plugin.getClass().getCanonicalName();
    }


    protected final PluginContextWrapper getWrapper() {
        return wrapper;
    }

    /**
     * 从插件的properties文件里获取某个key对应的值
     *
     * @param propertyKey
     * @return null if propertyKey not found
     */
    protected final String getProperty(String propertyKey) {
        return wrapper.getProperty(this, propertyKey);
    }

    /**
     * 从插件的properties文件里根据指定数据类型获取某个key对应的值
     * @param propertyKey
     * @param type        只支持 String, Integer, Float, Boolean
     * @param <T>
     * @return null if propertyKey not found or type not supported
     */
    protected final <T> T getProperty(String propertyKey, Class<T> type) {
        return wrapper.getProperty(this, propertyKey, type);
    }

    /**
     * 插件停止方法
     */
    public void start() throws PluginException {
    }

    /**
     * 插件启动方法
     */
    public void stop() throws PluginException {
    }

}
