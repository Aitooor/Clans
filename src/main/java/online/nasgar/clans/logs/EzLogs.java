package online.nasgar.clans.logs;

import online.nasgar.clans.Clans;
import online.nasgar.clans.core.ClanMember;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EzLogs {
    private static Clans plugin = null;
    private static final ConcurrentLinkedQueue<String> logCache = new ConcurrentLinkedQueue<String>();
    private static File logFile = null;
    private static final int flushInterval = 600;

    public static void initialize(Clans p) {
        try {
            plugin = p;
            File folder = plugin.getDataFolder();
            logFile = new File(folder, "logs" + File.separator + getLogName());
            new File(logFile.getParent()).mkdirs();
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
                public void run() {
                    flush();
                }
            }, flushInterval, flushInterval);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void flush() {
        if (!logCache.isEmpty()) {
            try {
                File folder = plugin.getDataFolder();
                logFile = new File(folder, "logs" + File.separator + getLogName());
                new File(logFile.getParent()).mkdirs();
                if (!logFile.exists()) {
                    logFile.createNewFile();
                }
                FileWriter fileWritter = new FileWriter(logFile, true);
                BufferedWriter bw = new BufferedWriter(fileWritter);

                while (!logCache.isEmpty()) {
                    bw.write(logCache.poll());
                    bw.newLine();
                }

                bw.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void logBalance(ClanMember cm, int amt, String action) {
        String entry = "";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        entry = entry += "[" + dtf.format(now) + "] ";
        entry += "Action: " + action;
        entry += " Amt: " + amt;
        entry += " Clan: " + cm.getClan().getTag();
        entry += " Id: " + cm.getClan().getId();
        entry += " Player: " + cm.getName();
        logCache.add(entry);
    }

    public static void logStorage(Player p, Material m, int amt, String action) {
        String entry = "";
        ClanMember cm = online.nasgar.clans.core.Clans.getMember(p.getUniqueId());
        if (cm != null) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            entry = entry += "[" + dtf.format(now) + "] ";
            entry += "Action: " + action;
            entry += " Item: " + m.toString();
            entry += " Amt: " + amt;
            entry += " Clan: " + cm.getClan().getTag();
            entry += " Id: " + cm.getClan().getId();
            entry += " Player: " + p.getName();
            logCache.add(entry);
        }
    }

    private static String getLogName() {
        String result = "";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        result = dtf.format(now);
        result += "-clans.log";
        return result;
    }
}
