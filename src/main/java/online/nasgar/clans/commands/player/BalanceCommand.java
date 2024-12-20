package online.nasgar.clans.commands.player;

import online.nasgar.clans.Clans;
import online.nasgar.clans.commands.ACommand;
import online.nasgar.clans.commands.CommandException;
import online.nasgar.clans.core.Clan;
import online.nasgar.clans.core.ClanMember;
import online.nasgar.clans.integrations.EzEconomy;
import online.nasgar.clans.lang.Lang;
import online.nasgar.clans.logs.EzLogs;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand extends ACommand {

    protected static final boolean consoleExecutable = false;

    public BalanceCommand(CommandSender sender, String[] args, Clans plugin) {
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
        Clan clan = member.getClan();
        if (args.length < 2) {
            p.sendMessage(String.format(Lang.getLang("balance"), clan.getBank()));
        } else if (args.length > 2) {
            if (!member.isLeader() && !member.isModerator()) {
                throw new CommandException(Lang.getLang("not_leader_or_moderator"));
            }
            String action = args[1];
            String amt = args[2];
            int parsedAmt = -1;
            try {
                parsedAmt = Integer.parseInt(amt);
            } catch (NumberFormatException e) {
            }

            if (parsedAmt <= 0) {
                throw new CommandException(Lang.getLang("amt_invalid"));
            }
            double balance = EzEconomy.getBalance(p);
            int bank = clan.getBank();
            int maxBank = online.nasgar.clans.core.Clans.getMaxBank(clan.getLevel());
            if (action.equals("add")) {
                if (balance >= parsedAmt) {
                    if (parsedAmt + bank > maxBank) {
                        p.sendMessage(String.format(Lang.getLang("maxbank"), maxBank + ""));
                    } else {
                        clan.deposit(parsedAmt);
                        EzEconomy.withdraw(p, parsedAmt);
                        p.sendMessage(String.format(Lang.getLang("balance_add").replace("%s", amt), parsedAmt + ""));
                        EzLogs.logBalance(member, parsedAmt, "deposit");
                    }
                } else {
                    throw new CommandException(String.format(Lang.getLang("not_enough_money"), parsedAmt + ""));
                }
            } else if (action.equals("take")) {
                if (bank >= parsedAmt) {
                    clan.withdraw(parsedAmt);
                    EzEconomy.pay(p, parsedAmt);
                    p.sendMessage(String.format(Lang.getLang("balance_take").replace("%s", amt), parsedAmt + ""));
                    EzLogs.logBalance(member, parsedAmt, "withdraw");
                } else {
                    throw new CommandException(String.format(Lang.getLang("bank_not_enough"), parsedAmt + ""));
                }
            } else {
                throw new CommandException(Lang.getLang("balance_usage"));
            }
        } else {
            throw new CommandException(Lang.getLang("balance_usage"));
        }

        return false;
    }

    @Override
    public boolean executeAsync() throws CommandException {
        // TODO Auto-generated method stub
        return false;
    }

}
