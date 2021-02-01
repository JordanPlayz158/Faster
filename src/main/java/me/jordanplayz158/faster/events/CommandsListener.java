package me.jordanplayz158.faster.events;

import me.jordanplayz158.faster.CommandHandler;
import me.jordanplayz158.faster.Faster;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandsListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        // If Prefix of Bot then continue, else do nothing
        if(event.getMessage().getContentRaw().startsWith(Faster.getInstance().getConfig().getPrefix())) {
            CommandHandler.handler(event);
        }
    }
}
