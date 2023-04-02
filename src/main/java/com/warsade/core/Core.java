package com.warsade.core;

public class Core extends CorePlugin {

    @Override
    public void onEnable() {
        super.onEnable();

        getLogger().info("Core plugin enabled");
    }

    @Override
    public void onDisable() {
        super.onDisable();
        getLogger().info("Core plugin disabled");
    }

}
