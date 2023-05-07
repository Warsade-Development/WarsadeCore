package com.warsade.core.menu.context;

import com.warsade.core.config.providers.MenuConfig;

public class PreOpenMenuContext {

    String inventoryTitle;
    int inventoryRows;

    public PreOpenMenuContext(MenuConfig menuConfig) {
        this.inventoryTitle = menuConfig.getName();
        this.inventoryRows = menuConfig.getRows();
    }

    public String getInventoryTitle() {
        return inventoryTitle;
    }

    public void setInventoryTitle(String title) {
        this.inventoryTitle = title;
    }

    public int getInventoryRows() {
        return inventoryRows;
    }

    public void setInventoryRows(int inventoryRows) {
        this.inventoryRows = inventoryRows;
    }

}
