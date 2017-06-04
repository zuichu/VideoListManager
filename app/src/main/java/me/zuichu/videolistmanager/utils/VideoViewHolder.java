package me.zuichu.videolistmanager.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import me.zuichu.videolistmanager.R;


public class VideoViewHolder extends RecyclerView.ViewHolder {
    public FrameLayout fl_video;
    public final TextView tv_title;
    public final ImageView iv_img;
    public final TextView tv_name;
    public final TextView tv_time;
    public final TextView tv_guanzhu;

    public VideoViewHolder(View view) {
        super(view);
        fl_video = (FrameLayout) view.findViewById(R.id.fl_video);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_time = (TextView) view.findViewById(R.id.tv_time);
        tv_guanzhu = (TextView) view.findViewById(R.id.tv_guanzhu);
        iv_img = (ImageView) view.findViewById(R.id.iv_img);
    }
}
