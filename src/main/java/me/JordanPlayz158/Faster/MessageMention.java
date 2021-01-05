package me.JordanPlayz158.Faster;

import net.dv8tion.jda.api.entities.User;

public class MessageMention {
    public static User extractMention(String user) {
        try {
            Integer.parseInt(user.substring(0, 1));
            return User.fromId(user);
        } catch (NumberFormatException e) {
            return User.fromId(user.substring(3, user.length() - 1));
        }
    }
}
