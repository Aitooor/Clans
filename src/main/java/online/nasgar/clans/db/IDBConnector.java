package online.nasgar.clans.db;

import java.util.Map;
import java.util.UUID;

import online.nasgar.clans.core.Clan;

public interface IDBConnector {
	public void loadClans();
	public boolean saveClans(Map<UUID, Clan> clans);
}
