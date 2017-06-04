package me.zuichu.videolistlibrary.manager.player_messages;


import me.zuichu.videolistlibrary.manager.PlayerMessageState;
import me.zuichu.videolistlibrary.manager.manager.VideoPlayerManagerCallback;

/**
 * This PlayerMessage creates new MediaPlayer instance that will be used inside
 */
public class CreateNewPlayerInstance extends PlayerMessage {

    public CreateNewPlayerInstance( VideoPlayerManagerCallback callback) {
        super(callback);
    }

    @Override
    protected void performAction() {

    }

    @Override
    protected PlayerMessageState stateBefore() {
        return PlayerMessageState.CREATING_PLAYER_INSTANCE;
    }

    @Override
    protected PlayerMessageState stateAfter() {
        return PlayerMessageState.PLAYER_INSTANCE_CREATED;
    }
}
