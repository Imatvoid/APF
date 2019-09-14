package com.arch.ability;

/**
 * 将PluginContext可以暴露的方法对Plugin暴露
 */
public final class PluginContextWrapper {

    private final PluginContext pluginContext;

    public PluginContextWrapper(PluginContext pluginContext) {
        this.pluginContext = pluginContext;
    }

    /**
     * 根据key获取资源文件里配置的值
     *
     * @param plugin 对于{@link Plugin}传入{@code this}即可
     * @param propertyKey
     * @return null if propertyKey not found
     */
    public String getProperty(Plugin plugin, String propertyKey) {
        return pluginContext.getProperty(plugin, propertyKey);
    }

    /**
     * 从插件的properties文件里根据指定数据类型获取某个key对应的值
     *
     * @param plugin
     * @param propertyKey
     * @param type 只支持 String, Integer, Float, Boolean
     * @param <T>
     * @return null if propertyKey not found or type not supported
     */
    public <T> T getProperty(Plugin plugin, String propertyKey, Class<T> type) {
        String value = getProperty(plugin, propertyKey);
        if (value == null) {
            return null;
        }

        try {
            if (type == String.class) {
                return (T) value;
            }
            if (type == Boolean.class) {
                return (T) Boolean.valueOf(value);
            }
            if (type == Integer.class) {
                return (T) Integer.valueOf(value);
            }
            if (type == Float.class) {
                return (T) Float.valueOf(value);
            }
        } catch (Exception ignored) {
            // might be NumberFormatException
        }

        return null;
    }

}
