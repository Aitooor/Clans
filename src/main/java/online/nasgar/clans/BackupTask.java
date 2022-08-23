package online.nasgar.clans;

import java.io.File;
import java.nio.file.Files;
import java.util.Date;

import org.bukkit.Bukkit;

public class BackupTask implements Runnable {
	private File toBackup;
	public BackupTask(File toBackup) {
		super();
		this.toBackup = toBackup;
	}
	@Override
	public void run() {
		//Bukkit.getLogger().info("[Clans] Running backup!");
		try {
			Clans.db.saveClans(online.nasgar.clans.core.Clans.getClans());
		}
		catch (Exception e) {
			Bukkit.getLogger().severe("[Clans] Could not save clans!");
			e.printStackTrace();
		}
		if (toBackup.exists()) {
			try {
				String newPath = toBackup.getParent()+File.separator+"backups"+File.separator+toBackup.getName()+(new Date()).getTime();
				//Bukkit.getLogger().info("[Clans] Saving to: "+newPath);
				File target = new File(newPath);
				target.getParentFile().mkdirs();
				Files.copy(toBackup.toPath(), target.toPath());
				//Bukkit.getLogger().info("[Clans] Backup success!");
			}
			catch (Exception e) {
				Bukkit.getLogger().severe("[Clans] Backup fail!");
				e.printStackTrace();
			}
		}
	}
}
