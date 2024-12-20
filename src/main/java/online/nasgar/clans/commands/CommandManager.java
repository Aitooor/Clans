package online.nasgar.clans.commands;

import online.nasgar.clans.Clans;
import online.nasgar.clans.commands.admin.HomeAdminCommand;
import online.nasgar.clans.commands.admin.RenameAdminCommand;
import online.nasgar.clans.commands.admin.SettagAdminCommand;
import online.nasgar.clans.commands.admin.StorageAdminCommand;
import online.nasgar.clans.commands.player.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager {

    private static Clans plugin = null;
    private static final Map<String, Map<String, Class<? extends ACommand>>> commands = new HashMap<String, Map<String, Class<? extends ACommand>>>();

    public static void initialize(Clans p) {
        plugin = p;
        Map<String, Class<? extends ACommand>> pcommands = new HashMap<String, Class<? extends ACommand>>();
        Map<String, Class<? extends ACommand>> acommands = new HashMap<String, Class<? extends ACommand>>();
        pcommands.put("create", CreateCommand.class);
        pcommands.put("info", InfoCommand.class);
        pcommands.put("disband", DisbandCommand.class);
        pcommands.put("sethome", SethomeCommand.class);
        pcommands.put("home", HomeCommand.class);
        pcommands.put("settag", SettagCommand.class);
        pcommands.put("invite", InviteCommand.class);
        pcommands.put("accept", AcceptCommand.class);
        pcommands.put("rename", RenameCommand.class);
        pcommands.put("leave", LeaveCommand.class);
        pcommands.put("kick", KickCommand.class);
        pcommands.put("chat", ChatCommand.class);
        pcommands.put("pvp", PvpCommand.class);
        pcommands.put("help", HelpCommand.class);
        pcommands.put("list", ListCommand.class);
        pcommands.put("promote", SetmoderatorCommand.class);
        pcommands.put("demote", RemovemoderatorCommand.class);
        pcommands.put("setleader", SetleaderCommand.class);
        pcommands.put("storage", StorageCommand.class);
        pcommands.put("s", StorageCommand.class);
        pcommands.put("balance", BalanceCommand.class);
        pcommands.put("upgrade", UpgradeCommand.class);
        pcommands.put("online", OnlineCommand.class);
        pcommands.put("loginmessage", LoginmessageCommand.class);
        pcommands.put("color", ColorCommand.class);
        pcommands.put("player", PlayerCommand.class);
        commands.put("clan", pcommands);
        acommands.put("storage", StorageAdminCommand.class);
        acommands.put("settag", SettagAdminCommand.class);
        acommands.put("rename", RenameAdminCommand.class);
        acommands.put("home", HomeAdminCommand.class);
        commands.put("adminclan", acommands);
    }

    private static ACommand factory(String label, String cmdName, CommandSender sender, String[] args) {
        Map<String, Class<? extends ACommand>> mapSet = commands.get(label);
        if (mapSet == null)
            return null;
        Class<? extends ACommand> cmdClass = mapSet.get(cmdName);
        if (cmdClass == null)
            return null;
        try {
            ACommand command = cmdClass.getDeclaredConstructor(CommandSender.class, String[].class, Clans.class).newInstance(sender, args, plugin);
            return command;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> getCommandList(String command) {
        if (command.equals("c")) {
            command = "clan";
        }
        List<String> result = new ArrayList<String>();
        Map<String, Class<? extends ACommand>> mapSet = commands.get(command);
        if (mapSet != null) {
            result.addAll(mapSet.keySet());
        }
        return result;
    }

    public static ACommand createCommand(String label, CommandSender sender, String[] args) throws Exception {
        if (args.length > 0) {
            String name = args[0];
            ACommand cmd = factory(label, name, sender, args);
            return cmd;
        } else {
            return null;
        }
    }

    public static boolean legalName(String name) {
        String strippedName = stripColor(name);
        if (strippedName.length() <= 28) {
            return strippedName.matches("[\\w�-��-�\"'\\-�� &]+");
        }
        return false;
    }

    public static boolean legalTag(String name) {
        String strippedName = stripColor(name);
        if (strippedName.length() <= 12) {
            return strippedName.matches("[\\w�-��-�\"'\\-�� &\u0021-\uFFFC]+");
        }
        return false;
    }

    public static String stripColor(String str) {
        return ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', str));
    }
}
