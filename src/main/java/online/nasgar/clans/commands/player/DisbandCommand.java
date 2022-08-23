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

public class DisbandCommand extends ACommand {
	
	protected static final boolean consoleExecutable = false;

	public DisbandCommand(CommandSender sender, String[] args, Clans plugin) {
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
		if (!member.isLeader()) {
			throw new CommandException(Lang.getLang("not_leader"));
		}
		UUID pending = Pendings.getDisbandPending(p.getUniqueId());
		if (pending == null) {
			Pendings.setDisbandPending(p.getUniqueId(), member.getClan().getId());
			p.sendMessage(String.format(Lang.getLang("disband_confirm"), Lang.colorString(member.getClan().getName())));
		}
		else {
			if (!pending.equals(member.getClan().getId()) || args.length < 2 || !args[1].equals("confirm")) {
				Pendings.removeDisbandPending(p.getUniqueId());
				p.sendMessage(Lang.getLang("disband_canceled"));
			}
			else {
				online.nasgar.clans.core.Clans.removeClan(pending);
				Pendings.removeDisbandPending(p.getUniqueId());
				p.sendMessage(String.format(Lang.getLang("disband"), Lang.colorString(member.getClan().getName())));
			}
		}
		return false;
	}

	@Override
	public boolean executeAsync() throws CommandException {
		// TODO Auto-generated method stub
		return false;
	}

}
