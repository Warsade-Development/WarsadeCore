package com.warsade.core.menu;

import com.warsade.core.CorePlugin;
import com.warsade.core.config.providers.MenuConfig;
import com.warsade.core.menu.context.MenuContext;
import com.warsade.core.menu.context.PaginatedMenuContext;
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

    static HashMap<UUID, MenuContext> menuContexts = new HashMap<>();

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
    protected void onClose(@NotNull ViewContext context) {
        super.onClose(context);
        menuContexts.remove(context.getPlayer().getUniqueId());
    }

    @Override
    public MenuContext getMenuContextByPlayer(Player player) {
        if (menuContexts.containsKey(player.getUniqueId())) return getMenuContexts().get(player.getUniqueId());

        ViewContext playerViewContext = getContexts().stream().filter(viewContext -> viewContext.getPlayer().getUniqueId().equals(player.getUniqueId())).findFirst().orElse(null);
        if (playerViewContext != null) {
            return new PaginatedMenuContext(playerViewContext);
        } else {
            return null;
        }
    }

    /**
     * Add a new item to this menu
     * @deprecated
     * This method has been moved to {@link MenuContext#attachSlot(MenuSlot)}
     */
    @Override
    public void attachSlot(MenuSlot<K> menuSlot, MenuContext menuContext) {
        menuContext.attachSlot(menuSlot);
    }

    @Override
    protected void onRender(@NotNull ViewContext viewContext) {
        super.onRender(viewContext);
        K data = viewContext.get("object");
        MenuContext menuContext = new PaginatedMenuContext(viewContext);

        viewContext.paginated().setSource(getItems(viewContext.getPlayer(), data));
        onOpen(viewContext.getPlayer(), data).accept(menuContext);

        menuContexts.put(viewContext.getPlayer().getUniqueId(), menuContext);
    }

    @Override
    protected void onItemRender(@NotNull PaginatedViewSlotContext<T> paginatedViewSlotContext, @NotNull ViewItem viewItem, @NotNull T value) {
        viewItem.withItem(value)
                .onRender(render -> render.setItem(getViewItemStack(value)))
                .onClick(viewSlotClickContext -> {
                    MenuSlot<T> menuSlot = new MenuSlotImpl<>(viewSlotClickContext.getSlot(), viewSlotClickContext.getCurrentItem().asBukkitItem(), value, null);
                    MenuSlotClick<T> menuSlotClick = new MenuSlotClickImpl<>(menuSlot, viewSlotClickContext.getPlayer(), viewSlotClickContext.getCurrentItem().asBukkitItem(), viewSlotClickContext.getSlot());

                    onItemClick(menuSlotClick);
                });
    }

    public abstract ItemStack getViewItemStack(T value);
    public abstract void onItemClick(MenuSlotClick<T> onClick);

    public MenuConfig getMenuConfig() {
        return menuConfig;
    }

    @Override
    public HashMap<UUID, MenuContext> getMenuContexts() {
        return menuContexts;
    }

}