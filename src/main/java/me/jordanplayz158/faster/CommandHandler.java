package me.jordanplayz158.faster;

import me.jordanplayz158.faster.commands.Command;
import me.jordanplayz158.utils.MessageUtils;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.MarkdownUtil;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CommandHandler {
    public static void handler(MessageReceivedEvent event) {
        // Removes the prefix (substring) then splits the message into arguments by spaces
        String[] args = event.getMessage().getContentRaw().substring(Faster.getInstance().getConfig().getPrefix().length()).split(" ");
        String command = args[0];

        Command realCommand = null;

        for(Map.Entry<List<String>, Command> c : Faster.getInstance().getCommandsList().entrySet()) {
            for(String alias : c.getKey()) {
                if(alias.equals(command)) {
                    realCommand = c.getValue();
                    break;
                }
            }
        }

        // Is the command empty or does the command exist?
        if (command.isEmpty() || realCommand == null)
            return;

        MessageChannel channel = event.getChannel();

        if ((realCommand.getPermission() != null && !Objects.requireNonNull(event.getMember()).hasPermission(realCommand.getPermission())) || (realCommand.getRole() != null && !Objects.requireNonNull(event.getMember()).getRoles().contains(realCommand.getRole()))) {
            channel.sendMessage(Faster.getInstance().getTemplate(event.getAuthor()).setColor(Color.RED).setTitle("Access Denied").setDescription("You do not have access to that command!").build()).queue();
            return;
        }

        boolean isArgumentCommand = realCommand.getClass().getSuperclass().getSimpleName().equals("ArgumentCommand");

        if (isArgumentCommand && args.length == 1) {
            StringBuilder aliases = new StringBuilder();

            if (realCommand.getAliases().size() > 1) {
                aliases.append("\n\n").append(MarkdownUtil.bold("Known Aliases")).append("\n");

                for (String alias : realCommand.getAliases()) {
                    aliases.append(Faster.getInstance().getConfig().getPrefix()).append(alias).append("\n");
                }
            }

            channel.sendMessage(Faster.getInstance().getTemplate(event.getAuthor()).setColor(Color.YELLOW)
                    .setTitle(realCommand.getName())
                    .setDescription(realCommand.getDescription() + aliases)
                    .addField("Syntax", realCommand.getSyntax(), true).build()).queue();
            return;
        }

        boolean isModerationCommand = realCommand.getClass().getSuperclass().getSimpleName().equals("ModerationCommand");

        if(isModerationCommand) {
            Guild guild = event.getGuild();
            User mentionedUser = MessageUtils.extractMention(args[1]);

            if (guild.isMember(mentionedUser)) {
                if (!hierarchyCheck(guild.getRoles(), Objects.requireNonNull(event.getMember()).getRoles(), Objects.requireNonNull(guild.getMember(mentionedUser)).getRoles(), channel)) {
                    channel.sendMessage(Faster.getInstance().getTemplate(event.getAuthor()).setColor(Color.RED).setTitle("Hierarchy Check").setDescription("You can't execute this command as you are lower or the same on the hierarchy than the person you are using this on.").build()).queue();
                    return;
                }
            }
        }

        realCommand.onCommand(event, args);
    }

    private static boolean hierarchyCheck(List<Role> guildRoles, List<Role> memberRoles, List<Role> mentionRoles, MessageChannel channel) {
        /*channel.sendMessage("Member's Highest Role: "
                + guildRoles.indexOf(memberRoles.get(0))
                + "\nMention's Highest Role: "
                + guildRoles.indexOf(mentionRoles.get(0))
                + "\nIs the member's highest role higher than the mentioned member's highest role? "
                + (guildRoles.indexOf(memberRoles.get(0)) < guildRoles.indexOf(mentionRoles.get(0)))).queue();*/

        if(memberRoles.size() < 1) {
            return false;
        }

        if(mentionRoles.size() < 1) {
            return true;
        }

        return guildRoles.indexOf(memberRoles.get(0)) < guildRoles.indexOf(mentionRoles.get(0));
    }
}
