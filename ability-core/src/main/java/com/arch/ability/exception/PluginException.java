package com.arch.ability.exception;

public class PluginException extends RuntimeException {

    public PluginException(String message) {
        super(message);
    }

    public PluginException(Throwable cause) {
        super(cause);
    }

}
