package me.jordanplayz158.faster.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import me.jordanplayz158.faster.Faster;
import me.jordanplayz158.utils.LoadJson;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

public class UnwarnCommand extends ArgumentCommand {
    public UnwarnCommand() {
        super("unwarn",
                List.of("remwarn", "delwarn"),
                "Remove a warning from a member on the server.",
                null,
                Faster.getInstance().getJDA().getRoleById(Faster.getInstance().getConfig().getRoleStaff()),
                Faster.getInstance().getConfig().getPrefix() + "unwarn <uuid> <reason>");
    }

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        // Checking preconditions
        if(noReason(event, args)) {
            return;
        }

        StringBuilder reason = new StringBuilder();

        for(int i = 2; i < args.length; i++) {
            reason.append(args[i]).append(" ");
        }

        reason.delete(reason.length() - 1, reason.length());

        // 2. Grab the data in data/warns then save the UUID to the data/warns.json file

        JsonObject json = LoadJson.linkedTreeMap(Faster.getInstance().getWarnsFile());
        String userId = null;

        for(String key : json.keySet()) {
            if(json.getAsJsonObject(key).has(args[1])) {
                userId = key;
                break;
            }
        }

        if(userId == null) {
            event.getChannel().sendMessage(Faster.getInstance().getTemplate(event.getAuthor())
                    .setColor(Color.RED)
                    .setTitle("Unwarn Error")
                    .setDescription("There is no warn with that uuid!")
                    .build()
            ).queue();
            return;
        }

        Member unwarnMember = Objects.requireNonNull(event.getGuild().getMemberById(userId));
        json.getAsJsonObject(userId).remove(args[1]);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            Writer writer = Files.newBufferedWriter(Faster.getInstance().getWarnsFile().toPath());

            gson.toJson(json, writer);

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 3. Send warn message
        event.getChannel().sendMessage(Faster.getInstance().getTemplate(event.getAuthor())
                .setColor(Color.GREEN)
                .setTitle("Unwarn Successful")
                .setDescription(unwarnMember.getAsMention() + " has been unwarned (The warn has been deleted)")
                .addField("Deleted ID", args[1], false)
                .build()
        ).queue();

        // 4. Send warn log message
        logAction(event, unwarnMember.getUser(), "unwarned", reason.toString());
    }
}
