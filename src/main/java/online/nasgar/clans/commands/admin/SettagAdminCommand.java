package online.nasgar.clans.commands.admin;

import online.nasgar.clans.Clans;
import online.nasgar.clans.commands.CommandException;
import online.nasgar.clans.commands.CommandManager;
import online.nasgar.clans.core.ClanMember;
import online.nasgar.clans.lang.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import online.nasgar.clans.commands.ACommand;

public class SettagAdminCommand extends ACommand {
	
	protected static final String requirePermission = "clans.admin";

	public SettagAdminCommand(CommandSender sender, String[] args, Clans plugin) {
		super(sender, args, plugin);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute() throws Exception {

		if (sender instanceof Player && !((Player) sender).hasPermission(requirePermission)) {
			throw new CommandException(Lang.getLang("not_enough_perms"));
		}
		if (args.length < 3) {
			throw new CommandException(Lang.getLang("not_enough_args"));
		}
		String toRetag = args[1];
		ClanMember retagMember = online.nasgar.clans.core.Clans.getMemberByName(toRetag);
		if (retagMember == null) {
			throw new CommandException(Lang.getLang("not_in_clan_other"));
		}
		String clanTag = "";
		for (int i = 2; i < args.length; i++) {
			clanTag += args[i]+" ";
		}
		clanTag = clanTag.trim();
		if (!CommandManager.legalTag(clanTag)) {
			throw new CommandException(Lang.getLang("illegal_tag"));
		}
		retagMember.getClan().setTag(clanTag);
		retagMember.getClan().clanMessage(String.format(Lang.getLang("tag_changed"), Lang.colorString(clanTag)));
		
		return false;
	}

	@Override
	public boolean executeAsync() throws CommandException {
		// TODO Auto-generated method stub
		return false;
	}

}
