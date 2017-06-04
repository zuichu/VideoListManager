package me.zuichu.videolistlibrary.manager.manager;

import android.content.res.AssetFileDescriptor;

import me.zuichu.videolistlibrary.manager.meta.MetaData;

/**
 * This is a general interface for VideoPlayerManager
 * It supports :
 * 1. Start playback of new video by calling:
 * 2. Stop existing playback. {@link #stopAnyPlayback()}
 * 3. Reset Media Player if it's no longer needed. {@link #resetMediaPlayer()}
 */
public interface VideoPlayerManager<T extends MetaData> {

    /**
     * Call it if you have direct url or path to video source
     * @param metaData - optional Meta Data
     * @param videoUrl - the link to the video source
     */
    void playNewVideo(T metaData, String videoUrl);

    /**
     * Call it if you have video source in assets directory
     * @param metaData - optional Meta Data
     * @param assetFileDescriptor -The asset descriptor of the video file
     */
    void playNewVideo(T metaData, AssetFileDescriptor assetFileDescriptor);

    /**
     * Call it if you need to stop any playback that is currently playing
     */
    void stopAnyPlayback();

    /**
     * Call it if you no longer need the player
     */
    void resetMediaPlayer();
}
