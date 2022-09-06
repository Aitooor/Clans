package online.nasgar.clans.commands.admin;

import online.nasgar.clans.Clans;
import online.nasgar.clans.commands.ACommand;
import online.nasgar.clans.commands.CommandException;
import online.nasgar.clans.core.Clan;
import online.nasgar.clans.lang.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StorageAdminCommand extends ACommand {

    protected static final boolean consoleExecutable = false;
    protected static final String requirePermission = "clans.admin";

    public StorageAdminCommand(CommandSender sender, String[] args, Clans plugin) {
        super(sender, args, plugin);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean execute() throws Exception {
        if (!consoleExecutable && !(sender instanceof Player)) {
            throw new CommandException(Lang.getLang("must_be_player"));
        }
        Player p = (Player) sender;
        if (!p.hasPermission(requirePermission)) {
            throw new CommandException(Lang.getLang("not_enough_perms"));
        }
        if (args.length < 2) {
            throw new CommandException(Lang.getLang("not_enough_args"));
        }
        String clanName = "";
        for (int i = 1; i < args.length; i++) {
            clanName += args[i] + " ";
        }
        clanName = clanName.trim();
        Clan clan = online.nasgar.clans.core.Clans.getClanByName(clanName);
        if (clan != null) {
            p.openInventory(clan.getStorage());
        } else {
            throw new CommandException(Lang.getLang("no_such_clan"));
        }
        return false;
    }

    @Override
    public boolean executeAsync() throws CommandException {
        // TODO Auto-generated method stub
        return false;
    }

}
