package me.jordanplayz158.faster.json;

import com.google.gson.JsonObject;
import me.jordanplayz158.faster.Faster;
import me.jordanplayz158.utils.LoadJson;
import net.dv8tion.jda.api.entities.Activity;

import java.io.File;

public class Config {
    private JsonObject json;

    public Config() {
        json = LoadJson.linkedTreeMap(Faster.getInstance().getConfigFile());
    }

    public Config(File json) {
        this.json = LoadJson.linkedTreeMap(json);
    }

    public JsonObject getJson() {
        return json;
    }

    public String getPrefix() {
        return json.get("prefix").getAsString();
    }

    public JsonObject getActivity() {
        return json.getAsJsonObject("activity");
    }

    public String getActivityName() {
        return getActivity().get("name").getAsString();
    }

    public Activity.ActivityType getActivityType() {
        return Activity.ActivityType.valueOf(getActivity().get("type").getAsString().toUpperCase());
    }

    public JsonObject getChannels() {
        return json.getAsJsonObject("channels");
    }

    public long getChannelLog() {
        return getChannels().get("log").getAsLong();
    }

    public JsonObject getRoles() {
        return json.getAsJsonObject("roles");
    }

    public long getRoleOnJoin() {
        return getRoles().get("onJoin").getAsLong();
    }

    public long getRoleStaff() {
        return getRoles().get("staff").getAsLong();
    }

    public long getRoleMute() {
        return getRoles().get("mute").getAsLong();
    }
}
