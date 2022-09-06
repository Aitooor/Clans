package online.nasgar.clans.commands.player;

import online.nasgar.clans.Clans;
import online.nasgar.clans.commands.ACommand;
import online.nasgar.clans.commands.CommandException;
import online.nasgar.clans.core.Clan;
import online.nasgar.clans.core.ClanMember;
import online.nasgar.clans.lang.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InfoCommand extends ACommand {

    public static final boolean consoleExecutable = false;

    public InfoCommand(CommandSender sender, String[] args, Clans plugin) {
        super(sender, args, plugin);
    }

    @Override
    public boolean execute() throws Exception {
        if (!consoleExecutable && !(sender instanceof Player)) {
            throw new CommandException(Lang.getLang("must_be_player"));
        }
        if (args.length > 1) {
            String clanName = "";
            for (int i = 1; i < args.length; i++) {
                clanName += args[i] + " ";
            }
            clanName = clanName.trim();
            Clan clan = online.nasgar.clans.core.Clans.getClanByName(clanName);
            if (clan != null) {
                online.nasgar.clans.core.Clans.msgInfo(clan, (Player) sender);
            } else {
                throw new CommandException(Lang.getLang("no_such_clan"));
            }
        } else {
            Player player = (Player) sender;
            ClanMember member = online.nasgar.clans.core.Clans.getMember(player.getUniqueId());
            if (member != null) {
                online.nasgar.clans.core.Clans.msgInfoMember(member.getClan(), player);
            } else {
                throw new CommandException(Lang.getLang("no_such_clan"));
            }
        }

        return false;
    }

    @Override
    public boolean executeAsync() {
        // TODO Auto-generated method stub
        return false;
    }

}
