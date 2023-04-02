package com.warsade.core.command;

import com.warsade.core.command.requirements.RequirementValidator;
import com.warsade.core.command.requirements.RequirementValidatorResponse;
import com.warsade.core.utils.Pair;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

public abstract class CommandArgument {

    Player player = null;
    Player target = null;

    final private Plugin plugin;
    final private String argumentName;

    public CommandArgument(Plugin plugin, String argumentName) {
        this.plugin = plugin;
        this.argumentName = argumentName;
    }

    /**
     * Will try to invoke the argument based on the requirements
     * @return true if everything goes well and false if a requirement is not approved
     */
    public boolean execute(CommandSender sender, String[] args, Command command) {
        Pair<Boolean, String> requirementsCheckResult = checkRequirements(sender, argumentName, args);
        boolean meetRequirements = requirementsCheckResult.getFirst();
        String errorMessage = requirementsCheckResult.getSecond();
        if (!meetRequirements) {
            command.onErrorResponse(errorMessage);
            return false;
        }

        invoke(sender, args);
        return true;
    }

    /**
     * This method will go through all requirements
     * and will call the validate method from the validator
     * to check if the provided args match the requirement
     *
     * this method also sends a message to the provided sender
     * if the requirement fails with the returned error message
     * from the validator.
     *
     * @return A [Pair] containing true if everything goes well
     * and false if a requirement is not approved and
     * an error message if the command doesn't meet the argument
     * requirements.
     */
    private Pair<Boolean, String> checkRequirements(CommandSender sender, String argument, String[] args) {
        if (args.length < getArgsSize()) {
            return new Pair<>(false, "help." + argument);
        }

        boolean meetRequirements = true;
        String errorMessage = null;

        for (RequirementValidator<?> requirement : getValidators()) {
            RequirementValidatorResponse<?> response = requirement.validate(plugin, sender, args);
            if (!response.isSucceeded()) {
                meetRequirements = false;
                errorMessage = response.getErrorMessage();
                break;
            }

            setupCommand(requirement, response.getResult());
        }

        return new Pair<>(meetRequirements, errorMessage);
    }

    /**
     * This method will set up the default variables
     * based on the actual requirement
     *
     * @param result the object returned from the argument validator
     */
    protected void setupCommand(RequirementValidator<?> requirement, Object result) {
        requirement.setupArgumentVariable(this, result);
    }

    public abstract int getArgsSize();
    public abstract List<RequirementValidator<?>> getValidators();
    public abstract void invoke(CommandSender sender, String[] args);

    public Player getPlayer() {
        return player;
    }

    public Player getTarget() {
        return target;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setTarget(Player target) {
        this.target = target;
    }

    public String getArgumentName() {
        return argumentName;
    }

}