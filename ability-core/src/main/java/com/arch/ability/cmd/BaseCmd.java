package com.arch.ability.cmd;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 业务能力扩展点的指令基础类
 */
public abstract class BaseCmd {
    private Map<String, Object> attachments;

    public final void setAttachment(String key, Object value) {

        if (attachments == null) {
            attachments = new HashMap<String, Object>();
        }
        attachments.put(key, value);

    }

    public final Object getAttachment(String key) {
        return attachments.get(key);
    }

    public final <T> T getAttachment(String key, Class<T> type) throws ClassCastException {
        Object value = getAttachment(key);
        if (value == null) {
            return null;
        }
        return type.cast(value);
    }

}
