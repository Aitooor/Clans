package online.nasgar.clans.commands.player;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import online.nasgar.clans.Clans;
import online.nasgar.clans.commands.ACommand;
import online.nasgar.clans.commands.CommandException;
import online.nasgar.clans.core.Clan;
import online.nasgar.clans.core.ClanMember;
import online.nasgar.clans.core.Pendings;
import online.nasgar.clans.lang.Lang;

public class AcceptCommand extends ACommand {
	
	protected static final boolean consoleExecutable = false;

	public AcceptCommand(CommandSender sender, String[] args, Clans plugin) {
		super(sender, args, plugin);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute() throws Exception {
		if (!consoleExecutable && !(sender instanceof Player)) {
			throw new CommandException(Lang.getLang("must_be_player"));
		}
		Player p = (Player) sender;
		UUID toAcceptId = Pendings.getInvitePending(p.getUniqueId());
		if (toAcceptId == null) {
			throw new CommandException(Lang.getLang("no_invites"));
		}
		Pendings.removeInvitePending(p.getUniqueId());
		Clan toAccept = online.nasgar.clans.core.Clans.getClan(toAcceptId);
		if (toAccept == null || toAccept.isDisbanded()) {
			throw new CommandException(Lang.getLang("accept_disbanded"));
		}
		if (toAccept.clanSize()+1> online.nasgar.clans.core.Clans.getMaxMembers(toAccept.getLevel())) {
			throw new CommandException(Lang.getLang("invite_maxsize")+ online.nasgar.clans.core.Clans.getMaxMembers(toAccept.getLevel()));
		}
		ClanMember member = online.nasgar.clans.core.Clans.getMember(p.getUniqueId());
		if (member != null) {
			throw new CommandException(Lang.getLang("already_in_clan"));
		}
		ClanMember newMember = toAccept.addPlayer(p);
		if (newMember != null) {
			p.sendMessage(String.format(Lang.getLang("accepted_invite"), Lang.colorString(toAccept.getName())));
			newMember.getClan().clanMessage(String.format(Lang.getLang("enter_msg"), newMember.getName()));
		}
		return false;
	}

	@Override
	public boolean executeAsync() throws CommandException {
		// TODO Auto-generated method stub
		return false;
	}

}
