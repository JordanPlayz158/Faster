package me.jordanplayz158.faster.commands;

import me.jordanplayz158.faster.Faster;
import me.jordanplayz158.utils.MessageUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.MarkdownUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help",
                new ArrayList<>(),
                "This is the help command that tells you all the commands",
                null,
                null,
                Faster.getInstance().getConfig().getPrefix() + "help");
    }

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        EmbedBuilder embed = Faster.getInstance().getTemplate(event.getAuthor())
                .setColor(Color.YELLOW)
                .setTitle("Help");

        Member executor = Objects.requireNonNull(event.getMember());

        for(Map.Entry<List<String>, Command> c : Faster.getInstance().getCommandsList().entrySet()) {
            Command command = c.getValue();

            if((executor.hasPermission(command.getPermission()) || command.getPermission() == null) && (executor.getRoles().contains(command.getRole()) || command.getRole() == null)) {
                embed.appendDescription(MarkdownUtil.bold(MessageUtils.upperCaseFirstLetter(command.getName())));
                embed.appendDescription(" - ");
                embed.appendDescription(command.getDescription());
                embed.appendDescription("\n");
                embed.appendDescription("Syntax: `");
                embed.appendDescription(command.getSyntax());
                embed.appendDescription("`\n\n");
            }
        }

        event.getChannel().sendMessage(embed.build()).queue();
    }
}
