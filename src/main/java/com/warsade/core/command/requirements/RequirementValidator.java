package com.warsade.core.command.requirements;

import com.warsade.core.command.CommandArgument;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public abstract class RequirementValidator <T> {

    RequirementValidatorResponse<T> response = new RequirementValidatorResponse<>();

    public abstract void setupArgumentVariable(CommandArgument argument, Object result);
    public abstract RequirementValidatorResponse<T> validate(Plugin plugin, CommandSender sender, String[] args);

    public RequirementValidatorResponse<T> getResponse() {
        return response;
    }

}