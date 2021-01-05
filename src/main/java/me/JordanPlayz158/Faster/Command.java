package me.JordanPlayz158.Faster;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public abstract class Command {
    protected String name;
    protected String description;
    protected Permission permission;
    protected String syntax;

    public Command(@NotNull String name, @NotNull String description, Permission permission, @NotNull String syntax) {
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.syntax = syntax;
    }

    public abstract void onCommand(MessageReceivedEvent event, String[] args);

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Permission getPermission() {
        return permission;
    }

    public String getSyntax() {
        return syntax;
    }
}