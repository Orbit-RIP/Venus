package rip.orbit.hcteams.util;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ConfigFile extends YamlConfiguration {

    @Getter
    @Setter
    private File file;
    @Getter
    @Setter
    private String name, directory;
    @Getter
    @Setter
    private YamlConfiguration configuration;

    public ConfigFile(JavaPlugin plugin, String name, String directory) {
        setName(name);
        setDirectory(directory);
        file = new File(directory, name + ".yml");

        if (!file.exists())
            plugin.saveResource(name + ".yml", false);

        this.configuration = YamlConfiguration.loadConfiguration(this.getFile());
    }

    public void save() {
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
