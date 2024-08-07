package io.github.rainvaporeon.customenchantments.util.io;

import io.github.rainvaporeon.customenchantments.util.SharedConstants;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.sql.Struct;

public class LocalConfig {
    private static LocalConfig theInstance;
    private final Plugin instance;
    private FileConfiguration config;

    public LocalConfig(Plugin plugin) {
        this.instance = plugin;
        this.config = plugin.getConfig();
    }

    public void write(String entry, Object value) {
        this.config.addDefault(entry, value);
        this.save();
        this.reload(false);
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
        this.instance.saveConfig();
    }

    public void reload(boolean hard) {
        if (hard) this.instance.reloadConfig();
        this.config = instance.getConfig();
    }

    private void writeToDisk() {
        this.instance.saveConfig();
    }

    public static void init(Plugin plugin) {
        theInstance = new LocalConfig(plugin);
        writeIfAbsent();
    }

    private static void writeIfAbsent() {
        doWrite(SharedConstants.STRICT_INFUSION, true, false);
        theInstance.save();
        theInstance.reload(false);
    }

    private static void doWrite(String key, Object value, boolean overwrite) {
        if (theInstance.fileConfiguration().contains(key) && !overwrite) return;
        theInstance.write(key, value);
    }

    public static LocalConfig instance() {
        return theInstance;
    }
}
