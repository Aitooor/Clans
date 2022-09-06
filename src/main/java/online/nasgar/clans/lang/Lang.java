package online.nasgar.clans.lang;

import online.nasgar.clans.Clans;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Lang {

    public static CustomConfig langFile;
    public static Map<String, String> options = new HashMap<String, String>();

    public static void initialize(Clans plugin) {
        options = new HashMap<String, String>();
        langFile = new CustomConfig(plugin, "lang.yml");
        FileConfiguration fc = langFile.getCustomConfig(true);
        Set<String> keys = fc.getKeys(false);
        for (String key : keys) {
            options.put(key, fc.getString(key));
        }
    }

    public static String getLang(String key) {
        String option = options.get(key);
        if (option != null) {
            return ChatColor.translateAlternateColorCodes('&', option);
        }
        return "";
    }

    public static String colorString(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static boolean checkHex(String string) {
        return string.matches("^#[\\da-fA-F]{6}$");
    }
}
