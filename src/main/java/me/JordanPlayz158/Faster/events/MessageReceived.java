package me.JordanPlayz158.Faster.events;

import me.JordanPlayz158.Faster.CommandHandler;
import me.JordanPlayz158.Utils.loadJson;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageReceived extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        // If Prefix of Bot then continue, else do nothing
        if(event.getMessage().getContentRaw().startsWith(loadJson.value("config.json", "prefix"))) {
            CommandHandler.handler(event);
        }
    }
}
