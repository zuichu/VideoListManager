package me.zuichu.videolistlibrary.manager.manager;


import me.zuichu.videolistlibrary.manager.meta.MetaData;

/**
 * Created by danylo.volokh on 06.01.2016.
 */
public interface PlayerItemChangeListener {
    void onPlayerItemChanged(MetaData currentItemMetaData);
}
