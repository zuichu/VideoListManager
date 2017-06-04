package me.zuichu.videolistlibrary.manager.player_messages;


import me.zuichu.videolistlibrary.manager.PlayerMessageState;
import me.zuichu.videolistlibrary.manager.manager.VideoPlayerManagerCallback;

/**
 * This is generic PlayerMessage for setDataSource
 */
public abstract class SetDataSourceMessage extends PlayerMessage {

    public SetDataSourceMessage(VideoPlayerManagerCallback callback) {
        super(callback);
    }

    @Override
    protected PlayerMessageState stateBefore() {
        return PlayerMessageState.SETTING_DATA_SOURCE;
    }

    @Override
    protected PlayerMessageState stateAfter() {
        return PlayerMessageState.DATA_SOURCE_SET;
    }
}
