package me.zuichu.videolistlibrary.manager.manager;


import me.zuichu.videolistlibrary.manager.meta.MetaData;

/**
 * This is basic interface for Items in Adapter of the list. Regardless of is it {@link android.widget.ListView}
 * or
 */
public interface VideoItem {
    void playNewVideo(MetaData currentItemMetaData, VideoPlayerManager<MetaData> videoPlayerManager);

    void stopPlayback(VideoPlayerManager videoPlayerManager);
}
