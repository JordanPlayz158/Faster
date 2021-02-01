package me.jordanplayz158.faster.commands;

import me.jordanplayz158.faster.Faster;
import me.jordanplayz158.utils.MessageUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.MarkdownUtil;

import java.awt.*;
import java.util.ArrayList;

public class BanCommand extends ModerationCommand {
    public BanCommand() {
        super("ban",
                new ArrayList<>(),
                "Ban a member from the guild!",
                null,
                Faster.getInstance().getJDA().getRoleById(Faster.getInstance().getConfig().getRoleStaff()),
                Faster.getInstance().getConfig().getPrefix() + "ban <user> <reason>");
    }

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        if (noReason(event, args))
            return;

        TextChannel channel = event.getTextChannel();
        MessageEmbed invalidUser = Faster.getInstance().getTemplate(event.getAuthor())
                .setColor(Color.RED)
                .setTitle("Ban Failed")
                .setDescription("The user does not exist")
                .build();

        if(event.getMessage().getMentionedMembers().size() < 1) {
            channel.sendMessage(invalidUser).queue();
            return;
        }

        Member member = event.getMessage().getMentionedMembers().get(0);
        StringBuilder reason = new StringBuilder();

        for (int i = 2; i < args.length; i++) {
            reason.append(args[i]).append(" ");
        }

        try {
            event.getGuild().ban(member, 0).reason(reason.toString()).queue();
        } catch (Exception e) {
            channel.sendMessage(invalidUser).queue();
            return;
        }

        channel.sendMessage(Faster.getInstance().getTemplate(event.getAuthor())
                .setColor(Color.GREEN)
                .setTitle("Ban Successful")
                .setDescription(MarkdownUtil.bold(MessageUtils.nameAndTag(member.getUser())) + " was banned")
                .build()).queue();

        logAction(event, member.getUser(), "banned", reason.toString());
    }
}