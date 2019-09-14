package com.arch.ability;


import com.arch.ability.annotation.RegisterPlugin;
import com.arch.ability.exception.PluginException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import static com.arch.ability.utils.AopTargetUtils.getTargetPlugin;

/**
 * 插件相关的注册中心
 */
@Slf4j
@Getter
@Setter
public class Registry {

    private static final String EnhancerByCglibMethodGetTargetClass = "getTargetClass";
    private static final String EnhancerByCglibMethodGetTargetSource = "getTargetSource";

    private static final String PropertiesRoot = "/properties/ability-point/";
    private static final String PropertiesSuffix = ".properties";
    //能力点接口后缀
    private static final String ExtPtSuffix = "AbilityPoint";
    //插件后缀
    private static final String ExtSuffix = "Plugin";

    // 能力扩展点登记表，一个能力点AbilityPoint对应List of Plugin实现
    private Map<Class, List<Plugin>> pluginTable = new HashMap<Class, List<Plugin>>();

    // 默认扩展点登记表，一个能力点有且只有一个默认实现
    private Map<Class, Plugin> defaultPluginTable = new HashMap<Class, Plugin>();

    // 插件的properties属性表，一个能力点AbilityPoint有一个属性表
    private Map<Class, Properties> pluginPropertiesTable = new HashMap<Class, Properties>();

    // 插件的@RegisterPlugin信息缓存
    private Map<Class<? extends Plugin>, RegisterPlugin> pluginRegisterPluginTable = new HashMap<>();

    /**
     * 注册能力点的具体实现插件
     *
     * @param abilityPointClazz 能力点接口
     * @param plugin            具体实现插件
     * @param registerPlugin    注册插件注解
     */
    void register(Class abilityPointClazz, Plugin plugin, RegisterPlugin registerPlugin) {
        // 规范性检查：不符合规范，就不加载
        String canonicalName = plugin.canonicalName();
        if (!canonicalName.endsWith(ExtSuffix)) {
            throw new PluginException(canonicalName + " must have suffix " + ExtSuffix);
        }

        if (pluginTable.containsKey(abilityPointClazz)) {
            pluginTable.get(abilityPointClazz).add(plugin);
        } else {
            List<Plugin> plugins = new ArrayList<Plugin>();
            plugins.add(plugin);
            pluginTable.put(abilityPointClazz, plugins);
        }

        boolean isDefaultExt = registerPlugin.businessUnitNoKeys().length == 0 &&
                registerPlugin.sellerNoKeys().length == 0;
        if (isDefaultExt) {
            if (defaultPluginTable.containsKey(abilityPointClazz)) {
                throw new PluginException(abilityPointClazz.getName() + " has duplicated default plugin: " + defaultPluginTable.get(abilityPointClazz).getClass().getName());
            }

            defaultPluginTable.put(abilityPointClazz, plugin);
        }

        // 注册该扩展点的属性文件：一个扩展点的所有插件共用一个properties文件
        registerProperties(abilityPointClazz);

        // 注册该扩展点的RegisterExt注解
        registerRegisterPlugin(plugin, registerPlugin);
    }

    private void registerProperties(Class extPtClazz) {
        synchronized (extPtClazz) {
            if (pluginPropertiesTable.containsKey(extPtClazz)) {
                return;
            }
            String resourceName = PropertiesRoot + extPtClazz.getSimpleName() + PropertiesSuffix;
            InputStream is = this.getClass().getResourceAsStream(resourceName);
            InputStreamReader inputStreamReader = null;
            Properties properties = new Properties();
            try {
                inputStreamReader = new InputStreamReader(is, "UTF-8");
                properties.load(inputStreamReader);
            } catch (IOException e) {
                throw new PluginException("fail to load properties of " + extPtClazz.getName());
            } finally {
                if (inputStreamReader != null) {
                    try {
                        inputStreamReader.close();
                    } catch (IOException ignored) {
                        log.error(resourceName, ignored);
                    }
                }
            }

            log.info("loaded {} for {}", resourceName, extPtClazz);
            pluginPropertiesTable.put(extPtClazz, properties);
        }
    }

    private void registerRegisterPlugin(Plugin plugin, RegisterPlugin registerPlugin) {
        synchronized (plugin) {
            if (!pluginRegisterPluginTable.containsKey(plugin.getClass())) {
                pluginRegisterPluginTable.put(plugin.getClass(), registerPlugin);
            } else {
                throw new PluginException("Duplicated plugin found: " + plugin.getClass().getCanonicalName());
            }
        }
    }

    void validate() throws PluginException {
        for (Class extPt : pluginTable.keySet()) {
            if (!defaultPluginTable.containsKey(extPt) || defaultPluginTable.get(extPt) == null) {
                throw new PluginException(extPt.getCanonicalName() + " has no default Plugin");
            }
        }
    }

    void startPlugins() {
        for (Map.Entry<Class, List<Plugin>> entry : pluginTable.entrySet()) {
            for (Plugin plugin : entry.getValue()) {
                log.debug("starting {}: {}", entry.getKey().getCanonicalName(), plugin);
                plugin.start(); // 如果抛出异常，就拒绝启动
            }
        }
    }

    void stopPlugins() {
        for (Map.Entry<Class, List<Plugin>> entry : pluginTable.entrySet()) {
            for (Plugin plugin : entry.getValue()) {
                log.debug("stopping {}: {}", entry.getKey().getCanonicalName(), plugin);
                try {
                    getTargetPlugin(plugin).stop();
                } catch (RuntimeException ignored) {
                    log.error("plugin[{}] stop issue", plugin, ignored);
                }
            }
        }
    }

    /**
     * 找到一个插件的扩展点接口
     *
     * @param clazz 一个{@link Plugin}
     * @return 一个ExtPt
     */
    static Class getAbilityPointInterfaceClassRecursive(Class clazz) {
        Class[] interfaces = clazz.getInterfaces();
        if (interfaces != null) {
            for (Class inter : interfaces) {
                if (inter.getName().endsWith(ExtPtSuffix)) {
                    return inter;
                }
            }
        }
        return getAbilityPointInterfaceClassRecursive(clazz.getSuperclass());
    }

    String getProperty(Plugin plugin, String propertyKey) {
        Class extPtClass = getAbilityPointInterfaceClassRecursive(plugin.getClass());
        if (!pluginPropertiesTable.containsKey(extPtClass)) {
            throw new RuntimeException(extPtClass.getName() + " properties not exists or has no Ext");
        }

        Properties properties = pluginPropertiesTable.get(extPtClass);
        if (!properties.containsKey(propertyKey)) {
            return null;
        }

        return properties.getProperty(propertyKey);
    }

    RegisterPlugin getRegisterExt(Plugin plugin) {
        if(plugin == null) return  null;

        plugin = getTargetPlugin(plugin);
        RegisterPlugin registerPlugin = plugin.getClass().getAnnotation(RegisterPlugin.class);
        return registerPlugin;
    }

}
