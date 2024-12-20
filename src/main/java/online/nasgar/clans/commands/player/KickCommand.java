package online.nasgar.clans.commands.player;

import online.nasgar.clans.Clans;
import online.nasgar.clans.commands.ACommand;
import online.nasgar.clans.commands.CommandException;
import online.nasgar.clans.core.ClanMember;
import online.nasgar.clans.lang.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand extends ACommand {

    protected static final boolean consoleExecutable = false;

    public KickCommand(CommandSender sender, String[] args, Clans plugin) {
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
        ClanMember member = online.nasgar.clans.core.Clans.getMember(p.getUniqueId());
        if (member == null) {
            throw new CommandException(Lang.getLang("not_in_clan"));
        }
        if (!member.isLeader() && !member.isModerator()) {
            throw new CommandException(Lang.getLang("not_leader_or_moderator"));
        }
        String toKick = args[1];
        ClanMember kicked = member.getClan().getMemberByName(toKick);
        if (kicked == null) {
            throw new CommandException(Lang.getLang("kick_not_in_clan"));
        }
        if (p.getUniqueId().equals(kicked.getUuid())) {
            throw new CommandException(Lang.getLang("kick_yourself"));
        }
        if (member.isModerator() && (kicked.isModerator() || kicked.isLeader())) {
            throw new CommandException(Lang.getLang("kick_not_enough"));
        }
        if (member.getClan().removeMember(kicked)) {
            Player kickedPlayer = Bukkit.getServer().getPlayer(kicked.getUuid());
            if (kickedPlayer != null && kickedPlayer.isOnline()) {
                kickedPlayer.sendMessage(String.format(Lang.getLang("kicked_you"), Lang.colorString(member.getClan().getName())));
            }
            member.getClan().clanMessage(String.format(Lang.getLang("kick_msg"), kicked.getName()));
        }

        return false;
    }

    @Override
    public boolean executeAsync() throws CommandException {
        // TODO Auto-generated method stub
        return false;
    }

}
