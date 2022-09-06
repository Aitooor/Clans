package online.nasgar.clans.db;

import online.nasgar.clans.core.Clan;

import java.util.Map;
import java.util.UUID;

public interface IDBConnector {
    void loadClans();

    boolean saveClans(Map<UUID, Clan> clans);
}
