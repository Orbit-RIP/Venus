package net.frozenorb.foxtrot.util;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

@Getter
@Setter
public class Config extends YamlConfiguration {

    private File file;
    private String name, directory;

    public Config(JavaPlugin plugin, String name, String directory){
        setName(name);
        setDirectory(directory);
        file = new File(directory, name + ".yml");
        if (!file.exists()) {
            plugin.saveResource(name + ".yml", false);
        }
        load();
        save();
    }

    public void load() {
        /* Load the files configuration */
        try {
            this.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the configuration file from memory to storage
     */
    public void save() {
        try {
            this.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Configuration getConfiguration() {
        return this;
    }

    public void reloadConfiguration(){
        YamlConfiguration.loadConfiguration(file);
    }
}