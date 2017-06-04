package me.zuichu.videolistlibrary.manager.player_messages;

import android.media.MediaPlayer;

import me.zuichu.videolistlibrary.manager.PlayerMessageState;
import me.zuichu.videolistlibrary.manager.manager.VideoPlayerManagerCallback;


/**
 * This PlayerMessage calls {@link MediaPlayer#reset()} on the instance that is used inside
 */
public class Reset extends PlayerMessage {
    public Reset( VideoPlayerManagerCallback callback) {
        super(callback);
    }

    @Override
    protected void performAction() {

    }

    @Override
    protected PlayerMessageState stateBefore() {
        return PlayerMessageState.RESETTING;
    }

    @Override
    protected PlayerMessageState stateAfter() {
        return PlayerMessageState.RESET;
    }
}
