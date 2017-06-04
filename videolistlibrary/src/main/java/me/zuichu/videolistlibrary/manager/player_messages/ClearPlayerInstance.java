package me.zuichu.videolistlibrary.manager.player_messages;


import me.zuichu.videolistlibrary.manager.PlayerMessageState;
import me.zuichu.videolistlibrary.manager.manager.VideoPlayerManagerCallback;

/**
 * This PlayerMessage clears MediaPlayer instance that was used inside
 */
public class ClearPlayerInstance extends PlayerMessage {

    public ClearPlayerInstance(VideoPlayerManagerCallback callback) {
        super(callback);
    }

    @Override
    protected void performAction() {

    }

    @Override
    protected PlayerMessageState stateBefore() {
        return PlayerMessageState.CLEARING_PLAYER_INSTANCE;
    }

    @Override
    protected PlayerMessageState stateAfter() {
        return PlayerMessageState.PLAYER_INSTANCE_CLEARED;
    }
}
