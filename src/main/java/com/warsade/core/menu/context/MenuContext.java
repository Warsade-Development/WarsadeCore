package com.warsade.core.menu.context;

import me.saiintbrisson.minecraft.ViewContext;

public abstract class MenuContext {

    ViewContext viewContext;

    public MenuContext(ViewContext viewContext) {
        this.viewContext = viewContext;
    }

    public ViewContext getViewContext() {
        return viewContext;
    }

}