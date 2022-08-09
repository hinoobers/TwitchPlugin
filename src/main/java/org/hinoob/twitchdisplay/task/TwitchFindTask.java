package org.hinoob.twitchdisplay.task;

import com.github.twitch4j.helix.domain.User;
import org.hinoob.twitchdisplay.ConfigVariables;
import org.hinoob.twitchdisplay.TwitchDisplay;

public class TwitchFindTask implements Runnable{


    @Override
    public void run() {
        TwitchDisplay.INSTANCE.trackedLiveStreamers.clear();
        for(String trackedStreamer : ConfigVariables.trackedStreamers.keySet()){
            User user = TwitchDisplay.INSTANCE.api.getUser(trackedStreamer);
            if(user != null && TwitchDisplay.INSTANCE.api.isUsernameStreaming(trackedStreamer)){
                // Don't know if there's a better way to do this
                TwitchDisplay.INSTANCE.trackedLiveStreamers.add(trackedStreamer);
            }
        }
    }
}
