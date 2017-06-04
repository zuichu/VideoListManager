package me.zuichu.videolistlibrary.manager.player_messages;

import android.media.MediaPlayer;

import me.zuichu.videolistlibrary.manager.Config;
import me.zuichu.videolistlibrary.manager.PlayerMessageState;
import me.zuichu.videolistlibrary.manager.manager.VideoPlayerManagerCallback;
import me.zuichu.videolistlibrary.manager.ui.MediaPlayerWrapper;
import me.zuichu.videolistlibrary.manager.utils.Logger;

/**
 * This PlayerMessage calls {@link MediaPlayer#prepare()} on the instance that is used inside
 */
public class Prepare extends PlayerMessage {

    private static final boolean SHOW_LOGS = Config.SHOW_LOGS;
    private static final String TAG = Prepare.class.getSimpleName();

    private PlayerMessageState mResultPlayerMessageState;

    public Prepare(VideoPlayerManagerCallback callback) {
        super(callback);
    }

    @Override
    protected void performAction() {

        MediaPlayerWrapper.State resultOfPrepare = MediaPlayerWrapper.State.PREPARED;
        if (SHOW_LOGS) Logger.v(TAG, "resultOfPrepare " + resultOfPrepare);

        switch (resultOfPrepare) {
            case IDLE:
            case INITIALIZED:
            case PREPARING:
            case STARTED:
            case PAUSED:
            case STOPPED:
            case PLAYBACK_COMPLETED:
            case END:
                throw new RuntimeException("unhandled state " + resultOfPrepare);

            case PREPARED:
                mResultPlayerMessageState = PlayerMessageState.PREPARED;
                break;

            case ERROR:
                mResultPlayerMessageState = PlayerMessageState.ERROR;
                break;
        }
    }

    @Override
    protected PlayerMessageState stateBefore() {
        return PlayerMessageState.PREPARING;
    }

    @Override
    protected PlayerMessageState stateAfter() {
        return mResultPlayerMessageState;
    }
}
