package com.warsade.core.menu.context;

import com.warsade.core.menu.slot.MenuSlot;
import com.warsade.core.menu.slot.MenuSlotClick;
import com.warsade.core.menu.slot.impl.MenuSlotClickImpl;
import me.saiintbrisson.minecraft.ViewContext;

public class PaginatedMenuContext extends MenuContext {

    public PaginatedMenuContext(ViewContext viewContext) {
        super(viewContext);
    }

    @Override
    public <T> void attachSlot(MenuSlot<T> menuSlot) {
        ViewContext viewContext = getViewContext();
        viewContext.slot(menuSlot.getSlot(), menuSlot.getItem()).onClick(viewSlotClickContext -> {
            MenuSlotClick<T> menuSlotClick = new MenuSlotClickImpl<>(menuSlot, viewSlotClickContext.getPlayer(), viewSlotClickContext.getItemWrapper().asBukkitItem(), viewSlotClickContext.getSlot());
            if (menuSlot.getClickAction() != null) menuSlot.getClickAction().accept(menuSlotClick);
        });

        super.attachSlot(menuSlot);
    }

}