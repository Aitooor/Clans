package online.nasgar.clans.commands.player;

import online.nasgar.clans.integrations.EzEconomy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import online.nasgar.clans.Clans;
import online.nasgar.clans.commands.ACommand;
import online.nasgar.clans.commands.CommandException;
import online.nasgar.clans.commands.CommandManager;
import online.nasgar.clans.core.Clan;
import online.nasgar.clans.core.ClanMember;
import online.nasgar.clans.core.Pendings;
import online.nasgar.clans.lang.Lang;

public class CreateCommand extends ACommand{
	
	protected static final boolean consoleExecutable = false;

	public CreateCommand(CommandSender sender, String[] args, Clans plugin) {
		super(sender, args, plugin);
	}
	
	@Override
	public boolean execute() throws Exception {
		if (!consoleExecutable && !(sender instanceof Player)) {
			throw new CommandException(Lang.getLang("must_be_player"));
		}
		Player p = (Player) sender;
		double cost = online.nasgar.clans.core.Clans.getClanCost(1);
		if (args.length < 2) {
			throw new CommandException(Lang.getLang("not_enough_args"));
		}
		ClanMember currentMember = online.nasgar.clans.core.Clans.getMember(p.getUniqueId());
		if (currentMember != null) {
			throw new CommandException(Lang.getLang("already_in_clan"));
		}
		String clanName = "";
		for (int i = 1; i < args.length; i++) {
			clanName += args[i]+" ";
		}
		clanName = clanName.trim();
		if (!CommandManager.legalName(clanName)) {
			throw new CommandException(Lang.getLang("illegal_name"));
		}
		Clan byName = online.nasgar.clans.core.Clans.getClanByName(clanName);
		if (byName != null) {
			throw new CommandException(Lang.getLang("already_exists"));
		}
		String pending = Pendings.getPending(p.getUniqueId());
		if (pending != null) {
			if (!pending.equals(clanName)) {
				Pendings.removePending(p.getUniqueId());
				throw new CommandException(Lang.getLang("repeat_name"));
			}
			else {
				double balance = EzEconomy.getBalance(p);
				if (balance < cost) {
					throw new CommandException(String.format(Lang.getLang("not_enough_money"), Double.toString(cost)));
				}
				Clan clan = online.nasgar.clans.core.Clans.createClan(clanName);
				if (clan == null) {
					throw new CommandException(Lang.getLang("could_not_create"));
				}
				ClanMember member = clan.addPlayer(p);
				member.setAsLeader();
				EzEconomy.withdraw(p, cost);
				sender.sendMessage(String.format(Lang.getLang("clan_created"), Lang.colorString(clanName)));
				Pendings.removePending(p.getUniqueId());
			}
		}
		else {
			Pendings.setPending(p.getUniqueId(), clanName);
			p.sendMessage(String.format(Lang.getLang("clan_create_confirm"), Lang.colorString(clanName), Double.toString(cost)));
		}
		
		return false;
	}

	@Override
	public boolean executeAsync() {
		// TODO Auto-generated method stub
		return false;
	}

}
