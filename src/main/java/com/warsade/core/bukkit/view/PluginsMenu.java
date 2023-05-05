package com.warsade.core.bukkit.view;

import com.warsade.core.Core;
import com.warsade.core.CorePlugin;
import com.warsade.core.config.providers.MenuConfig;
import com.warsade.core.menu.PaginatedMenu;
import com.warsade.core.menu.context.MenuContext;
import com.warsade.core.menu.slot.MenuSlotClick;
import com.warsade.core.utils.ItemBuilder;
import com.warsade.core.utils.MessageUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PluginsMenu extends PaginatedMenu<CorePlugin, Void> {

    public PluginsMenu() {
    }

    public PluginsMenu(MenuConfig menuConfig) {
        super(menuConfig);
    }

    @Override
    public List<CorePlugin> getItems(Player player, Void data) {
        List<CorePlugin> plugins = new ArrayList<>();
        plugins.add(CorePlugin.getPlugin(Core.class));
        CorePlugin.getPlugins().forEach(plugin -> {
            if (plugin.getClass() != Core.class) plugins.add(plugin);
        });

        return plugins;
    }

    @Override
    public Consumer<MenuContext> onOpen(Player player, Void data) {
        return menuContext -> {
        };
    }

    @Override
    public void onClose(Player player) {
    }

    @Override
    public ItemStack getViewItemStack(CorePlugin plugin) {
        List<String> lore = new ArrayList<>();
        lore.add("&f");
        lore.add("&fVersion: &7" + plugin.getDescription().getVersion());
        lore.add("&fAuthors: &7" + plugin.getDescription().getAuthors());
        lore.add("&fEvents used:");
        getUsedEventsFromPlugin(plugin).forEach(event -> lore.add("&8" + event));
        lore.add("&fCommands:");
        getCommandsFromPlugin(plugin).forEach(command -> lore.add("&8" + command));
        lore.add("&f");
        lore.add("&aClick to reload");
        lore.add("&f");

        return new ItemBuilder(plugin.getPluginIcon())
                .setName(ChatColor.YELLOW + plugin.getDescription().getName())
                .setLore(MessageUtils.replaceColor(lore))
                .build();
    }

    private List<String> getUsedEventsFromPlugin(CorePlugin plugin) {
        List<String> events = new ArrayList<>();
        HandlerList.getRegisteredListeners(plugin).forEach(event -> {
            String eventName = event.getListener().toString().split("@")[0];
            if (!events.contains(eventName)) {
                events.add(eventName);
            }
        });

        if (events.isEmpty()) events.add("None");

        return events;
    }

    private List<String> getCommandsFromPlugin(CorePlugin plugin) {
        List<String> commands = new ArrayList<>();
        plugin.getDescription().getCommands().forEach((command, unused) -> {
            commands.add("/" + command);
        });

        if (commands.isEmpty()) commands.add("None");

        return commands;
    }

    @Override
    public void onItemClick(MenuSlotClick<CorePlugin> onClick) {
        CorePlugin plugin = onClick.getMenuSlot().getData();
        plugin.reloadConfig();
        plugin.reload();

        onClick.getPlayer().sendMessage(MessageUtils.replaceColor("&aPlugin &f" + plugin.getDescription().getName() + " &areloaded"));
    }

}
