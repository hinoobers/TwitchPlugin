package org.hinoob.twitchdisplay.task;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.hinoob.twitchdisplay.ConfigVariables;
import org.hinoob.twitchdisplay.TwitchDisplay;

public class BossbarUpdateTask implements Runnable{

    @Override
    public void run() {
        String highestStreamer = TwitchDisplay.INSTANCE.getHighligtedStreamer();
        if(highestStreamer != null){
            if(TwitchDisplay.INSTANCE.bossbar.getColor() != ConfigVariables.onlineColor){
                TwitchDisplay.INSTANCE.bossbar.setColor(ConfigVariables.onlineColor);
            }

            if(TwitchDisplay.INSTANCE.bossbar.getProgress() != ConfigVariables.onlineProgress){
                TwitchDisplay.INSTANCE.bossbar.setProgress(ConfigVariables.onlineProgress);
            }

            if(TwitchDisplay.INSTANCE.onlineBossbarIndex >= ConfigVariables.onlineBossbarMessages.size()){
                TwitchDisplay.INSTANCE.onlineBossbarIndex = 0;
            }

            for(Player player : Bukkit.getOnlinePlayers()){
                if(!TwitchDisplay.INSTANCE.bossbar.getPlayers().contains(player)){
                    TwitchDisplay.INSTANCE.bossbar.addPlayer(player);
                }
            }
            String newMessage = ConfigVariables.onlineBossbarMessages.get(TwitchDisplay.INSTANCE.onlineBossbarIndex);
            newMessage = newMessage.replace("%link%", "twitch.tv/" + highestStreamer);
            newMessage = newMessage.replace("%username%", highestStreamer);
            TwitchDisplay.INSTANCE.bossbar.setTitle(newMessage);

            TwitchDisplay.INSTANCE.onlineBossbarIndex++;
        }else{
            if(TwitchDisplay.INSTANCE.bossbar.getColor() != ConfigVariables.offlineColor){
                TwitchDisplay.INSTANCE.bossbar.setColor(ConfigVariables.offlineColor);
            }

            if(TwitchDisplay.INSTANCE.bossbar.getProgress() != ConfigVariables.offlineProgress){
                TwitchDisplay.INSTANCE.bossbar.setProgress(ConfigVariables.offlineProgress);
            }

            if(TwitchDisplay.INSTANCE.offlineBossbarIndex >= ConfigVariables.offlineBossbarMessages.size()){
                TwitchDisplay.INSTANCE.offlineBossbarIndex = 0;
            }

            for(Player player : Bukkit.getOnlinePlayers()){
                if(!TwitchDisplay.INSTANCE.bossbar.getPlayers().contains(player)){
                    TwitchDisplay.INSTANCE.bossbar.addPlayer(player);
                }
            }
            String newMessage = ConfigVariables.offlineBossbarMessages.get(TwitchDisplay.INSTANCE.offlineBossbarIndex);
            TwitchDisplay.INSTANCE.bossbar.setTitle(newMessage);

            TwitchDisplay.INSTANCE.offlineBossbarIndex++;
        }
    }
}
