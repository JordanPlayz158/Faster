package me.JordanPlayz158.Faster.commands;

import me.JordanPlayz158.Faster.Command;
import me.JordanPlayz158.Faster.MessageMention;
import me.JordanPlayz158.Utils.loadJson;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class UnbanCommand extends Command {
    public UnbanCommand() {
        super("unban", "Unban a member from the guild!", Permission.BAN_MEMBERS, loadJson.value("config.json", "prefix") + "unban <user> [reason]\nEx. " + loadJson.value("config.json", "prefix") + "unban @ExampleUser They were wrongfully banned.");
    }

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        User user = MessageMention.extractMention(args[1]);

        Guild guild = event.getGuild();
        TextChannel channel = event.getTextChannel();
        User messageAuthor = event.getAuthor();

        guild.unban(user).queue(success -> channel.sendMessage("The user with the id of " + user.getId() + " has been unbanned by " + messageAuthor.getName() + "#" + messageAuthor.getDiscriminator()).queue(), error -> {
            int errorCode = Integer.parseInt(error.getMessage().substring(0, error.getMessage().indexOf(":")));
            if(errorCode == 10026)
                channel.sendMessage("The user is not banned!").queue();
        });
    }
}