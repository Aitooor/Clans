package online.nasgar.clans.commands.player;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import online.nasgar.clans.Clans;
import online.nasgar.clans.commands.ACommand;
import online.nasgar.clans.commands.CommandException;
import online.nasgar.clans.core.ClanMember;
import online.nasgar.clans.core.Pendings;
import online.nasgar.clans.lang.Lang;

public class SetleaderCommand extends ACommand {
	protected static final boolean consoleExecutable = false;

	public SetleaderCommand(CommandSender sender, String[] args, Clans plugin) {
		super(sender, args, plugin);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute() throws Exception {
		if (!consoleExecutable && !(sender instanceof Player)) {
			throw new CommandException(Lang.getLang("must_be_player"));
		}
		Player p = (Player) sender;
		
		UUID pending = Pendings.getLeaderPending(p.getUniqueId()); //reset in any case
		Pendings.removeLeaderPending(p.getUniqueId());
		
		if (args.length < 2) {
			throw new CommandException(Lang.getLang("not_enough_args"));
		}
		ClanMember member = online.nasgar.clans.core.Clans.getMember(p.getUniqueId());
		if (member == null) {
			throw new CommandException(Lang.getLang("not_in_clan"));
		}
		if (!member.isLeader()) {
			throw new CommandException(Lang.getLang("not_leader"));
		}
		String toLeader = args[1];
		
		if (pending == null) {
			ClanMember leadered = member.getClan().getMemberByName(toLeader);
			if (leadered == null) {
				throw new CommandException(Lang.getLang("leader_not_in_clan"));
			}
			Pendings.setLeaderPending(p.getUniqueId(), leadered.getUuid());
			p.sendMessage(String.format(Lang.getLang("leader_confirm"), leadered.getName()));
		}
		else if (toLeader.equals("confirm")) {
			ClanMember newLeader = online.nasgar.clans.core.Clans.getMember(pending);
			if (newLeader == null || !newLeader.getClan().getId().equals(member.getClan().getId()) || newLeader.isLeader()) {
				throw new CommandException(Lang.getLang("leader_cancel"));
			}
			newLeader.setAsLeader();
			member.setModerator();
			member.getClan().clanMessage(String.format(Lang.getLang("leader_broadcast"), member.getName(), newLeader.getName()));
		}
		else {
			Pendings.removeLeaderPending(p.getUniqueId());
			throw new CommandException(Lang.getLang("leader_cancel"));
		}
		return false;
	}

	@Override
	public boolean executeAsync() throws CommandException {
		// TODO Auto-generated method stub
		return false;
	}

}
