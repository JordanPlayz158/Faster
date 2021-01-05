package me.JordanPlayz158.Faster;

import me.JordanPlayz158.Faster.commands.BanCommand;
import me.JordanPlayz158.Faster.commands.KickCommand;
import me.JordanPlayz158.Faster.commands.UnbanCommand;
import me.JordanPlayz158.Faster.events.MemberJoin;
import me.JordanPlayz158.Faster.events.MessageReceived;
import me.JordanPlayz158.Utils.loadJson;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static me.JordanPlayz158.Utils.copyFile.copyFile;
import static me.JordanPlayz158.Utils.initiateLog.initiateLog;

public class Faster {
    private static final String config = "config.json";
    private static String roleOnJoin = loadJson.value(config, "roleOnJoin");
    private static String prefix = loadJson.value(config, "prefix");
    private static final Map<String, Command> commands = new HashMap<>() {{
       put("ban", new BanCommand());
       put("unban", new UnbanCommand());
       put("kick", new KickCommand());
    }};

    public static void main(String[] args) throws LoginException, IOException, InterruptedException {
        // Initiates the log
        initiateLog();

        //Copy config
        copyFile(config, config);

        String token = loadJson.value(config, "token");
        String activity = loadJson.value(config, "activity");
        String activityType = loadJson.value(config, "activityType");


        // Checks if the Token is 1 character or less and if so, tell the person they need to provide a token
        if (token.length() <= 1) {
            System.out.println("You have to provide a token in your config file!");
            System.exit(1);
        }

        // Token and activity is read from and set in the config.json
        JDABuilder jdaBuilder = JDABuilder.createLight(token);

        if(!roleOnJoin.isEmpty()) {
            jdaBuilder.enableIntents(GatewayIntent.GUILD_MEMBERS);
            jdaBuilder.addEventListeners(new MemberJoin());
        }

        if(!prefix.isEmpty()) {
            jdaBuilder.enableIntents(GatewayIntent.GUILD_MESSAGES);
            jdaBuilder.addEventListeners(new MessageReceived());
        }

        jdaBuilder
                .setActivity(Activity.of(Activity.ActivityType.valueOf(activityType.toUpperCase()), activity))
                .build()
                .awaitReady();
    }

    public static String getRoleOnJoin() {
        return roleOnJoin;
    }

    public static Map<String, Command> getCommands() {
        return commands;
    }
}
