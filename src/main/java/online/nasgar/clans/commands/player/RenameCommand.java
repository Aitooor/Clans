package online.nasgar.clans.commands.player;

import online.nasgar.clans.Clans;
import online.nasgar.clans.commands.ACommand;
import online.nasgar.clans.commands.CommandException;
import online.nasgar.clans.commands.CommandManager;
import online.nasgar.clans.core.Clan;
import online.nasgar.clans.core.ClanMember;
import online.nasgar.clans.lang.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RenameCommand extends ACommand {

    protected static final boolean consoleExecutable = false;

    public RenameCommand(CommandSender sender, String[] args, Clans plugin) {
        super(sender, args, plugin);
    }

    @Override
    public boolean execute() throws Exception {
        if (!consoleExecutable && !(sender instanceof Player)) {
            throw new CommandException(Lang.getLang("must_be_player"));
        }
        Player p = (Player) sender;
        if (args.length < 2) {
            throw new CommandException(Lang.getLang("not_enough_args"));
        }
        ClanMember currentMember = online.nasgar.clans.core.Clans.getMember(p.getUniqueId());
        if (currentMember == null) {
            throw new CommandException(Lang.getLang("not_in_clan"));
        }
        if (!currentMember.isLeader()) {
            throw new CommandException(Lang.getLang("not_leader"));
        }
        String clanName = "";
        for (int i = 1; i < args.length; i++) {
            clanName += args[i] + " ";
        }
        clanName = clanName.trim();
        if (!CommandManager.legalName(clanName)) {
            throw new CommandException(Lang.getLang("illegal_name"));
        }
        Clan byName = online.nasgar.clans.core.Clans.getClanByName(clanName);
        if (byName != null) {
            throw new CommandException(Lang.getLang("already_exists"));
        }

        currentMember.getClan().setName(clanName);
        p.sendMessage(String.format(Lang.getLang("clan_rename"), Lang.colorString(clanName)));

        return false;
    }

    @Override
    public boolean executeAsync() throws CommandException {
        // TODO Auto-generated method stub
        return false;
    }

}
