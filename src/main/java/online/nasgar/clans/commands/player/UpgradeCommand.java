package online.nasgar.clans.commands.player;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import online.nasgar.clans.Clans;
import online.nasgar.clans.commands.ACommand;
import online.nasgar.clans.commands.CommandException;
import online.nasgar.clans.core.ClanException;
import online.nasgar.clans.core.ClanMember;
import online.nasgar.clans.lang.Lang;

public class UpgradeCommand extends ACommand {
	
	protected static final boolean consoleExecutable = false;

	public UpgradeCommand(CommandSender sender, String[] args, Clans plugin) {
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
		try {
			member.getClan().upgrade();
		}
		catch (ClanException e) {
			p.sendMessage(Lang.getLang("upgrade_fail")+" "+e.getMessage());
		}
		catch (Exception e) {
			p.sendMessage(Lang.getLang("upgrade_fail")+"unknown");
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean executeAsync() throws CommandException {
		// TODO Auto-generated method stub
		return false;
	}

}
