package com.arch.ability.router;

import com.arch.ability.identity.Identifier;
import com.arch.ability.Registry;

/**
 * 插件的路由策略
 * <ul>
 * <li>{@code Router}根据业务身份标识对能力点的实现进行一级路由(定位到具体哪个插件实现)</li>
 * </ul>
 * <p>为了支持更细粒度的路由策略，插件内部可以继续使用二级路由</p>
 * <p>二级路由的实现，也不意味着if/else，可以通过策略模式、工厂方法等模式及组合，保持局部clean and elegant code</p>
 */
public interface Router {

    /**
     * 根据业务身份标识和能力点类型定位具体的插件
     *
     * @param extPt      能力点
     * @param identifier 业务身份标识
     * @param <T>
     * @return the Plugin,一个能力点的具体实现
     */
    <T> T route(Class<T> extPt, Identifier identifier);

    /**
     * 设置(传递)插件注册表，由插件微内核提供已经解析好的扩展点注册表
     *
     * @param registry
     */
    void setRegistry(Registry registry);
}
