package com.warsade.core.plugin.exception;

public class PluginAlreadyRegisteredException extends RuntimeException {

    public PluginAlreadyRegisteredException() {
        super("There's already a plugin registered with this name.");
    }

}
