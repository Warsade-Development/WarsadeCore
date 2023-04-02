package com.warsade.core.menu;

import com.warsade.core.CorePlugin;
import com.warsade.core.config.providers.MenuConfig;
import com.warsade.core.menu.context.MenuContext;
import com.warsade.core.menu.context.MenuContextImpl;
import com.warsade.core.menu.slot.MenuSlot;
import com.warsade.core.menu.slot.MenuSlotClick;
import com.warsade.core.menu.slot.impl.MenuSlotClickImpl;
import com.warsade.core.menu.slot.impl.MenuSlotImpl;
import me.saiintbrisson.minecraft.AbstractView;
import me.saiintbrisson.minecraft.View;
import me.saiintbrisson.minecraft.ViewContext;
import me.saiintbrisson.minecraft.ViewItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

public abstract class NormalMenu <T> extends View implements Menu<T> {

    MenuConfig menuConfig;

    public NormalMenu() {}

    public NormalMenu(MenuConfig menuConfig) {
        super(menuConfig.getRows(), menuConfig.getName());
        setCancelOnClick(true);

        this.menuConfig = menuConfig;
    }

    @Override
    public String getInventoryTitle() {
        return menuConfig.getName();
    }

    @Override
    public void openInventory(Player player) {
        openForPlayer(player, Collections.emptyMap(), onOpen(player, null));
    }

    @Override
    public void openInventory(Player player, T data) {
        openForPlayer(player, Collections.emptyMap(), onOpen(player, data));
    }

    @Override
    public void openInventory(Player player, Map<String, Object> initialData, T object) {
        openForPlayer(player, initialData, onOpen(player, object));
    }

    @Override
    public void closeInventory(Player player) {
        onClose(player);
        closeForPlayer(player);
    }

    private void openForPlayer(Player player, Map<String, Object> data, Consumer<MenuContext> setup) {
        CorePlugin.getMenuRegistry().getViewFrameByMenuClass((Class<? extends Menu<?>>) this.getClass()).ifPresent(viewFrame -> {
            data.put("setup", setup);
            viewFrame.open(this.getClass(), player, data);
        });
    }

    public abstract Consumer<MenuContext> onOpen(Player player, T data);
    public abstract void onClose(Player player);

    public void closeForPlayer(Player player) {
        CorePlugin.getMenuRegistry().getViewFrameByMenuClass((Class<? extends Menu<?>>) this.getClass()).ifPresent(viewFrame -> {
            AbstractView view = viewFrame.get(player);
            if (view != null) view.closeUninterruptedly();
        });
    }

    @Override
    protected void onRender(@NotNull ViewContext context) {
        super.onRender(context);
        setupInventory(context);
    }

    private void setupInventory(ViewContext viewContext) {
        Consumer<MenuContext> menuContextConsumer = viewContext.get("setup");
        menuContextConsumer.accept(new MenuContextImpl(viewContext));
    }

    @Override
    public MenuSlot getSlot(int slot) {
        ViewItem viewItem = slot(slot);

        if (viewItem.getItem() == null) return null;
        else return new MenuSlotImpl(slot, (ItemStack) viewItem.getItem(), null);
    }

    @Override
    public MenuSlot getSlotByItemStack(ItemStack itemStack) {
        ViewItem viewItem = Arrays.stream(getItems()).filter(item -> item.getItem() == itemStack).findFirst().orElse(null);

        if (viewItem == null) return null;
        else return new MenuSlotImpl(viewItem.getSlot(), (ItemStack) viewItem.getItem(), null);
    }

    @Override
    public void attachSlot(MenuSlot menuSlot, MenuContext menuContext) {
        ViewContext viewContext = menuContext.getViewContext();
        viewContext.slot(menuSlot.getSlot(), menuSlot.getItem()).onClick(viewSlotClickContext -> {
            MenuSlotClick menuSlotClick = new MenuSlotClickImpl(viewSlotClickContext.getPlayer(), viewSlotClickContext.getCurrentItem().asBukkitItem(), viewSlotClickContext.getSlot());
            if (menuSlot.getClickAction() != null) menuSlot.getClickAction().accept(menuSlotClick);
        });
    }

    public MenuConfig getMenuConfig() {
        return menuConfig;
    }

}