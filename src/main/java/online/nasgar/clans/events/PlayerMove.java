package online.nasgar.clans.events;

import online.nasgar.clans.core.Pendings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import online.nasgar.clans.core.TeleportPlayerInfo;
import online.nasgar.clans.lang.Lang;

public class PlayerMove implements Listener {
	
	public PlayerMove() {
		super();
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
	    Player player = event.getPlayer();
	    double distance = event.getFrom().distance(event.getTo());
	    if (distance < 0.1) {
	    	return;
	    }
	    TeleportPlayerInfo info = Pendings.getTeleportPending(player.getUniqueId());
	    if (info != null) {
	    	Bukkit.getScheduler().cancelTask(info.getTask());
	    	Pendings.removeTeleportPending(player.getUniqueId());
	    	player.sendMessage(Lang.getLang("home_canceled"));
	    }
	}
}
