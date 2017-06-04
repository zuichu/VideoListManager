package me.zuichu.videolistmanager.entity;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.view.View;

import com.bumptech.glide.Glide;

import me.zuichu.videolistlibrary.manager.Config;
import me.zuichu.videolistlibrary.manager.manager.VideoPlayerManager;
import me.zuichu.videolistlibrary.manager.meta.MetaData;
import me.zuichu.videolistlibrary.manager.utils.Logger;
import me.zuichu.videolistmanager.utils.VideoViewHolder;

public class AssetVideoItem extends BaseVideoItem {

    private static final String TAG = AssetVideoItem.class.getSimpleName();
    private static final boolean SHOW_LOGS = Config.SHOW_LOGS;

    private final AssetFileDescriptor mAssetFileDescriptor;
    private final String mTitle;

    private Glide mImageLoader;
    private final int mImageResource;
    private final Context context;

    public AssetVideoItem(Context context, String title, AssetFileDescriptor assetFileDescriptor, VideoPlayerManager<MetaData> videoPlayerManager, int imageResource) {
        super(videoPlayerManager);
        mTitle = title;
        mAssetFileDescriptor = assetFileDescriptor;
        mImageResource = imageResource;
        this.context = context;
    }

    @Override
    public void update(int position, final VideoViewHolder viewHolder, VideoPlayerManager videoPlayerManager) {
        if (SHOW_LOGS) Logger.v(TAG, "update, position " + position);
        viewHolder.tv_title.setText(mTitle);
        viewHolder.iv_img.setVisibility(View.VISIBLE);
        mImageLoader.with(context).load(mImageResource).into(viewHolder.iv_img);
    }


    @Override
    public void playNewVideo(MetaData currentItemMetaData, VideoPlayerManager<MetaData> videoPlayerManager) {
        videoPlayerManager.playNewVideo(currentItemMetaData, mAssetFileDescriptor);
    }

    @Override
    public void stopPlayback(VideoPlayerManager videoPlayerManager) {
        videoPlayerManager.stopAnyPlayback();
    }

    @Override
    public String toString() {
        return getClass() + ", mTitle[" + mTitle + "]";
    }
}
