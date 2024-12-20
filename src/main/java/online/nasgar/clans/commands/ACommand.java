package online.nasgar.clans.commands;

import online.nasgar.clans.Clans;
import org.bukkit.command.CommandSender;

public abstract class ACommand {
    protected static final boolean consoleExecutable = false;
    protected CommandSender sender;
    protected String[] args;
    protected Clans plugin;

    public ACommand(CommandSender sender, String[] args, Clans plugin) {
        this.sender = sender;
        this.args = args;
        this.plugin = plugin;
    }

    public abstract boolean execute() throws Exception;

    public abstract boolean executeAsync() throws CommandException;
}
