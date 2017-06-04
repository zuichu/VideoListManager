package me.zuichu.videolistlibrary.manager.manager;


import me.zuichu.videolistlibrary.manager.PlayerMessageState;
import me.zuichu.videolistlibrary.manager.meta.MetaData;

/**
 * This callback is used by
 * to get and set data it needs
 */
public interface VideoPlayerManagerCallback {

    void setCurrentItem(MetaData currentItemMetaData);

    void setVideoPlayerState(PlayerMessageState playerMessageState);

    PlayerMessageState getCurrentPlayerState();
}
