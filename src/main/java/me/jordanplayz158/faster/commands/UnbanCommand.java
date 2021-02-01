package me.jordanplayz158.faster.commands;

import me.jordanplayz158.faster.Faster;
import me.jordanplayz158.utils.MessageUtils;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;

public class UnbanCommand extends ModerationCommand {
    public UnbanCommand() {
        super("unban",
                new ArrayList<>(),
                "Unban a member from the guild!",
                null,
                Faster.getInstance().getJDA().getRoleById(Faster.getInstance().getConfig().getRoleStaff()),
                Faster.getInstance().getConfig().getPrefix() + "unban <user> [reason]\nEx. " + Faster.getInstance().getConfig().getPrefix() + "unban @ExampleUser They were wrongfully banned.");
    }

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        User user = MessageUtils.extractMention(args[1]);
        TextChannel channel = event.getTextChannel();

        StringBuilder reason = null;

        if(args.length > 2) {
            reason = new StringBuilder();

            for(int i = 2; i < args.length; i++) {
                reason.append(args[i]).append(" ");
            }
        }

        StringBuilder finalReason = reason;
        event.getGuild().unban(user).queue(success -> {
            channel.sendMessage(Faster.getInstance().getTemplate(event.getAuthor()).setColor(Color.GREEN).setTitle("Unban Successful").setDescription("The user with the id of " + user.getId() + " has been unbanned").build()).queue();

            if(finalReason == null) {
                logAction(event, null, "unbanned", null);
            } else {
                logAction(event, null, "unbanned", finalReason.toString());
            }
        }, error -> {
            int errorCode = Integer.parseInt(error.getMessage().substring(0, error.getMessage().indexOf(":")));
            if(errorCode == 10026)
                channel.sendMessage(Faster.getInstance().getTemplate(event.getAuthor()).setColor(Color.RED).setTitle("Unban Unsuccessful").setDescription("The user is not banned!").build()).queue();
        });
    }
}