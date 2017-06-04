package me.zuichu.videolistlibrary.manager;


import me.zuichu.videolistlibrary.manager.manager.VideoPlayerManagerCallback;
import me.zuichu.videolistlibrary.manager.meta.MetaData;
import me.zuichu.videolistlibrary.manager.player_messages.PlayerMessage;

public class SetNewViewForPlayback extends PlayerMessage {

    private final MetaData mCurrentItemMetaData;
//    private final VshowVideoPlayerView mCurrentPlayer;
    private final VideoPlayerManagerCallback mCallback;

    public SetNewViewForPlayback(MetaData currentItemMetaData, VideoPlayerManagerCallback callback) {
        super( callback);
        mCurrentItemMetaData = currentItemMetaData;
//        mCurrentPlayer = vshowVideoPlayerView;
        mCallback = callback;
    }

    @Override
    public String toString() {
        return SetNewViewForPlayback.class.getSimpleName() + ", mCurrentPlayer ";
    }

    @Override
    protected void performAction() {
        mCallback.setCurrentItem(mCurrentItemMetaData);
    }

    @Override
    protected PlayerMessageState stateBefore() {
        return PlayerMessageState.SETTING_NEW_PLAYER;
    }

    @Override
    protected PlayerMessageState stateAfter() {
        return PlayerMessageState.IDLE;
    }
}
