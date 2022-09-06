package online.nasgar.clans;

import online.nasgar.clans.commands.ClanCommandExecutor;
import online.nasgar.clans.commands.CommandManager;
import online.nasgar.clans.core.Clan;
import online.nasgar.clans.core.ClanMember;
import online.nasgar.clans.db.DBYml;
import online.nasgar.clans.db.IDBConnector;
import online.nasgar.clans.events.ChatEvent;
import online.nasgar.clans.events.LoginEvent;
import online.nasgar.clans.events.PlayerMove;
import online.nasgar.clans.events.PvpEvent;
import online.nasgar.clans.integrations.ClanTabCompleter;
import online.nasgar.clans.integrations.EzEconomy;
import online.nasgar.clans.integrations.EzPlaceholder;
import online.nasgar.clans.lang.Lang;
import online.nasgar.clans.logs.EzLogs;
import online.nasgar.clans.logs.InventoryListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public class Clans extends JavaPlugin {
    public static IDBConnector db;
    public static Clans plugin;

    public void onEnable() {
        plugin = this;
        if (!EzEconomy.setupEconomy()) {
            Bukkit.getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new EzPlaceholder(this).register();
        }
        Lang.initialize(this);
        enableSerialization();
        registerCommands();
        loadConfigs();
        loadDB();
        CommandManager.initialize(this);
        EzLogs.initialize(this);
        registerEvents();
    }

    public void onDisable() {
        EzLogs.flush();
        db.saveClans(online.nasgar.clans.core.Clans.clans);
    }

    private void registerCommands() {
        this.getCommand("clan").setExecutor(new ClanCommandExecutor());
        this.getCommand("adminclan").setExecutor(new ClanCommandExecutor());
        this.getCommand("clan").setTabCompleter(new ClanTabCompleter());
        this.getCommand("adminclan").setTabCompleter(new ClanTabCompleter());
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerMove(), this);
        getServer().getPluginManager().registerEvents(new PvpEvent(), this);
        getServer().getPluginManager().registerEvents(new ChatEvent(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new LoginEvent(), this);
    }

    private void loadConfigs() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        online.nasgar.clans.core.Clans.loadConfig(getConfig());
    }

    private void loadDB() {
        db = new DBYml(this);
        db.loadClans();
    }

    private void enableSerialization() {
        ConfigurationSerialization.registerClass(Clan.class, "Clan");
        ConfigurationSerialization.registerClass(ClanMember.class, "Clan");
    }

    public boolean reload() {
        db.saveClans(online.nasgar.clans.core.Clans.clans);
        loadConfigs();
        loadDB();
        Lang.initialize(this);
        return true;
    }

    public void backup() {

    }
}
