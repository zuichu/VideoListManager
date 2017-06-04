package me.zuichu.videolistmanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.zuichu.videolistlibrary.manager.manager.VideoPlayerManager;
import me.zuichu.videolistmanager.entity.DirectLinkVideoItem;
import me.zuichu.videolistmanager.utils.VideoViewHolder;


/**
 * Created by office on 2017/5/23.
 */

public class VshowListAdapter extends RecyclerView.Adapter<VideoViewHolder> {
    private final VideoPlayerManager mVideoPlayerManager;
    private final List<DirectLinkVideoItem> mList;
    private final Context mContext;

    public VshowListAdapter(VideoPlayerManager videoPlayerManager, Context context, List<DirectLinkVideoItem> list) {
        mVideoPlayerManager = videoPlayerManager;
        mContext = context;
        mList = list;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        DirectLinkVideoItem videoItem = mList.get(position);
        videoItem.setList(mList);
        View resultView = videoItem.createView(viewGroup, mContext.getResources().getDisplayMetrics().widthPixels);
        return new VideoViewHolder(resultView);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder viewHolder, int position) {
        DirectLinkVideoItem videoItem = mList.get(position);
        videoItem.setList(mList);
        videoItem.update(position, viewHolder, mVideoPlayerManager);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setData(ArrayList<DirectLinkVideoItem> items) {
        mList.addAll(items);
        notifyDataSetChanged();
    }
}
