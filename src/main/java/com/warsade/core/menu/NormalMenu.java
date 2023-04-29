package com.warsade.core.menu;

import com.warsade.core.CorePlugin;
import com.warsade.core.config.providers.MenuConfig;
import com.warsade.core.menu.context.MenuContext;
import com.warsade.core.menu.context.NormalMenuContext;
import com.warsade.core.menu.slot.MenuSlot;
import me.saiintbrisson.minecraft.AbstractView;
import me.saiintbrisson.minecraft.View;
import me.saiintbrisson.minecraft.ViewContext;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class NormalMenu <T> extends View implements Menu<T> {

    static HashMap<UUID, MenuContext> menuContexts = new HashMap<>();

    MenuConfig menuConfig;

    public NormalMenu() {}

    public NormalMenu(MenuConfig menuConfig) {
        super(menuConfig.getRows(), menuConfig.getName());
        setCancelOnClick(true);

        this.menuConfig = menuConfig;

        if (menuConfig.hasLayout()) {
            menuConfig.getMenuLayout().getPlaceholdersItems().forEach(placeholderItem -> {
                setLayout(placeholderItem.getKey().toUpperCase().charAt(0), onRenderItem -> {
                    onRenderItem.setItem(placeholderItem.toItemStack());
                });
            });
            setLayout(menuConfig.getMenuLayout().getValue());
        }
    }

    @Override
    public String getInventoryTitle() {
        return menuConfig.getName();
    }

    @Override
    public void openInventory(Player player) {
        openInventory(player, null);
    }

    @Override
    public void openInventory(Player player, T data) {
        openInventory(player, new HashMap<>(), data);
    }

    @Override
    public void openInventory(Player player, HashMap<String, Object> initialData, T object) {
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

    public abstract Consumer<MenuContext> onOpen(Player player, T data);
    public abstract void onClose(Player player);

    @Override
    protected void onClose(@NotNull ViewContext context) {
        super.onClose(context);
        menuContexts.remove(context.getPlayer().getUniqueId());
    }

    @Override
    protected void onRender(@NotNull ViewContext context) {
        super.onRender(context);

        setupInventory(context);
    }

    private void setupInventory(ViewContext viewContext) {
        T data = viewContext.get("object");
        MenuContext menuContext = new NormalMenuContext(viewContext);

        menuContexts.put(viewContext.getPlayer().getUniqueId(), menuContext);
        onOpen(viewContext.getPlayer(), data).accept(menuContext);
    }

    @Override
    public MenuContext getMenuContextByPlayer(Player player) {
        if (menuContexts.containsKey(player.getUniqueId())) return getMenuContexts().get(player.getUniqueId());

        ViewContext playerViewContext = getContexts().stream().filter(viewContext -> viewContext.getPlayer().getUniqueId().equals(player.getUniqueId())).findFirst().orElse(null);
        if (playerViewContext != null) {
            return new NormalMenuContext(playerViewContext);
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
    public void attachSlot(MenuSlot<T> menuSlot, MenuContext menuContext) {
        menuContext.attachSlot(menuSlot);
    }

    public MenuConfig getMenuConfig() {
        return menuConfig;
    }

    public HashMap<UUID, MenuContext> getMenuContexts() {
        return menuContexts;
    }

}