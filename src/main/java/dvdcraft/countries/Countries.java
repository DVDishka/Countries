package dvdcraft.countries;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dvdcraft.countries.common.Classes.Owner;
import dvdcraft.countries.common.CommonVariables;
import dvdcraft.countries.common.Classes.Country;
import dvdcraft.countries.common.FileDir;
import dvdcraft.countries.executorsHandlers.CommandExecutor;
import dvdcraft.countries.executorsHandlers.CountryTabCompleter;
import dvdcraft.countries.Threads.TerritoryCheckProcess;
import dvdcraft.countries.executorsHandlers.EventHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;

public final class Countries extends JavaPlugin {

    @Override
    public void onEnable() {
        if (!FileDir.fileDirExist("plugins", "Countries")){
            if (FileDir.createDirectory("plugins", "Countries")) {
                CommonVariables.logger.info("Countries directory was created");
            } else {
                CommonVariables.logger.warning("Countries directory was not created!");
            }
        }
        if (!FileDir.fileDirExist("plugins/Countries", "countries.json")) {
            if (FileDir.createFile("plugins/Countries", "countries.json")) {
                CommonVariables.logger.info("countries.json file was created");
            } else {
                CommonVariables.logger.warning("Something went wrong while trying to create countries.json file!");
            }
        }
        if (!FileDir.fileDirExist("plugins/Countries", "owners.json")) {
            if (FileDir.createFile("plugins/Countries", "owners.json")) {
                CommonVariables.logger.info("owners.json file was created");
            } else {
                CommonVariables.logger.warning("Something went wrong while trying to create owners.json file!");
            }
        }
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            File file = new File("plugins/Countries/countries.json");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String json = "";
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                json = json.concat(line);
            }
            bufferedReader.close();
            fileReader.close();
            Type countryHashSetType = new TypeToken<HashSet<Country>>(){}.getType();
            if (gson.fromJson(json, countryHashSetType) != null) {
                CommonVariables.countries = gson.fromJson(json, countryHashSetType);
            }
        } catch (Exception e) {
            CommonVariables.logger.warning("Can not read countries.json file!");
        }
        for (Country country : CommonVariables.countries) {
            CommonVariables.addTeam(country.getName());
        }

        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            File file = new File("plugins/Countries/owners.json");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String json = "";
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                json = json.concat(line);
            }
            bufferedReader.close();
            fileReader.close();
            Type ownersHashMapType = new TypeToken<HashMap<String, Owner>>(){}.getType();
            if (gson.fromJson(json, ownersHashMapType) != null) {
                CommonVariables.owners = gson.fromJson(json, ownersHashMapType);
            }
        } catch (Exception e) {
            CommonVariables.logger.warning("Can not read countries.json file!");
        }

        new TerritoryCheckProcess("Check").start();
        CommandExecutor commandExecutor = new CommandExecutor();
        TabCompleter TabCompleter = new CountryTabCompleter();
        Bukkit.getPluginManager().registerEvents(new EventHandler(), this);

        PluginCommand countryCommand = getCommand("country");

        countryCommand.setExecutor(commandExecutor);
        countryCommand.setTabCompleter(TabCompleter);

        CommonVariables.logger.info("Plugin has been enabled");
    }

    @Override
    public void onDisable() {
        try {
            File file = new File("plugins/Countries/countries.json");
            FileWriter fileWriter = new FileWriter(file);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            fileWriter.write(gson.toJson(CommonVariables.countries));
            fileWriter.close();
        } catch (Exception e) {
            CommonVariables.logger.warning("Can not write countries.json");
        }
        try {
            File file = new File("plugins/Countries/owners.json");
            FileWriter fileWriter = new FileWriter(file);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            fileWriter.write(gson.toJson(CommonVariables.owners));
            fileWriter.close();
        } catch (Exception e) {
            CommonVariables.logger.warning("Can not write owners.json");
        }
        CommonVariables.logger.info("Plugin has been disabled");
    }
}
