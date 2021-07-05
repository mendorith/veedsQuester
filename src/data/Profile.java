package data;

import com.epicbot.api.shared.APIContext;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class Profile {
    APIContext ctx;
    GsonBuilder builder;
    Gson gson;

    public Profile(APIContext ctx) {
        this.ctx = ctx;
        this.builder = new GsonBuilder();
        builder.setPrettyPrinting();
        gson = builder.create();
    }

    public void saveProfile(String fileName, QuestsInfo quest) {
        Gson gson = new Gson();

        Settings settings = createSettingsObject(quest);

        // Java objects to String
        // String json = gson.toJson(staff);

        File directory = ctx.script().getSettingsDirectory();
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Java objects to File
        try (FileWriter writer = new FileWriter(directory + "\\" + fileName + ".json")) {
            gson.toJson(settings, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadProfile() {

        Gson gson = new Gson();

        try (Reader reader = new FileReader(ctx.script().getScriptProfile().get().getAbsolutePath())) {

            // Convert JSON File to Java Object
            Settings settings = gson.fromJson(reader, Settings.class);

            // print staff object
            System.out.println(settings);

            settings.applySettings();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static Settings createSettingsObject(QuestsInfo quest) {

        Settings settings = new Settings();

        settings.setQuest(quest);

        return settings;

    }
}

class Settings {
    private QuestsInfo quest;

    public void applySettings() {
        Vars.currentQuest = quest;
        Vars.start = true;
    }

    public QuestsInfo getQuest() {
        return quest;
    }

    public void setQuest(QuestsInfo quest) {
        this.quest = quest;
    }
}