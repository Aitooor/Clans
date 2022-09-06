package online.nasgar.clans.commands.player;

import online.nasgar.clans.Clans;
import online.nasgar.clans.commands.ACommand;
import online.nasgar.clans.commands.CommandException;
import online.nasgar.clans.core.Clan;
import online.nasgar.clans.core.ClanMember;
import online.nasgar.clans.lang.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SethomeCommand extends ACommand {

    protected static final boolean consoleExecutable = false;

    public SethomeCommand(CommandSender sender, String[] args, Clans plugin) {
        super(sender, args, plugin);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean execute() throws Exception {
        if (!consoleExecutable && !(sender instanceof Player)) {
            throw new CommandException(Lang.getLang("must_be_player"));
        }
        Player p = (Player) sender;
        ClanMember member = online.nasgar.clans.core.Clans.getMember(p.getUniqueId());
        if (member == null) {
            throw new CommandException(Lang.getLang("not_in_clan"));
        }
        if (!member.isLeader() && !member.isModerator()) {
            throw new CommandException(Lang.getLang("not_leader_or_moderator"));
        }
        Clan clan = member.getClan();
        if (clan.getBank() >= online.nasgar.clans.core.Clans.getSetHomeCost()) {
            clan.setHome(p.getLocation());
            p.sendMessage(Lang.getLang("sethome"));
            clan.withdraw(online.nasgar.clans.core.Clans.getSetHomeCost());
        } else {
            throw new CommandException(String.format(Lang.getLang("bank_not_enough"), online.nasgar.clans.core.Clans.getSetHomeCost() + ""));
        }
        return false;
    }

    @Override
    public boolean executeAsync() throws CommandException {
        // TODO Auto-generated method stub
        return false;
    }

}
