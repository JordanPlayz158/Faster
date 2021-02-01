package me.jordanplayz158.faster.events;

import me.jordanplayz158.faster.Faster;
import me.jordanplayz158.faster.commands.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ReadyListener extends ListenerAdapter {
    public void onReady(@NotNull ReadyEvent event) {
        Faster.getInstance().setCommandsList(new HashMap<>() {{
            put(new BanCommand().getAliases(), new BanCommand());
            put(new KickCommand().getAliases(), new KickCommand());
            put(new MuteCommand().getAliases(), new MuteCommand());
            put(new UnbanCommand().getAliases(), new UnbanCommand());
            put(new UnwarnCommand().getAliases(), new UnwarnCommand());
            put(new WarnCommand().getAliases(), new WarnCommand());
        }});

        /*Scanner in = new Scanner(System.in);
        String command = in.nextLine();

        while(command != "exit") {
            command = in.nextLine();
        }

        System.exit(0);*/
    }
}