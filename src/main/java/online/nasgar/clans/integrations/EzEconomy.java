package online.nasgar.clans.integrations;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EzEconomy {

    private static Economy econ = null;

    public static boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static double getBalance(Player p) {
        return econ.getBalance(p);
    }

    public static void withdraw(Player p, double amt) {
        econ.withdrawPlayer(p, amt);
    }

    public static void pay(Player p, double amt) {
        econ.depositPlayer(p, amt);
    }

}
