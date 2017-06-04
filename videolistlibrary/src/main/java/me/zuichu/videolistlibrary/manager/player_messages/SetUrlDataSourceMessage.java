package me.zuichu.videolistlibrary.manager.player_messages;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import me.zuichu.videolistlibrary.manager.manager.VideoPlayerManagerCallback;

/**
 * This PlayerMessage calls {@link MediaPlayer#setDataSource(Context, Uri)} on the instance that is used inside
 */
public class SetUrlDataSourceMessage extends SetDataSourceMessage {

    private final String mVideoUrl;

    public SetUrlDataSourceMessage(String videoUrl, VideoPlayerManagerCallback callback) {
        super(callback);
        mVideoUrl = videoUrl;
    }

    @Override
    protected void performAction() {

    }
}
