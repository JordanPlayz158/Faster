package me.JordanPlayz158.Faster.commands;

import me.JordanPlayz158.Faster.Command;
import me.JordanPlayz158.Utils.loadJson;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class KickCommand extends Command {
    public KickCommand() {
        super("kick", "Kicks a member from the guild!", Permission.KICK_MEMBERS, loadJson.value("config.json", "prefix") + "kick <user> <reason>\nEx. " + loadJson.value("config.json", "prefix") + "kick @ExampleUser Take a break.");
    }

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        event.getGuild().kick(event.getMessage().getMentionedMembers().get(0)).reason(args[2]).queue(success -> {
            PrivateChannel dm = event.getMessage().getMentionedMembers().get(0).getUser().openPrivateChannel().complete();
        }, error -> {
            
        });
    }
}
