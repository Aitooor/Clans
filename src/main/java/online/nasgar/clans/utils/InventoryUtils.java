package online.nasgar.clans.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class InventoryUtils {
    public static boolean testSavable(ItemStack item) {
        try {
            if (item.getType() == Material.WRITTEN_BOOK || item.getType() == Material.WRITABLE_BOOK) {
                File tempFile = new File(Bukkit.getPluginManager().getPlugin("Clans").getDataFolder(), "testfile.yml");
                YamlConfiguration customConfig = new YamlConfiguration();
                customConfig.set("item", item);
                customConfig.save(tempFile);
                tempFile.delete();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
