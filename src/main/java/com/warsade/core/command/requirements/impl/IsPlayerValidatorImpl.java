package com.warsade.core.command.requirements.impl;

import com.warsade.core.command.CommandArgument;
import com.warsade.core.command.requirements.RequirementValidator;
import com.warsade.core.command.requirements.RequirementValidatorResponse;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class IsPlayerValidatorImpl extends RequirementValidator<Player> {

    @Override
    public void setupArgumentVariable(CommandArgument argument, Object result) {
        argument.setPlayer((Player) result);
    }

    @Override
    public RequirementValidatorResponse<Player> validate(Plugin plugin, CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            getResponse().setResult((Player) sender);
        } else {
            getResponse().setErrorMessage("onlyInGame");
        }

        return getResponse();
    }

}