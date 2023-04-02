package com.warsade.core.menu;

import com.warsade.core.CorePlugin;
import com.warsade.core.config.providers.MenuConfig;
import com.warsade.core.menu.context.MenuContext;
import com.warsade.core.menu.context.MenuContextImpl;
import com.warsade.core.menu.slot.MenuSlot;
import com.warsade.core.menu.slot.MenuSlotClick;
import com.warsade.core.menu.slot.impl.MenuSlotClickImpl;
import com.warsade.core.menu.slot.impl.MenuSlotImpl;
import me.saiintbrisson.minecraft.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public abstract class PaginatedMenu<T, K> extends PaginatedView<T> implements Menu<K> {

    MenuConfig menuConfig;

    public PaginatedMenu() {}

    public PaginatedMenu(MenuConfig menuConfig, String... layout) {
        super(6, menuConfig.getName());
        setCancelOnClick(true);

        this.menuConfig = menuConfig;

        setLayout(layout);
        setPreviousPageItem((context, viewItem) -> {
            if (!context.hasPreviousPage()) return;

            menuConfig.getItems().stream().filter(itemSchema -> itemSchema.getKey().equalsIgnoreCase("previous")).findFirst().ifPresent(itemSchema -> {
                viewItem.withItem(itemSchema.toItemStack());
            });
        });
        setNextPageItem((context, viewItem) -> {
            if (!context.hasNextPage()) return;

            menuConfig.getItems().stream().filter(itemSchema -> itemSchema.getKey().equalsIgnoreCase("next")).findFirst().ifPresent(itemSchema -> {
                viewItem.withItem(itemSchema.toItemStack());
            });
        });
    }

    public String getInventoryTitle() {
        return menuConfig.getName();
    }

    public abstract List<T> getItems(Player player, K data);

    @Override
    public void openInventory(Player player) {
        openInventory(player, null);
    }

    @Override
    public void openInventory(Player player, K data) {
        openInventory(player, new HashMap<>(), data);
    }

    @Override
    public void openInventory(Player player, HashMap<String, Object> initialData, K object) {
        initialData.put("object", object);

        CorePlugin.getMenuRegistry().getViewFrameByMenuClass((Class<? extends Menu<?>>) this.getClass()).ifPresent(viewFrame -> {
            viewFrame.open(this.getClass(), player, initialData);
        });
    }

    @Override
    public void closeInventory(Player player) {
        onClose(player);

        CorePlugin.getMenuRegistry().getViewFrameByMenuClass((Class<? extends Menu<?>>) this.getClass()).ifPresent(viewFrame -> {
            AbstractView view = viewFrame.get(player);
            if (view != null) view.closeUninterruptedly();
        });
    }

    public abstract Consumer<MenuContext> onOpen(Player player, K data);
    public abstract void onClose(Player player);

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
            MenuSlotClick menuSlotClick = new MenuSlotClickImpl(viewSlotClickContext.getPlayer(), viewSlotClickContext.getItemWrapper().asBukkitItem(), viewSlotClickContext.getSlot());
            if (menuSlot.getClickAction() != null) menuSlot.getClickAction().accept(menuSlotClick);
        });
    }

    @Override
    protected void onRender(@NotNull ViewContext viewContext) {
        super.onRender(viewContext);
        K data = viewContext.get("object");
        Consumer<MenuContext> menuContextConsumer = onOpen(viewContext.getPlayer(), data);

        viewContext.paginated().setSource(getItems(viewContext.getPlayer(), data));
        menuContextConsumer.accept(new MenuContextImpl(viewContext));
    }

    @Override
    protected void onItemRender(@NotNull PaginatedViewSlotContext<T> paginatedViewSlotContext, @NotNull ViewItem viewItem, @NotNull T value) {
        viewItem.withItem(value)
                .onRender(render -> render.setItem(getViewItemStack(value)))
                .onClick(viewSlotClickContext -> {
                    MenuSlotClick menuSlotClick = new MenuSlotClickImpl(viewSlotClickContext.getPlayer(), viewSlotClickContext.getCurrentItem().asBukkitItem(), viewSlotClickContext.getSlot());
                    onItemClick(menuSlotClick);
                });
    }

    public abstract ItemStack getViewItemStack(T value);
    public abstract void onItemClick(MenuSlotClick onClick);

    public MenuConfig getMenuConfig() {
        return menuConfig;
    }

}
