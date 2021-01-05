package me.JordanPlayz158.Faster;

import me.JordanPlayz158.Utils.loadJson;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;
import java.util.Objects;

public class CommandHandler {
    public static void handler(MessageReceivedEvent event) {
        // Removes the prefix (substring) then splits the message into arguments by spaces
        String[] args = event.getMessage().getContentRaw().substring(loadJson.value("config.json", "prefix").length()).split(" ");
        String command = args[0];


        Command realCommand = Faster.getCommands().get(command);

        // Is the command empty or does the command exist?
        if (command.isEmpty() || realCommand == null)
            return;

        MessageChannel channel = event.getChannel();

        if (realCommand.getPermission() != null && !Objects.requireNonNull(event.getMember()).hasPermission(realCommand.getPermission())) {
            channel.sendMessage("You do not have access to that command!").queue();
            return;
        }

        if (args.length == 1) {
            EmbedBuilder embedBuilder = new EmbedBuilder()
            .setTitle(realCommand.getName())
            .appendDescription(realCommand.getDescription())
            .addField("Syntax", realCommand.getSyntax(), true);

            channel.sendMessage(embedBuilder.build()).queue();
            return;
        }

        Guild guild = event.getGuild();
        User mentionedUser = MessageMention.extractMention(args[1]);

        if(guild.isMember(mentionedUser)) {
            if (!hierarchyCheck(guild.getRoles(), Objects.requireNonNull(event.getMember()).getRoles(), Objects.requireNonNull(guild.getMember(mentionedUser)).getRoles(), channel)) {
                channel.sendMessage("You can't execute this command as you are lower or the same on the hierarchy than the person you are using this on.").queue();
                return;
            }
        }

        realCommand.onCommand(event, args);
    }

    private static boolean hierarchyCheck(List<Role> guildRoles, List<Role> memberRoles, List<Role> mentionRoles, MessageChannel channel) {
        channel.sendMessage("Member's Highest Role: "
                + guildRoles.indexOf(memberRoles.get(0))
                + "\nMention's Highest Role: "
                + guildRoles.indexOf(mentionRoles.get(0))
                + "\nIs the member's highest role higher than the mentioned member's highest role? "
                + (guildRoles.indexOf(memberRoles.get(0)) < guildRoles.indexOf(mentionRoles.get(0)))).queue();

        return guildRoles.indexOf(memberRoles.get(0)) < guildRoles.indexOf(mentionRoles.get(0));
    }
}
