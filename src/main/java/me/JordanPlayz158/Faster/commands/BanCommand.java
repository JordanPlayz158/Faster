package me.JordanPlayz158.Faster.commands;

import me.JordanPlayz158.Faster.Command;
import me.JordanPlayz158.Utils.loadJson;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

public class BanCommand extends Command {
    public BanCommand() {
        super("ban", "Ban a member from the guild!", Permission.BAN_MEMBERS, loadJson.value("config.json", "prefix") + "ban <user> [time] [reason]\nEx. " + loadJson.value("config.json", "prefix") + "ban @ExampleUser 1h They were breaking the rules.");
    }

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        Guild guild = event.getGuild();
        TextChannel channel = event.getTextChannel();
        Member member = event.getMessage().getMentionedMembers().get(0);
        User messageAuthor = event.getAuthor();

        try {
            guild.ban(member, 0).queue();
        } catch (Exception e) {
            channel.sendMessage("The user does not exist").queue();
            return;
        }

        /*int reasonStart;

        try {
            Integer.parseInt(args[3].substring(0, args[3].length() - 1));
            reasonStart = 3;
        } catch (NumberFormatException e) {
            channel.sendMessage(e.getMessage()).queue();
            reasonStart = 2;
        }*/

        try {
            PrivateChannel privateChannel = member.getUser().openPrivateChannel().complete();
            privateChannel.sendMessage("You have been banned by " + messageAuthor.getName() + " for reason ").queue();
        } catch (ErrorResponseException e) {
            channel.sendMessage("The user has been banned but a DM could not be sent to the banned member").queue();
            return;
        }

        channel.sendMessage("The user has been banned and a DM has been sent to the banned member with the reason ").queue();
    }
}
