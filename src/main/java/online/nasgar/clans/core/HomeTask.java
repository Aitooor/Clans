package online.nasgar.clans.core;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class HomeTask implements Runnable {

    private final Player player;
    private final Location location;

    public HomeTask(Player player, Location location) {
        this.player = player;
        this.location = location;
    }

    @Override
    public void run() {
        if (player.isOnline()) {
            player.teleport(location);
            Pendings.removeTeleportPending(player.getUniqueId());
        }
    }

}
