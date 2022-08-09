package org.hinoob.twitchdisplay;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


// I know static abuse, stfu
public class ConfigVariables {

    public static Map<String, Integer> trackedStreamers = new HashMap<>();

    public static BarColor onlineColor;
    public static int onlineProgress;
    public static List<String> onlineBossbarMessages = new ArrayList<>();

    public static BarColor offlineColor;
    public static int offlineProgress;
    public static List<String> offlineBossbarMessages = new ArrayList<>();

    public static List<String> joinMessage = new ArrayList<>();
    public static List<String> repeatMessage = new ArrayList<>();

    public static Sound repeatMessageSound;

    public static void load(FileConfiguration configuration){
        for(String streamer : configuration.getConfigurationSection("settings.tracked-streamers").getKeys(false)){
            trackedStreamers.put(streamer, configuration.getInt("settings.tracked-streamers." + streamer));
        }

        onlineBossbarMessages = configuration.getStringList("settings.bossbar.online.animation").stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList());
        offlineBossbarMessages = configuration.getStringList("settings.bossbar.offline.animation").stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList());

        joinMessage = configuration.getStringList("settings.join-message");
        repeatMessage = configuration.getStringList("settings.repeat-message.message");

        onlineColor = BarColor.valueOf(configuration.getString("settings.bossbar.online.color"));
        offlineColor = BarColor.valueOf(configuration.getString("settings.bossbar.offline.color"));

        onlineProgress = configuration.getInt("settings.bossbar.online.progress");
        offlineProgress = configuration.getInt("settings.bossbar.offline.progress");

        repeatMessageSound = Sound.valueOf(configuration.getString("settings.repeat-message.sound").toUpperCase());
    }
}
