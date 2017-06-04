package me.zuichu.videolistlibrary.manager.player_messages;

import android.media.MediaPlayer;

import me.zuichu.videolistlibrary.manager.PlayerMessageState;
import me.zuichu.videolistlibrary.manager.manager.VideoPlayerManagerCallback;

/**
 * This PlayerMessage calls {@link MediaPlayer#stop()} on the instance that is used inside
 */
public class Stop extends PlayerMessage {
    public Stop(VideoPlayerManagerCallback callback) {
        super(callback);
    }

    @Override
    protected void performAction() {

    }

    @Override
    protected PlayerMessageState stateBefore() {
        return PlayerMessageState.STOPPING;
    }

    @Override
    protected PlayerMessageState stateAfter() {
        return PlayerMessageState.STOPPED;
    }
}
