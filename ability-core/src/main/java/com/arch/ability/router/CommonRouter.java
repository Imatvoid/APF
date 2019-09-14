package com.arch.ability.router;


import com.arch.ability.exception.PluginException;
import com.arch.ability.identity.Identifier;
import com.arch.ability.Plugin;
import com.arch.ability.Registry;
import com.arch.ability.annotation.RegisterPlugin;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 插件路由实现
 */
@Slf4j
@Data
public class CommonRouter implements Router {

    private Matcher matcher = new Matcher();

    private Registry registry;

    @Override
    public <T> T route(Class<T> extPt, Identifier identifier) {
        List<Plugin> pluginList = registry.getPluginTable().get(extPt);
        if (pluginList == null || pluginList.size() == 0) {
            log.warn("{} failed to locate the plugin: never registered", extPt.getCanonicalName());
            return null;
        }

        if (identifier == null) {
            // should never happen, but for defensive programming, use default ext
            log.error("{} encounters null Identifier, will use default ext", extPt.getCanonicalName());
            return (T) registry.getDefaultPluginTable().get(extPt);
        }

        if (pluginList.size() == 1) {
            // 这个扩展点只有一个插件：默认插件
            Plugin plugin = pluginList.get(0);
            plugin.setIdentifier(identifier);
            return (T) plugin;
        }

        // 优先匹配 businessUnitNoKeys
        for (Plugin plugin : pluginList) {
            RegisterPlugin registerPlugin = registry.getPluginRegisterPluginTable().get(plugin.getClass());
            if (registerPlugin.businessUnitNoKeys().length > 0 && identifier.bizCode() != null) {
                for (String businessUnitNoKey : registerPlugin.businessUnitNoKeys()) {
                    // 如果注册插件时指定了事业部，那么必须配置properties
                    if (!registry.getPluginPropertiesTable().containsKey(extPt) || registry.getPluginPropertiesTable().get(extPt).getProperty(businessUnitNoKey) == null) {
                        throw new PluginException("can not found properties " + businessUnitNoKey + " of " + extPt.getName());
                    }

                    String businessUnitNoPropValue = registry.getPluginPropertiesTable().get(extPt).getProperty(businessUnitNoKey);
                    if (matcher.match(identifier.bizCode(), businessUnitNoPropValue)) {
                        plugin.setIdentifier(identifier);
                        return (T) plugin;
                    }
                }
            }
        }

        // 通过 businessUnitNoKeys 没有匹配成功，resort to sellerNoKeys
        for (Plugin plugin : pluginList) {
            RegisterPlugin registerPlugin = registry.getPluginRegisterPluginTable().get(plugin.getClass());
            if (registerPlugin.sellerNoKeys().length > 0 && identifier.tenantCode() != null) {
                for (String sellerNoKey : registerPlugin.sellerNoKeys()) {
                    if (!registry.getPluginPropertiesTable().containsKey(extPt) || registry.getPluginPropertiesTable().get(extPt).getProperty(sellerNoKey) == null) {
                        throw new PluginException("can not find properties " + sellerNoKey + " of " + extPt.getName());
                    }

                    String sellerNoPropValue = registry.getPluginPropertiesTable().get(extPt).getProperty(sellerNoKey);
                    if (registerPlugin.businessUnitNoKeys().length == 0 && matcher.match(identifier.tenantCode(), sellerNoPropValue)) {
                        plugin.setIdentifier(identifier);
                        return (T) plugin;
                    }
                }
            }
        }

        Plugin defaultExt = registry.getDefaultPluginTable().get(extPt);
        if (defaultExt == null) {
            return null;
        }

        defaultExt.setIdentifier(identifier);
        return (T) defaultExt;
    }

}
