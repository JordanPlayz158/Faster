package me.jordanplayz158.faster.commands;

import me.jordanplayz158.faster.Faster;
import me.jordanplayz158.utils.MessageUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MuteCommand extends ModerationCommand {
    public MuteCommand() {
        super("mute",
                new ArrayList<>(),
                "Mute a member of the discord (default 30 minute mute)",
                null,
                Faster.getInstance().getJDA().getRoleById(Faster.getInstance().getConfig().getRoleStaff()),
                Faster.getInstance().getConfig().getPrefix() + "mute @Member [time]\nEx. " + Faster.getInstance().getConfig().getPrefix() + "mute @BadMember 1h");
    }

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        Member muteMember = Objects.requireNonNull(event.getGuild().getMember(MessageUtils.extractMention(args[1])));
        Role muteRole = Objects.requireNonNull(Faster.getInstance().getJDA().getRoleById(Faster.getInstance().getConfig().getRoleMute()));
        String delay = "30m";

        if(args.length > 2)
            delay = args[2];

        String finalDelay = delay;
        event.getGuild().addRoleToMember(muteMember, muteRole).queue(success -> event.getChannel().sendMessage(Faster.getInstance().getTemplate(event.getAuthor())
                .setColor(Color.GREEN)
                .setTitle("Mute Successful")
                .setDescription(MessageUtils.nameAndTag(muteMember.getUser()) + " has been muted for " + finalDelay)
                .build()).queue(success2 -> logAction(event, muteMember.getUser(), "muted", null)));

        Executors.newScheduledThreadPool(1).schedule(() -> event.getGuild().removeRoleFromMember(muteMember, muteRole).queue(),
                Long.parseLong(delay.substring(0, delay.length() - 1)),
                getTimeUnit(delay));

    }

    public TimeUnit getTimeUnit(String time) {
        switch(time.substring(time.length() - 1).toLowerCase()) {
            case "s":
                return TimeUnit.SECONDS;
            case "m":
                return TimeUnit.MINUTES;
            case "h":
                return TimeUnit.HOURS;
            case "d":
                return TimeUnit.DAYS;
            default:
                return null;
        }
    }
}