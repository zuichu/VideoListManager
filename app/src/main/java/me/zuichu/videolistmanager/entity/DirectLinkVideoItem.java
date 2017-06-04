package me.zuichu.videolistmanager.entity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import me.zuichu.videolistlibrary.manager.manager.VideoPlayerManager;
import me.zuichu.videolistlibrary.manager.meta.MetaData;
import me.zuichu.videolistmanager.base.BaseApplication;
import me.zuichu.videolistmanager.utils.VideoViewHolder;
import me.zuichu.videolistmanager.view.VshowVideoPlayer;

/**
 * Use this class if you have direct path to the video source
 */
public class DirectLinkVideoItem extends BaseVideoItem {

    private final String mDirectUrl;
    private final String mTitle;

    private Glide mImageLoader;
    private final int mImageResource;
    private final Context context;
    private VideoViewHolder videoViewHolder;
    private List<DirectLinkVideoItem> mList;

    public DirectLinkVideoItem(Context context, String title, String directUr, VideoPlayerManager videoPlayerManager, int imageResource) {
        super(videoPlayerManager);
        mDirectUrl = directUr;
        mTitle = title;
//        mImageLoader = imageLoader;
        mImageResource = imageResource;
        this.context = context;
    }

    @Override
    public void update(int position, VideoViewHolder viewHolder, VideoPlayerManager videoPlayerManager) {
        videoViewHolder = viewHolder;
        viewHolder.tv_title.setText(mTitle);
//        viewHolder.mCover.setVisibility(View.VISIBLE);
//        mImageLoader.with(context).load(mImageResource).into(viewHolder.mCover);
    }

    @Override
    public void playNewVideo(MetaData currentItemMetaData, VideoPlayerManager<MetaData> videoPlayerManager) {
        videoPlayerManager.playNewVideo(currentItemMetaData, mDirectUrl);
    }

    @Override
    public void stopPlayback(VideoPlayerManager videoPlayerManager) {
        videoPlayerManager.stopAnyPlayback();
    }

    @Override
    public void setActive(View newActiveView, int newActiveViewPosition) {
        super.setActive(newActiveView, newActiveViewPosition);
        if (newActiveViewPosition == 0 && mList.size() > 2) {
            mList.get(1).removePlayer();
            mList.get(2).removePlayer();
        } else if (newActiveViewPosition > 0 && mList.size() > 2 && newActiveViewPosition < mList.size() - 1) {
            mList.get(newActiveViewPosition - 1).removePlayer();
            mList.get(newActiveViewPosition + 1).removePlayer();
        } else if (newActiveViewPosition > 0 && mList.size() > 2 && newActiveViewPosition == mList.size() - 1) {
            mList.get(newActiveViewPosition - 1).removePlayer();
        }
        setPlayer(BaseApplication.getVshowVideoPlayer(context));
        BaseApplication.position = newActiveViewPosition;
    }

    public void setPlayer(VshowVideoPlayer vshowVideoPlayer) {
        ViewGroup parent = (ViewGroup) vshowVideoPlayer.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }
        videoViewHolder.fl_video.addView(vshowVideoPlayer);
        vshowVideoPlayer.start(mDirectUrl);
    }

    public void removePlayer() {
        if (videoViewHolder != null) {
            if (BaseApplication.vshowVideoPlayer != null) {
                BaseApplication.vshowVideoPlayer.stop();
            }
            BaseApplication.vshowVideoPlayer = null;
            videoViewHolder.fl_video.removeAllViews();
        }
    }

    public void removeItemPlayer() {
        if (videoViewHolder != null) {
            videoViewHolder.fl_video.removeAllViews();
        }
    }

    public void setList(List<DirectLinkVideoItem> list) {
        this.mList = list;
    }
}
