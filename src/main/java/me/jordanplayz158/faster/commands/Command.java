package me.jordanplayz158.faster.commands;

import me.jordanplayz158.faster.Faster;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Command {
    protected String name;
    protected final List<String> aliases = new ArrayList<>();
    protected String description;
    protected Permission permission;
    protected final Role role;
    protected String syntax;

    public Command(@NotNull String name, List<String> aliases, @NotNull String description, Permission permission, Role role, @NotNull String syntax) {
        this.name = name;

        this.aliases.add(name);

        if(aliases != null)
        this.aliases.addAll(aliases);

        this.description = description;
        this.permission = permission;
        this.role = role;
        this.syntax = syntax;
    }

    public abstract void onCommand(MessageReceivedEvent event, String[] args);

    public String getName() {
        return name;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public String getDescription() {
        return description;
    }

    public Permission getPermission() {
        return permission;
    }

    public Role getRole() {
        return role;
    }

    public String getSyntax() {
        return syntax;
    }

    public boolean noReason(MessageReceivedEvent event, String[] args) {
        if (args.length < 3) {
            event.getChannel().sendMessage(Faster.getInstance().getTemplate(event.getAuthor())
                    .setColor(Color.RED)
                    .setTitle("Invalid Reason!")
                    .setDescription("Reason cannot be null")
                    .build()).queue();

            return true;
        }

        return false;
    }
}