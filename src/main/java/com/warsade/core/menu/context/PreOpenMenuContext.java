package com.warsade.core.menu.context;

import com.warsade.core.config.providers.MenuConfig;

public class PreOpenMenuContext {

    String inventoryTitle;

    public PreOpenMenuContext(MenuConfig menuConfig) {
        this.inventoryTitle = menuConfig.getName();
    }

    public String getInventoryTitle() {
        return inventoryTitle;
    }

    public void setInventoryTitle(String title) {
        this.inventoryTitle = title;
    }

}
