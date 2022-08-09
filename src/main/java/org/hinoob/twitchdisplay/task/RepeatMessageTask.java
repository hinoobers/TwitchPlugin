package org.hinoob.twitchdisplay.task;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.hinoob.twitchdisplay.ConfigVariables;
import org.hinoob.twitchdisplay.TwitchDisplay;

public class RepeatMessageTask implements Runnable{

    @Override
    public void run() {
        if(TwitchDisplay.INSTANCE.getHighligtedStreamer() == null) return;
        for(Player player : Bukkit.getOnlinePlayers()){
            for(String message : ConfigVariables.repeatMessage){
                message = message.replaceAll("%streamer%", TwitchDisplay.INSTANCE.getHighligtedStreamer());
                message = message.replaceAll("%link%", "twitch.tv/" + TwitchDisplay.INSTANCE.getHighligtedStreamer());

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
            player.playSound(player.getLocation(), ConfigVariables.repeatMessageSound, 1f, 1f);
        }
    }
}
