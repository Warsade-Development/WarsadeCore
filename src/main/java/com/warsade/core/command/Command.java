package com.warsade.core.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class Command extends org.bukkit.command.Command implements CommandExecutor {

    final private Plugin plugin;

    protected Command(Plugin plugin, @NotNull String name) {
        super(name);
        this.plugin = plugin;
    }

    protected Command(Plugin plugin, @NotNull String name, String... aliases) {
        this(plugin, name);
        setAliases(Arrays.asList(aliases));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        return true;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) return onExecute(sender, args);
        else {
            List<CommandArgument> arguments = getArguments();
            if (arguments.size() > 0) {
                Optional<CommandArgument> foundArgument = arguments.stream()
                        .filter(argument -> argument.getArgumentName().equalsIgnoreCase(args[0]))
                        .findFirst();
                return foundArgument.map(argument -> argument.execute(sender, args, this)).orElseGet(() -> onExecute(sender, args));
            }
        }
        return true;
    }

    public abstract List<CommandArgument> getArguments();
    public abstract boolean onExecute(CommandSender sender, String[] args);
    public abstract void onErrorResponse(String errorMessage);

    public Plugin getPlugin() {
        return plugin;
    }

}
