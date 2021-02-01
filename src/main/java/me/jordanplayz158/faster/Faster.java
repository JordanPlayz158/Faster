package me.jordanplayz158.faster;

import me.jordanplayz158.faster.commands.Command;
import me.jordanplayz158.faster.events.CommandsListener;
import me.jordanplayz158.faster.events.MemberJoinListener;
import me.jordanplayz158.faster.events.ReadyListener;
import me.jordanplayz158.faster.json.Config;
import me.jordanplayz158.utils.FileUtils;
import me.jordanplayz158.utils.Initiate;
import me.jordanplayz158.utils.MessageUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import org.apache.log4j.Logger;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Faster {
    private Config config;

    // Files and Folders
    private final File configFile = new File("config.json");

    private final File dataFolder = new File("data");
    private final File warnsFile = new File(dataFolder, "warns.json");

    private Map<List<String>, Command> commandsList;

    private static final Faster instance = new Faster();
    private Logger logger;
    private JDA jda;

    public static void main(String[] args) throws LoginException, IOException {
        // Initiates the log
        instance.logger = Initiate.log();

        //Copy config
        FileUtils.copyFile(instance.configFile);
        instance.config = new Config();

        if(!instance.dataFolder.exists()) {
            if(instance.dataFolder.mkdir()) {
                FileUtils.copyFile(instance.warnsFile);
            }
        }

        final String token = instance.config.getJson().get("token").getAsString();
        // Checks if the Token is 1 character or less and if so, tell the person they need to provide a token
        if(token.length() <= 1) {
            instance.logger.fatal("You have to provide a token in your config file!");
            System.exit(1);
        }

        // Token and activity is read from and set in the config.json
        JDABuilder jdaBuilder = JDABuilder.createLight(token);

        if(instance.config.getRoleOnJoin() != 0) {
            jdaBuilder.enableIntents(GatewayIntent.GUILD_MEMBERS);
            jdaBuilder.addEventListeners(new MemberJoinListener());
        }

        if(!instance.config.getPrefix().isEmpty()) {
            jdaBuilder.enableIntents(GatewayIntent.GUILD_MESSAGES);
            jdaBuilder.addEventListeners(new CommandsListener());
        }

        jdaBuilder.addEventListeners(new ReadyListener());

        instance.jda = jdaBuilder
                .setActivity(Activity.of(instance.config.getActivityType(), instance.config.getActivityName()))
                .setChunkingFilter(ChunkingFilter.ALL)
                .build();

        /*Scanner in = new Scanner(System.in);
        String command = in.nextLine();

        while (command != "exit") {
            command = in.nextLine();
        }

        System.exit(0);*/
    }

    public Config getConfig() {
        return config;
    }

    public File getConfigFile() {
        return configFile;
    }

    public File getDataFolder() {
        return dataFolder;
    }

    public File getWarnsFile() {
        return warnsFile;
    }

    public Map<List<String>, Command> getCommandsList() {
        return commandsList;
    }

    public void setCommandsList(Map<List<String>, Command> commandsList) {
        this.commandsList = commandsList;
    }

    public static Faster getInstance() {
        return instance;
    }

    public Logger getLogger() {
        return logger;
    }

    public JDA getJDA() {
        return jda;
    }

    public EmbedBuilder getTemplate(User author) {
        return new EmbedBuilder()
                .setFooter("Faster | " + MessageUtils.nameAndTag(author));
    }
}
