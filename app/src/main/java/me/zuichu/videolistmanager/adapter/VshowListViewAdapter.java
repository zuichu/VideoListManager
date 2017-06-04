package me.zuichu.videolistmanager.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import me.zuichu.videolistlibrary.manager.manager.VideoPlayerManager;
import me.zuichu.videolistmanager.entity.DirectLinkVideoItem;
import me.zuichu.videolistmanager.utils.VideoViewHolder;

/**
 * Created by office on 2017/5/24.
 */
public class VshowListViewAdapter extends BaseAdapter {
    private final VideoPlayerManager mVideoPlayerManager;
    private final List<DirectLinkVideoItem> mList;
    private final Context mContext;

    public VshowListViewAdapter(VideoPlayerManager videoPlayerManager, Context context, List<DirectLinkVideoItem> list) {
        mVideoPlayerManager = videoPlayerManager;
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DirectLinkVideoItem videoItem = mList.get(position);
        videoItem.setList(mList);
        View resultView;
        if (convertView == null) {
            resultView = videoItem.createView(parent, mContext.getResources().getDisplayMetrics().widthPixels);
        } else {
            resultView = convertView;
        }
        videoItem.update(position, (VideoViewHolder) resultView.getTag(), mVideoPlayerManager);
        return resultView;
    }

    public void setData(ArrayList<DirectLinkVideoItem> items) {
        mList.addAll(items);
        notifyDataSetChanged();
    }
}
