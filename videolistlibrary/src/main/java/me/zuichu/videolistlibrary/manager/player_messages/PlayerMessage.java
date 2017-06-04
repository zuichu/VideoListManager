package me.zuichu.videolistlibrary.manager.player_messages;


import me.zuichu.videolistlibrary.manager.Config;
import me.zuichu.videolistlibrary.manager.PlayerMessageState;
import me.zuichu.videolistlibrary.manager.manager.VideoPlayerManagerCallback;
import me.zuichu.videolistlibrary.manager.utils.Logger;

/**
 * This is generic interface for PlayerMessage
 */
public abstract class PlayerMessage implements Message {

    private static final String TAG = PlayerMessage.class.getSimpleName();
    private static final boolean SHOW_LOGS = Config.SHOW_LOGS;
//    private final VshowVideoPlayerView mCurrentPlayer;
    private final VideoPlayerManagerCallback mCallback;

    public PlayerMessage( VideoPlayerManagerCallback callback) {
//        mCurrentPlayer = currentPlayer;
        mCallback = callback;
    }

    protected final PlayerMessageState getCurrentState(){
        return mCallback.getCurrentPlayerState();
    }

    @Override
    public final void polledFromQueue() {
        mCallback.setVideoPlayerState(stateBefore());
    }

    @Override
    public final void messageFinished() {
        mCallback.setVideoPlayerState(stateAfter());
    }

    public final void runMessage(){
        if(SHOW_LOGS) Logger.v(TAG, ">> runMessage, " + getClass().getSimpleName());
        performAction();
        if(SHOW_LOGS) Logger.v(TAG, "<< runMessage, " + getClass().getSimpleName());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    protected abstract void performAction();
    protected abstract PlayerMessageState stateBefore();
    protected abstract PlayerMessageState stateAfter();

}
