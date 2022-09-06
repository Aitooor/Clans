package online.nasgar.clans.commands.admin;

import online.nasgar.clans.Clans;
import online.nasgar.clans.commands.ACommand;
import online.nasgar.clans.commands.CommandException;
import online.nasgar.clans.commands.CommandManager;
import online.nasgar.clans.core.ClanMember;
import online.nasgar.clans.lang.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RenameAdminCommand extends ACommand {

    protected static final String requirePermission = "clans.admin";

    public RenameAdminCommand(CommandSender sender, String[] args, Clans plugin) {
        super(sender, args, plugin);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean execute() throws Exception {
        if (sender instanceof Player && !sender.hasPermission(requirePermission)) {
            throw new CommandException(Lang.getLang("not_enough_perms"));
        }
        if (args.length < 3) {
            throw new CommandException(Lang.getLang("not_enough_args"));
        }
        String toRename = args[1];
        ClanMember retagMember = online.nasgar.clans.core.Clans.getMemberByName(toRename);
        if (retagMember == null) {
            throw new CommandException(Lang.getLang("not_in_clan_other"));
        }
        String clanName = "";
        for (int i = 2; i < args.length; i++) {
            clanName += args[i] + " ";
        }
        clanName = clanName.trim();
        if (!CommandManager.legalTag(clanName)) {
            throw new CommandException(Lang.getLang("illegal_tag"));
        }
        retagMember.getClan().setName(clanName);
        sender.sendMessage(String.format(Lang.getLang("clan_rename"), Lang.colorString(clanName)));

        return false;
    }

    @Override
    public boolean executeAsync() throws CommandException {
        // TODO Auto-generated method stub
        return false;
    }

}
