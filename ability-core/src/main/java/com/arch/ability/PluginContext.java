package com.arch.ability;


import com.arch.ability.annotation.PhaseEnum;
import com.arch.ability.annotation.RegisterPlugin;
import com.arch.ability.exception.PluginException;
import com.arch.ability.exception.PluginNotFoundException;
import com.arch.ability.identity.Identifier;
import com.arch.ability.router.CommonRouter;
import com.arch.ability.router.Router;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.TargetSource;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Set;

import static com.arch.ability.utils.AopTargetUtils.getTargetPlugin;

/**
 * 扩展点的统一注册、加载器
 * <p>
 * <p>使用时，需要显式地配置spring bean，并且是单例</p>
 * {@code <bean id="pluginContext" class="com.arch.ability.PluginContext"/>}
 */
@Slf4j
@Getter
@Setter
public class PluginContext implements ApplicationContextAware {

    /**
     * 声明当前应用处理的所有阶段类型，为了按不同阶段需要加载不同的依赖
     */
    private Set<PhaseEnum> phases;

    private Registry registry;

    private Router router = new CommonRouter(); // by default

    /**
     * 宿主系统的应用名称
     * <p>
     */
    private String appName = System.getProperty("deploy.app.name");

    /**
     * 外部依赖检查器
     */
    private RequirementManager requirementManager;

    /**
     * 根据业务身份{@code Identifier}加载插件，并拦截该插件的方法调用
     *
     * @param extPtCls     业务能力扩展点
     * @param identifier   业务身份标识
     * @param <T>
     * @return 与业务身份匹配的Plugin
     * @throws PluginNotFoundException
     */
    public <T> T load(Class<T> extPtCls, Identifier identifier) throws PluginNotFoundException {
        T plugin = router.route(extPtCls, identifier);
        if (plugin == null) {
            throw new PluginNotFoundException("ExtPt[" + extPtCls.getCanonicalName() + "] plugin identified by " + identifier.toString() + " not found");
        }
        return plugin;
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.initialize(applicationContext);
        this.bootstrap(applicationContext);
    }

    private void bootstrap(ApplicationContext applicationContext) {
        log.warn("Plugins bootstrap for phases: {}", this.phases);

        if (appName == null || appName.isEmpty()) {
            throw new PluginException("appName is required! e,g. xxx");
        }

        if (phases == null || phases.isEmpty()) {
            throw new PluginException("phases是必填的");
        }

        if (router == null) {
            throw new PluginException(("router cannot be null"));
        }

        for (Object ext : applicationContext.getBeansWithAnnotation(RegisterPlugin.class).values()) {
            // 兼容JDK代理方式的AOP
            if (ext.getClass().getName().startsWith("com.sun.proxy.")) {
                try {
                    ext = ((TargetSource) ext.getClass().getDeclaredMethod("getTargetSource").invoke(ext)).getTarget();
                } catch (Exception e) {
                    throw new PluginException("Component annotated with @RegisterExt must extends Plugin: " + ext.getClass().getName());
                }
            }

            if (!(ext instanceof Plugin)) {
                // 规范性检查，其实不继承Plugin也不影响插件执行。但，强校验有利于在研发人员里建立规范化
                throw new PluginException("Component annotated with @RegisterExt must extends Plugin: " + ext.getClass().getName());
            }

            Class<? extends Object> pluginClazz = ext.getClass();
            log.info("loading {}", pluginClazz.getCanonicalName());

            // 获取该插件对应的扩展点接口：需要考虑多级继承关系
            Class extPtClazz = Registry.getAbilityPointInterfaceClassRecursive(pluginClazz);

            // wrapper the plugin and register the plugin
            Plugin plugin = (Plugin) ext;
            RegisterPlugin registerPlugin = registry.getRegisterExt(plugin);
            Plugin targetPlugin = getTargetPlugin(plugin);
            targetPlugin.setWrapper(new PluginContextWrapper(this));
            registry.register(extPtClazz, plugin, registerPlugin);

            // 处理外部依赖的满足条件：
            // 插件在能力中心界面注册自己对外部依赖的需求，注册bean id，并通过注解在插件代码里声明依赖关系
            requirementManager.handleRequirement(phases, targetPlugin);
        }

        registry.validate();

        log.warn("Plugins loaded fully");
        this.startPlugins();
        log.warn("Plugins started fully");

        log.warn("Add shutdown hook for all plugins to call stop()");
        final PluginContext theContext = this;
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                theContext.stopPlugins();
                log.warn("Plugins stopped fully");
            }
        });
    }

    private void startPlugins() {
        registry.startPlugins();
    }

    private void stopPlugins() {
        registry.stopPlugins();
    }

    private void initialize(ApplicationContext applicationContext) {
        this.registry = new Registry();
        this.router.setRegistry(this.registry);
        this.requirementManager = new RequirementManager(applicationContext);
    }


    public String getProperty(Plugin plugin, String propertyKey) {
        return registry.getProperty(plugin, propertyKey);
    }


}
