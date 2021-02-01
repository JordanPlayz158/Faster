package me.jordanplayz158.faster.commands;

import me.jordanplayz158.faster.Faster;
import me.jordanplayz158.utils.MessageUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.MarkdownUtil;

import java.awt.*;

public class KickCommand extends ModerationCommand {
    public KickCommand() {
        super("kick",
                null,
                "Kicks a member from the guild!",
                null,
                Faster.getInstance().getJDA().getRoleById(Faster.getInstance().getConfig().getRoleStaff()),
                Faster.getInstance().getConfig().getPrefix() + "kick <user> <reason>");
    }

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        if (noReason(event, args))
            return;

        Member kickedMember = event.getMessage().getMentionedMembers().get(0);
        StringBuilder reason = new StringBuilder();

        for (int i = 2; i < args.length; i++) {
            reason.append(args[i]).append(" ");
        }

        event.getGuild().kick(kickedMember).reason(reason.toString()).queue(success -> {
            event.getChannel().sendMessage(Faster.getInstance().getTemplate(event.getAuthor())
                    .setColor(Color.GREEN)
                    .setTitle("Kick Successful")
                    .setDescription(MarkdownUtil.bold(MessageUtils.nameAndTag(kickedMember.getUser()))
                            + " was kicked")
                    .build()).queue();

            logAction(event, kickedMember.getUser(), "kicked", reason.toString());
        }, error -> event.getChannel().sendMessage(Faster.getInstance().getTemplate(event.getAuthor())
                .setColor(Color.RED)
                .setTitle("Kick Failed")
                .setDescription("The kick didn't execute correctly, Please check console for details!")
                .build()).queue());
    }
}