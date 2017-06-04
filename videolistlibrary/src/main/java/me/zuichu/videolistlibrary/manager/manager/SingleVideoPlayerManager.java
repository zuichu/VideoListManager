package me.zuichu.videolistlibrary.manager.manager;

import android.content.res.AssetFileDescriptor;

import java.util.Arrays;

import me.zuichu.videolistlibrary.manager.Config;
import me.zuichu.videolistlibrary.manager.MessagesHandlerThread;
import me.zuichu.videolistlibrary.manager.PlayerMessageState;
import me.zuichu.videolistlibrary.manager.SetNewViewForPlayback;
import me.zuichu.videolistlibrary.manager.meta.MetaData;
import me.zuichu.videolistlibrary.manager.player_messages.ClearPlayerInstance;
import me.zuichu.videolistlibrary.manager.player_messages.CreateNewPlayerInstance;
import me.zuichu.videolistlibrary.manager.player_messages.Prepare;
import me.zuichu.videolistlibrary.manager.player_messages.Release;
import me.zuichu.videolistlibrary.manager.player_messages.Reset;
import me.zuichu.videolistlibrary.manager.player_messages.SetAssetsDataSourceMessage;
import me.zuichu.videolistlibrary.manager.player_messages.SetUrlDataSourceMessage;
import me.zuichu.videolistlibrary.manager.player_messages.Start;
import me.zuichu.videolistlibrary.manager.player_messages.Stop;
import me.zuichu.videolistlibrary.manager.ui.MediaPlayerWrapper;
import me.zuichu.videolistlibrary.manager.utils.Logger;

/**
 * This implementation of {@link VideoPlayerManager} is designed to manage a single video playback.
 * If new video should start playback this implementation previously stops currently playing video
 * and then starts new playback.
 */
public class SingleVideoPlayerManager implements VideoPlayerManager<MetaData>, VideoPlayerManagerCallback, MediaPlayerWrapper.MainThreadMediaPlayerListener {

    private static final String TAG = SingleVideoPlayerManager.class.getSimpleName();
    private static final boolean SHOW_LOGS = Config.SHOW_LOGS;

    /**
     * This is a handler thread that is used to process Player messages.
     */
    private final MessagesHandlerThread mPlayerHandler = new MessagesHandlerThread();

    /**
     * When {@link SingleVideoPlayerManager} actually switches the player
     * (Switching the player can take a while: we have to stop previous player then start another),
     * then it calls
     * To notify that player was switched.
     */
    private final PlayerItemChangeListener mPlayerItemChangeListener;

    private PlayerMessageState mCurrentPlayerState = PlayerMessageState.IDLE;

    public SingleVideoPlayerManager(PlayerItemChangeListener playerItemChangeListener) {
        mPlayerItemChangeListener = playerItemChangeListener;
    }

    /**
     * Call it if you have direct url or path to video source
     * <p/>
     * The logic is following:
     * 1. Stop queue processing to have consistent state of queue when posting new messages
     * 2. Check if current player is active.
     * 3. If it is active and already playing current video we do nothing
     * 4. If not active then start new playback
     * 5. Resume stopped queue
     *
     * @param currentItemMetaData
     * @param videoUrl             - the link to the video source
     */
    @Override
    public void playNewVideo(MetaData currentItemMetaData, String videoUrl) {

        /** 1. */
        mPlayerHandler.pauseQueueProcessing(TAG);

        /** 2. */

            startNewPlayback(currentItemMetaData, videoUrl);

        /** 5. */
        mPlayerHandler.resumeQueueProcessing(TAG);

    }

    /**
     * Call it if you have direct url or path to video source
     * <p/>
     * The logic is following:
     * 1. Stop queue processing to have consistent state of queue when posting new messages
     * 2. Check if current player is active.
     * 3. If it is active and already playing current video we do nothing
     * 4. If not active then start new playback
     * 5. Resume stopped queue
     * <p/>
     * This method is basically a copy-paste of
     * TODO: define a better interface to divide these two methods
     *
     * @param currentItemMetaData
     * @param assetFileDescriptor  - the asset descriptor for source file
     */
    @Override
    public void playNewVideo(MetaData currentItemMetaData, AssetFileDescriptor assetFileDescriptor) {

        if (SHOW_LOGS) Logger.v(TAG, "playNewVideo, currentItemMetaData " + currentItemMetaData);

        /** 1. */
        mPlayerHandler.pauseQueueProcessing(TAG);

        /** 2. */
            startNewPlayback(currentItemMetaData, assetFileDescriptor);

        /** 5. */
        mPlayerHandler.resumeQueueProcessing(TAG);
    }

    private boolean isInPlaybackState() {
        boolean isPlaying = mCurrentPlayerState == PlayerMessageState.STARTED || mCurrentPlayerState == PlayerMessageState.STARTING;
        if (SHOW_LOGS) Logger.v(TAG, "isInPlaybackState, " + isPlaying);
        return isPlaying;
    }

    /**
     * In order to start new playback we have to do few steps in specific order:
     * <p/>
     * Before calling this method the queue processing should be stopped
     * 1. Clear all pending messages from the queue
     * 2. Post messages that will Stop, Reset, Release and clear current instance of Video Player
     * "Clear instance" means removing instance of {@link android.media.MediaPlayer} and not the
     * 3. Set new view player of which become active.
     * 4. Post messages to start new playback
     *
     * @param currentItemMetaData
     * @param assetFileDescriptor
     */
    private void startNewPlayback(MetaData currentItemMetaData, AssetFileDescriptor assetFileDescriptor) {
        // set listener for new player
        // TODO: find a place when we can remove this listener.

        if (SHOW_LOGS)
            Logger.v(TAG, "startNewPlayback, mCurrentPlayerState " + mCurrentPlayerState);

        /** 1. */
        mPlayerHandler.clearAllPendingMessages(TAG);
        /** 2. */
        stopResetReleaseClearCurrentPlayer();
        /** 3. */
        setNewViewForPlayback(currentItemMetaData);
        /** 4. */
        startPlayback(assetFileDescriptor);
    }

    /**
     * This is copy paste of
     * The difference is that this method uses AssetFileDescriptor instead of direct path
     */
    private void startNewPlayback(MetaData currentItemMetaData, String videoUrl) {
        // set listener for new player
        // TODO: find a place when we have to remove this listener.
//        vshowVideoPlayerView.addMediaPlayerListener(this);
        if (SHOW_LOGS)
            Logger.v(TAG, "startNewPlayback, mCurrentPlayerState " + mCurrentPlayerState);

        mPlayerHandler.clearAllPendingMessages(TAG);

        stopResetReleaseClearCurrentPlayer();
        setNewViewForPlayback(currentItemMetaData);
        startPlayback(videoUrl);
    }

    /**
     * This method stops playback if one exists.
     */
    @Override
    public void stopAnyPlayback() {
        if (SHOW_LOGS)
            Logger.v(TAG, ">> stopAnyPlayback, mCurrentPlayerState " + mCurrentPlayerState);

        mPlayerHandler.pauseQueueProcessing(TAG);
        if (SHOW_LOGS) Logger.v(TAG, "stopAnyPlayback, mCurrentPlayerState " + mCurrentPlayerState);

        mPlayerHandler.clearAllPendingMessages(TAG);
        stopResetReleaseClearCurrentPlayer();

        mPlayerHandler.resumeQueueProcessing(TAG);

        if (SHOW_LOGS)
            Logger.v(TAG, "<< stopAnyPlayback, mCurrentPlayerState " + mCurrentPlayerState);
    }

    /**
     * This method stops current playback and resets MediaPlayer.
     * Call it when you no longer need it.
     */
    @Override
    public void resetMediaPlayer() {
        if (SHOW_LOGS)
            Logger.v(TAG, ">> resetMediaPlayer, mCurrentPlayerState " + mCurrentPlayerState);


        mPlayerHandler.pauseQueueProcessing(TAG);
        if (SHOW_LOGS)
            Logger.v(TAG, "resetMediaPlayer, mCurrentPlayerState " + mCurrentPlayerState);
        mPlayerHandler.clearAllPendingMessages(TAG);
        resetReleaseClearCurrentPlayer();

        mPlayerHandler.resumeQueueProcessing(TAG);

        if (SHOW_LOGS)
            Logger.v(TAG, "<< resetMediaPlayer, mCurrentPlayerState " + mCurrentPlayerState);
    }

    /**
     * This method posts a set of messages to
     * to start new playback
     *
     * @param videoUrl             - a source path
     */
    private void startPlayback( String videoUrl) {
        if (SHOW_LOGS) Logger.v(TAG, "startPlayback");

        mPlayerHandler.addMessages(Arrays.asList(
                new CreateNewPlayerInstance(this),
                new SetUrlDataSourceMessage(videoUrl, this),
                new Prepare(this),
                new Start(this)
        ));
    }

    private void startPlayback(AssetFileDescriptor assetFileDescriptor) {
        if (SHOW_LOGS) Logger.v(TAG, "startPlayback");

        mPlayerHandler.addMessages(Arrays.asList(
                new CreateNewPlayerInstance(this),
                new SetAssetsDataSourceMessage(assetFileDescriptor, this),
                new Prepare(this),
                new Start(this)
        ));
    }

    /**
     * This method posts a message that will eventually call
     * When current player is stopped and new player is about to be active this message sets new player
     */
    private void setNewViewForPlayback(MetaData currentItemMetaData) {
        mPlayerHandler.addMessage(new SetNewViewForPlayback(currentItemMetaData, this));
    }

    /**
     * This method posts a set of messages to
     * in order to stop current playback
     */
    private void stopResetReleaseClearCurrentPlayer() {

        switch (mCurrentPlayerState) {
            case SETTING_NEW_PLAYER:
            case IDLE:

            case CREATING_PLAYER_INSTANCE:
            case PLAYER_INSTANCE_CREATED:

            case CLEARING_PLAYER_INSTANCE:
            case PLAYER_INSTANCE_CLEARED:
                // in these states player is stopped
                break;
            case INITIALIZED:
            case PREPARING:
            case PREPARED:
            case STARTING:
            case STARTED:
            case PAUSING:
            case PAUSED:
                mPlayerHandler.addMessage(new Stop(this));
                //FALL-THROUGH

            case SETTING_DATA_SOURCE:
            case DATA_SOURCE_SET:
                /** if we don't reset player in this state, will will get 0;0 from {@link android.media.MediaPlayer.OnVideoSizeChangedListener}.
                 *  And this TextureView will never recover */
            case STOPPING:
            case STOPPED:
            case ERROR: // reset if error
            case PLAYBACK_COMPLETED:
                mPlayerHandler.addMessage(new Reset(this));
                //FALL-THROUGH
            case RESETTING:
            case RESET:
                mPlayerHandler.addMessage(new Release(this));
                //FALL-THROUGH
            case RELEASING:
            case RELEASED:
                mPlayerHandler.addMessage(new ClearPlayerInstance(this));

                break;
            case END:
                throw new RuntimeException("unhandled " + mCurrentPlayerState);
        }
    }

    private void resetReleaseClearCurrentPlayer() {

        switch (mCurrentPlayerState) {
            case SETTING_NEW_PLAYER:
            case IDLE:

            case CREATING_PLAYER_INSTANCE:
            case PLAYER_INSTANCE_CREATED:

            case SETTING_DATA_SOURCE:
            case DATA_SOURCE_SET:

            case CLEARING_PLAYER_INSTANCE:
            case PLAYER_INSTANCE_CLEARED:
                break;
            case INITIALIZED:
            case PREPARING:
            case PREPARED:
            case STARTING:
            case STARTED:
            case PAUSING:
            case PAUSED:
            case STOPPING:
            case STOPPED:
            case ERROR: // reset if error
            case PLAYBACK_COMPLETED:
                mPlayerHandler.addMessage(new Reset(this));
                //FALL-THROUGH
            case RESETTING:
            case RESET:
                mPlayerHandler.addMessage(new Release(this));
                //FALL-THROUGH
            case RELEASING:
            case RELEASED:
                mPlayerHandler.addMessage(new ClearPlayerInstance(this));

                break;
            case END:
                throw new RuntimeException("unhandled " + mCurrentPlayerState);
        }
    }

    /**
     * This method is called by {@link SetNewViewForPlayback} message when new player becomes active.
     * Then it passes that knowledge to the {@link #mPlayerItemChangeListener}
     */
    @Override
    public void setCurrentItem(MetaData currentItemMetaData) {
        if (SHOW_LOGS) Logger.v(TAG, ">> onPlayerItemChanged");

        mPlayerItemChangeListener.onPlayerItemChanged(currentItemMetaData);

        if (SHOW_LOGS) Logger.v(TAG, "<< onPlayerItemChanged");
    }

    /**
     * This method is called by
     * When video player state changes.
     * @param playerMessageState   - new state of player. The state is then used when posting new messages
     */
    @Override
    public void setVideoPlayerState( PlayerMessageState playerMessageState) {
        mCurrentPlayerState = playerMessageState;
    }

    @Override
    public PlayerMessageState getCurrentPlayerState() {
        if (SHOW_LOGS)
            Logger.v(TAG, "getCurrentPlayerState, mCurrentPlayerState " + mCurrentPlayerState);
        return mCurrentPlayerState;
    }

    @Override
    public void onVideoSizeChangedMainThread(int width, int height) {
    }

    @Override
    public void onVideoPreparedMainThread() {
    }

    @Override
    public void onVideoCompletionMainThread() {
        mCurrentPlayerState = PlayerMessageState.PLAYBACK_COMPLETED;
    }

    @Override
    public void onErrorMainThread(int what, int extra) {
        if (SHOW_LOGS) Logger.v(TAG, "onErrorMainThread, what " + what + ", extra " + extra);

        /** if error happen during playback, we need to set error state.
         * Because we cannot run some messages in Error state
         for example {@link com.volokh.danylo.videolist.manager.player_messages.Stop}*/
        mCurrentPlayerState = PlayerMessageState.ERROR;
    }

    @Override
    public void onBufferingUpdateMainThread(int percent) {
    }

    @Override
    public void onVideoStoppedMainThread() {

    }

    @Override
    public void onVideoInfo(int what, int extra) {

    }
}
