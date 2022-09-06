package online.nasgar.clans.commands.player;

import online.nasgar.clans.Clans;
import online.nasgar.clans.commands.ACommand;
import online.nasgar.clans.commands.CommandException;
import online.nasgar.clans.core.ClanMember;
import online.nasgar.clans.core.HomeTask;
import online.nasgar.clans.core.Pendings;
import online.nasgar.clans.core.TeleportPlayerInfo;
import online.nasgar.clans.lang.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeCommand extends ACommand {

    protected static final boolean consoleExecutable = false;

    public HomeCommand(CommandSender sender, String[] args, Clans plugin) {
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
        Location home = member.getClan().getHome();
        if (home == null) {
            throw new CommandException(Lang.getLang("no_clanhome"));
        }
        TeleportPlayerInfo info = Pendings.getTeleportPending(p.getUniqueId());
        if (info != null) {
            return false;
        }
        HomeTask ht = new HomeTask(p, home);
        int cd = online.nasgar.clans.core.Clans.getHomeCd(member.getClan().getLevel());
        p.sendMessage(String.format(Lang.getLang("home_prepare"), cd));
        int taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, ht, 20 * cd);
        TeleportPlayerInfo tpinfo = new TeleportPlayerInfo(taskId, p.getLocation());
        Pendings.setTeleportPending(p.getUniqueId(), tpinfo);
        return false;
    }

    @Override
    public boolean executeAsync() throws CommandException {
        // TODO Auto-generated method stub
        return false;
    }

}
