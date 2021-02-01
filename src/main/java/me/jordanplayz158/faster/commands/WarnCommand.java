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
import java.util.ArrayList;
import java.util.UUID;

public class WarnCommand extends ModerationCommand {
    public WarnCommand() {
        super("warn",
                new ArrayList<>(),
                "Warn a member on the server.",
                null,
                Faster.getInstance().getJDA().getRoleById(Faster.getInstance().getConfig().getRoleStaff()),
                Faster.getInstance().getConfig().getPrefix() + "warn @Member Breaking the rules!");
    }

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        // Checking preconditions
        if(event.getMessage().getMentionedMembers().size() > 1) {
            event.getChannel().sendMessage(Faster.getInstance().getTemplate(event.getAuthor())
                    .setColor(Color.RED)
                    .setTitle("Warn Error")
                    .setDescription("There is no mentioned member in the warn")
                    .build()
            ).queue();
            return;
        }

        if(noReason(event, args)) {
            return;
        }

        Member warnMember = event.getMessage().getMentionedMembers().get(0);

        // 1. Make a UUID for the warn
        UUID uuid = UUID.randomUUID();

        StringBuilder reason = new StringBuilder();

        for(int i = 2; i < args.length; i++) {
            reason.append(args[i]).append(" ");
        }

        reason.delete(reason.length() - 1, reason.length());

        // 2. Grab the data in data/warns then save the UUID to the data/warns.json file
        JsonObject json = LoadJson.linkedTreeMap(Faster.getInstance().getWarnsFile());

        if(!json.has(warnMember.getId())) {
            json.add(warnMember.getId(), new JsonObject());
        }

        JsonObject userWarns = json.getAsJsonObject(warnMember.getId());
        userWarns.addProperty(uuid.toString(), reason.toString());

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
                .setTitle("Warn Successful")
                .setDescription(warnMember.getAsMention() + " has been warned")
                .addField("ID", String.valueOf(uuid), false)
                .build()
        ).queue();

        // 4. Send warn log message
        logAction(event, warnMember.getUser(), "warned", reason.toString());
    }
}
