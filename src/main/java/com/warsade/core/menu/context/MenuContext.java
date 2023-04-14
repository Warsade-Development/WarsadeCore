package com.warsade.core.menu.context;

import com.warsade.core.menu.slot.MenuSlot;
import com.warsade.core.menu.slot.impl.MenuSlotImpl;
import me.saiintbrisson.minecraft.ViewContext;
import me.saiintbrisson.minecraft.ViewItem;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

public abstract class MenuContext {

    HashMap<Integer, MenuSlot<?>> slots = new HashMap<>();

    ViewContext viewContext;

    public MenuContext(ViewContext viewContext) {
        this.viewContext = viewContext;
    }

    protected ViewContext getViewContext() {
        return viewContext;
    }

    public <T> T getMenuObject() {
        return viewContext.get("object");
    }

    public <T> T getData(String dataName) {
        return viewContext.get(dataName);
    }

    public <T> MenuSlot<T> getSlot(int slot) {
        if (slots.containsKey(slot)) return (MenuSlot<T>) slots.get(slot);

        ViewItem viewItem = viewContext.slot(slot);

        if (viewItem.getItem() == null) return null;
        else return new MenuSlotImpl<>(slot, (ItemStack) viewItem.getItem(), getMenuObject(), null);
    }

    public MenuSlot<?> getSlotByItemStack(ItemStack itemStack) {
        Optional<MenuSlot<?>> foundSlot = slots.values().stream().filter(slot -> slot.getItem() == itemStack).findFirst();
        if (foundSlot.isPresent()) {
            return foundSlot.get();
        } else {
            return getSlotByItemStackWithViewContext(itemStack);
        }
    }

    private MenuSlot<?> getSlotByItemStackWithViewContext(ItemStack itemStack) {
        ViewItem viewItem = Arrays.stream(viewContext.getRoot().getItems()).filter(item -> item.getItem() == itemStack).findFirst().orElse(null);

        if (viewItem == null) return null;
        else return new MenuSlotImpl<>(viewItem.getSlot(), (ItemStack) viewItem.getItem(), getMenuObject(), null);
    }

    public <T> void attachSlot(MenuSlot<T> menuSlot) {
        slots.put(menuSlot.getSlot(), menuSlot);
    }

}