package com.warsade.core.bukkit.commands;

import com.warsade.core.CorePlugin;
import com.warsade.core.bukkit.view.PluginsMenu;
import com.warsade.core.command.Command;
import com.warsade.core.command.CommandArgument;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.List;

public class CoreCommand extends Command {

    public CoreCommand(Plugin plugin) {
        super(plugin, "core", "warsadecore", "wcore", "wc");
    }

    @Override
    public List<CommandArgument> getArguments() {
        return Collections.emptyList();
    }

    @Override
    public boolean onExecute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            getPlugin().getLogger().info("Registered Warsade plugins:");
            CorePlugin.getPlugins().forEach(corePlugin -> getPlugin().getLogger().info(corePlugin.getName()));
        } else {
            new PluginsMenu().openInventory((Player) sender);
        }
        return true;
    }

    @Override
    public void onErrorResponse(CommandSender sender, String[] args, String errorMessage) {
    }

}
