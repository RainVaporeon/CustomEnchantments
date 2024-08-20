package io.github.rainvaporeon.customenchantments.util.io;

import io.github.rainvaporeon.customenchantments.CustomEnchantments;
import io.github.rainvaporeon.customenchantments.util.SharedConstants;
import io.github.rainvaporeon.customenchantments.util.server.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;

public class LocalConfig {
    public static final String STRICT_INFUSIONS = "strict";
    public static final String DEBUG_LOGGING = "debug_logging";

    private static LocalConfig theInstance;
    private final Plugin instance;
    private volatile FileConfiguration config;

    public LocalConfig(Plugin plugin) {
        this.instance = plugin;
        this.config = plugin.getConfig();
    }

    public void write(String entry, Object value) {
        write0(entry, value);
        this.save();
        this.reload(false);
    }

    private void write0(String entry, Object value) {
        this.config.addDefault(entry, value);
    }

    public <T> T read(String entry, T def, Class<T> type) {
        return (T) this.config.get(entry, def);
    }

    public <T> T read(String entry, Class<T> type) {
        return read(entry, null, type);
    }

    public int readInt(String entry, int def) {
        return this.config.getInt(entry, def);
    }

    public int readInt(String entry) {
        return this.config.getInt(entry);
    }

    public boolean readBoolean(String entry, boolean def) {
        return this.config.getBoolean(entry, def);
    }

    public boolean readBoolean(String entry) {
        return this.config.getBoolean(entry);
    }

    public FileConfiguration fileConfiguration() {
        return this.config;
    }

    public void save() {
        this.writeToDisk();
    }

    public void reload(boolean hard) {
        if (hard) this.instance.reloadConfig();
        this.config = instance.getConfig();
    }

    private void writeToDisk() {
        try {
            this.config.save(new File(this.instance.getDataFolder(), "config.yml"));
        } catch (IOException ex) {
            Server.log(Level.WARNING, "Failed to save to disk", ex);
        }
    }

    public static void init(Plugin plugin) {
        theInstance = new LocalConfig(plugin);
        writeIfAbsent();
    }

    private static void writeIfAbsent() {
        doWrite(STRICT_INFUSIONS, true, false);
        doWrite(DEBUG_LOGGING, false, false);
        writeToDisk0(theInstance);
    }

    private static void doWrite(String key, Object value, boolean overwrite) {
        if (theInstance.fileConfiguration().contains(key) && !overwrite) return;
        theInstance.write0(key, value);
    }

    private static void writeToDisk0(LocalConfig cfg) {
        Plugin p = cfg.instance;
        FileConfiguration config = cfg.config;
        String data = config.saveToString();
        try (FileWriter w = new FileWriter(new File(p.getDataFolder(), "config.yml"))) {
            w.write(data);
        } catch (IOException ex) {
            Server.log(Level.WARNING, "Failed to save to disk", ex);
        }
    }

    public static LocalConfig instance() {
        return theInstance;
    }
}
