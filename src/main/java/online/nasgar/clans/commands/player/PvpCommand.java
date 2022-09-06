package online.nasgar.clans.commands.player;

import online.nasgar.clans.Clans;
import online.nasgar.clans.commands.ACommand;
import online.nasgar.clans.commands.CommandException;
import online.nasgar.clans.core.ClanMember;
import online.nasgar.clans.lang.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PvpCommand extends ACommand {

    public static final boolean consoleExecutable = false;

    public PvpCommand(CommandSender sender, String[] args, Clans plugin) {
        super(sender, args, plugin);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean execute() throws Exception {
        if (!consoleExecutable && !(sender instanceof Player)) {
            throw new CommandException(Lang.getLang("must_be_player"));
        }
        Player player = (Player) sender;
        ClanMember member = online.nasgar.clans.core.Clans.getMember(player.getUniqueId());
        if (member == null) {
            sender.sendMessage(Lang.getLang("not_in_clan"));
            return false;
        }
        boolean newPvp = !member.isClanPvp();
        member.setClanPvp(newPvp);
        String status = newPvp ? Lang.getLang("enabled") : Lang.getLang("disabled");
        player.sendMessage(String.format(Lang.getLang("pvp_mode"), status));
        return false;
    }

    @Override
    public boolean executeAsync() throws CommandException {
        // TODO Auto-generated method stub
        return false;
    }
}
