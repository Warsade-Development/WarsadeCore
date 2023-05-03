package com.warsade.core.menu.context;

import com.warsade.core.menu.slot.MenuSlot;
import com.warsade.core.menu.slot.MenuSlotClick;
import com.warsade.core.menu.slot.impl.MenuSlotClickImpl;
import me.saiintbrisson.minecraft.ViewContext;
import org.bukkit.event.inventory.ClickType;

public class NormalMenuContext extends MenuContext {

    public NormalMenuContext(ViewContext viewContext) {
        super(viewContext);
    }

    @Override
    public <T> void attachSlot(MenuSlot<T> menuSlot) {
        ViewContext viewContext = getViewContext();
        viewContext.slot(menuSlot.getSlot(), menuSlot.getItem()).onClick(viewSlotClickContext -> {
            MenuSlotClick<T> menuSlotClick = new MenuSlotClickImpl<>(menuSlot, ClickType.valueOf(viewSlotClickContext.getClickIdentifier()), viewSlotClickContext.getPlayer(), viewSlotClickContext.getCurrentItem().asBukkitItem(), viewSlotClickContext.getSlot());
            if (menuSlot.getClickAction() != null) menuSlot.getClickAction().accept(menuSlotClick);
        });

        super.attachSlot(menuSlot);
    }

}
