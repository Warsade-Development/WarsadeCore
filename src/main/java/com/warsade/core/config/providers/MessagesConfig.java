package com.warsade.core.config.providers;

import com.warsade.core.config.Config;

import java.util.ArrayList;
import java.util.List;

public class MessagesConfig {

    final private Config langConfig;

    public MessagesConfig(Config langConfig) {
        this.langConfig = langConfig;
    }

    public String getMessage(String message) {
        return langConfig.get().getString("Messages." + message);
    }

    public List<String> getMessageList(String message) {
        return new ArrayList<>(langConfig.get().getStringList("Messages." + message));
    }

}