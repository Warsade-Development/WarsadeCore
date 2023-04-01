package com.warsade.core.command.requirements.impl;

import com.warsade.core.command.CommandArgument;
import com.warsade.core.command.requirements.RequirementValidator;
import com.warsade.core.command.requirements.RequirementValidatorResponse;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class AnotherPlayerValidatorImpl extends RequirementValidator<Player> {

    @Override
    public void setupArgumentVariable(CommandArgument argument, Object result) {
        argument.setTarget((Player) result);
    }

    @Override
    public RequirementValidatorResponse<Player> validate(Plugin plugin, CommandSender sender, String[] args) {
        Player target = Bukkit.getPlayer(args[1]);

        getResponse().setResult(target);
        if (target == null) getResponse().setErrorMessage("playerNotFound");

        return getResponse();
    }

}