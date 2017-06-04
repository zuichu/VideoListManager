package me.zuichu.videolistlibrary.manager.player_messages;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.FileDescriptor;

import me.zuichu.videolistlibrary.manager.manager.VideoPlayerManagerCallback;

/**
 * This PlayerMessage calls {@link MediaPlayer#setDataSource(FileDescriptor)} on the instance that is used inside
 */
public class SetAssetsDataSourceMessage extends SetDataSourceMessage {

    private final AssetFileDescriptor mAssetFileDescriptor;

    public SetAssetsDataSourceMessage(AssetFileDescriptor assetFileDescriptor, VideoPlayerManagerCallback callback) {
        super(callback);
        mAssetFileDescriptor = assetFileDescriptor;
    }

    @Override
    protected void performAction() {
    }
}
