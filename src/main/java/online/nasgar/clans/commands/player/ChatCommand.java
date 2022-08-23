package online.nasgar.clans.commands.player;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import online.nasgar.clans.Clans;
import online.nasgar.clans.commands.ACommand;
import online.nasgar.clans.commands.CommandException;
import online.nasgar.clans.commands.CommandManager;
import online.nasgar.clans.core.ClanMember;
import online.nasgar.clans.lang.Lang;

public class ChatCommand extends ACommand {
	
	protected static final boolean consoleExecutable = false;

	public ChatCommand(CommandSender sender, String[] args, Clans plugin) {
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
		String msg = "";
		for (int i = 1; i < args.length; i++) {
			msg += args[i]+" ";
		}
		msg = CommandManager.stripColor(msg).trim();
		member.getClan().clanMessage(member, msg);
		return false;
	}

	@Override
	public boolean executeAsync() throws CommandException {
		return false;
	}

}
