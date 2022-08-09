package org.hinoob.twitchdisplay;

import com.github.twitch4j.helix.domain.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Boss;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.hinoob.twitchdisplay.task.BossbarUpdateTask;
import org.hinoob.twitchdisplay.task.RepeatMessageTask;
import org.hinoob.twitchdisplay.task.TwitchFindTask;

import java.util.*;
import java.util.stream.Collectors;

public class TwitchDisplay extends JavaPlugin implements Listener {

    public List<String> trackedLiveStreamers = new ArrayList<>();

    public TwitchAPI api;
    public BossBar bossbar;

    public int onlineBossbarIndex, offlineBossbarIndex;
    public static TwitchDisplay INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        saveDefaultConfig();

        // Handle config
        if(getConfig().getConfigurationSection("settings.tracked-streamers") == null){
            getLogger().info("No streamers set in config.yml, disabling plugin.");
            getPluginLoader().disablePlugin(this);
            return;
        }

        ConfigVariables.load(getConfig());

        // Handle instances
        this.bossbar = Bukkit.createBossBar("", BarColor.RED, BarStyle.SOLID);
        if(getConfig().getString("settings.twitch.client-id").equals("XXX") || getConfig().getString("settings.twitch.client-secret").equals("XXX")){
            getLogger().info("No client id or client secret set in config.yml, disabling plugin.");
            getPluginLoader().disablePlugin(this);
            return;
        }

        this.api = new TwitchAPI(
                getConfig().getString("settings.twitch.client-id"),
                getConfig().getString("settings.twitch.client-secret")
        );

        // Handle events
        Bukkit.getPluginManager().registerEvents(this, this);

        // Start runnables
        this.getServer().getScheduler().runTaskTimer(this, new TwitchFindTask(), getConfig().getInt("settings.update-tick"), getConfig().getInt("settings.update-tick"));
        this.getServer().getScheduler().runTaskTimer(this, new BossbarUpdateTask(), getConfig().getInt("settings.bossbar.interval"), getConfig().getInt("settings.bossbar.interval"));
        this.getServer().getScheduler().runTaskTimer(this, new RepeatMessageTask(), getConfig().getInt("settings.repeat-message.interval"), getConfig().getInt("settings.repeat-message.interval"));
    }

    @Override
    public void onDisable() {
        getLogger().info("TwitchDisplay disabled");

        if(this.bossbar != null){
            bossbar.removeAll();
        }
    }

    public String getHighligtedStreamer(){
        if(trackedLiveStreamers.isEmpty()) return null;
        if(trackedLiveStreamers.size() == 1) return trackedLiveStreamers.get(0);

        int highestIndex = 0;
        String highestStreamer = null;

        for(String streamer : trackedLiveStreamers){
            int currentIndex = ConfigVariables.trackedStreamers.get(streamer);
            if(currentIndex > highestIndex){
                highestIndex = currentIndex;
                highestStreamer = streamer;
            }
        }

        return highestStreamer;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        String highlightedStreamer = getHighligtedStreamer(); // Avoid sorting too much
        if(highlightedStreamer != null){
            for(String message : ConfigVariables.joinMessage){
                message = message.replaceAll("%link%", "twitch.tv/" + highlightedStreamer);
                message = message.replaceAll("%username%", highlightedStreamer);
                message = ChatColor.translateAlternateColorCodes('&', message);
                event.getPlayer().sendMessage(message);
            }
        }
    }
}
