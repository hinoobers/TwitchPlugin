package org.hinoob.twitchdisplay;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.helix.domain.StreamList;
import com.github.twitch4j.helix.domain.User;

import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class TwitchAPI {

    private TwitchClient client;

    public TwitchAPI(String clientId, String clientSecret) {
        this.client = TwitchClientBuilder.builder()
                .withEnableHelix(true)
                .withClientId(clientId)
                .withClientSecret(clientSecret)
                .build();
    }

    public User getUser(String username){
        return client.getHelix().getUsers(null, null, Arrays.asList(username)).execute().getUsers().stream().filter(u -> u.getDisplayName().equals(username)).findAny().orElse(null);
    }

    public boolean isUsernameStreaming(String username){
        String userId = getUser(username).getId();;

        StreamList list = client.getHelix().getStreams(null, null, null, 5, null, null, Arrays.asList(userId), null).execute();
        if(list.getStreams().stream().anyMatch(stream -> stream.getUserName().equals(username))){
            return true;
        }
        return false;
    }
}
