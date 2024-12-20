package online.nasgar.clans.commands.player;

import online.nasgar.clans.Clans;
import online.nasgar.clans.commands.ACommand;
import online.nasgar.clans.commands.CommandException;
import online.nasgar.clans.core.Clan;
import online.nasgar.clans.core.ClanMember;
import online.nasgar.clans.core.Pendings;
import online.nasgar.clans.lang.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InviteCommand extends ACommand {

    protected static final boolean consoleExecutable = false;

    public InviteCommand(CommandSender sender, String[] args, Clans plugin) {
        super(sender, args, plugin);
    }

    @Override
    public boolean execute() throws Exception {
        if (!consoleExecutable && !(sender instanceof Player)) {
            throw new CommandException(Lang.getLang("must_be_player"));
        }
        if (args.length < 2) {
            throw new CommandException(Lang.getLang("not_enough_args"));
        }
        Player p = (Player) sender;
        ClanMember member = online.nasgar.clans.core.Clans.getMember(p.getUniqueId());
        if (member == null) {
            throw new CommandException(Lang.getLang("not_in_clan"));
        }
        if (!member.isLeader() && !member.isModerator()) {
            throw new CommandException(Lang.getLang("not_leader_or_moderator"));
        }
        if (member.getClan().clanSize() + 1 > online.nasgar.clans.core.Clans.getMaxMembers(member.getClan().getLevel())) {
            throw new CommandException(Lang.getLang("invite_maxsize") + online.nasgar.clans.core.Clans.getMaxMembers(member.getClan().getLevel()));
        }
        String toInvite = args[1];
        Player invitedPlayer = Bukkit.getServer().getPlayer(toInvite);
        if (invitedPlayer == null || !invitedPlayer.isOnline()) {
            throw new CommandException(Lang.getLang("player_not_found"));
        }
        if (p.getUniqueId().equals(invitedPlayer.getUniqueId())) {
            throw new CommandException(Lang.getLang("invite_yourself"));
        }
        Clan currentClan = member.getClan();
        ClanMember invitedMember = online.nasgar.clans.core.Clans.getMember(invitedPlayer.getUniqueId());
        if (invitedMember != null) {
            throw new CommandException(Lang.getLang("already_in_clan_other"));
        }
        Pendings.setInvitePending(invitedPlayer.getUniqueId(), currentClan.getId());
        p.sendMessage(String.format(Lang.getLang("invitation_send"), Lang.colorString(toInvite)));
        invitedPlayer.sendMessage(String.format(Lang.getLang("invited_to_clan"), Lang.colorString(currentClan.getName())));
        return false;
    }

    @Override
    public boolean executeAsync() throws CommandException {
        // TODO Auto-generated method stub
        return false;
    }

}
