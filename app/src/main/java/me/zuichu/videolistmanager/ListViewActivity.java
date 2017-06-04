package me.zuichu.videolistmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zuichu.videolistlibrary.manager.manager.PlayerItemChangeListener;
import me.zuichu.videolistlibrary.manager.manager.SingleVideoPlayerManager;
import me.zuichu.videolistlibrary.manager.manager.VideoPlayerManager;
import me.zuichu.videolistlibrary.manager.meta.MetaData;
import me.zuichu.videolistlibrary.visiblelyUtils.calculator.DefaultSingleItemCalculatorCallback;
import me.zuichu.videolistlibrary.visiblelyUtils.calculator.ListItemsVisibilityCalculator;
import me.zuichu.videolistlibrary.visiblelyUtils.calculator.SingleListViewItemActiveCalculator;
import me.zuichu.videolistlibrary.visiblelyUtils.scroll_utils.ItemsPositionGetter;
import me.zuichu.videolistlibrary.visiblelyUtils.scroll_utils.ListViewItemPositionGetter;
import me.zuichu.videolistmanager.adapter.VshowListViewAdapter;
import me.zuichu.videolistmanager.base.BaseApplication;
import me.zuichu.videolistmanager.entity.DirectLinkVideoItem;
import me.zuichu.videolistmanager.utils.Utils;
import me.zuichu.videolistmanager.view.VshowVideoPlayer;

public class ListViewActivity extends AppCompatActivity {
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.fl_video)
    FrameLayout fl_video;
    private VshowListViewAdapter vshowListAdapter;
    private final ArrayList<DirectLinkVideoItem> mList = new ArrayList<>();
    private final ListItemsVisibilityCalculator listItemsVisibilityCalculator =
            new SingleListViewItemActiveCalculator(new DefaultSingleItemCalculatorCallback(), mList);
    private ItemsPositionGetter mItemsPositionGetter;
    private final VideoPlayerManager<MetaData> mVideoPlayerManager = new SingleVideoPlayerManager(new PlayerItemChangeListener() {
        @Override
        public void onPlayerItemChanged(MetaData metaData) {

        }
    });
    private int mScrollState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
    private ScreenBroadCast screenBroadCast;
    private VshowVideoPlayer vshowVideoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        vshowListAdapter = new VshowListViewAdapter(mVideoPlayerManager, ListViewActivity.this, mList);
        listview.setAdapter(vshowListAdapter);
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                mScrollState = scrollState;
                if (scrollState == SCROLL_STATE_IDLE && !mList.isEmpty()) {
                    listItemsVisibilityCalculator.onScrollStateIdle(mItemsPositionGetter, view.getFirstVisiblePosition(), view.getLastVisiblePosition());
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (!mList.isEmpty()) {
                    listItemsVisibilityCalculator.onScroll(mItemsPositionGetter, firstVisibleItem, visibleItemCount, mScrollState);
                }
            }
        });
        mItemsPositionGetter = new ListViewItemPositionGetter(listview);
        screenBroadCast = new ScreenBroadCast();
        IntentFilter intentFilter = new IntentFilter("ACTION_LANDSCAPE");
        intentFilter.addAction("ACTION_PORTRAIT");
        registerReceiver(screenBroadCast, intentFilter);
        vshowVideoPlayer = new VshowVideoPlayer(this);
        BaseApplication.vshowVideoPlayer = vshowVideoPlayer;
        initData();
    }

    private void initData() {
        mList.add(new DirectLinkVideoItem(this, "标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题", "http://baobab.wdjcdn.com/14564977406580.mp4", mVideoPlayerManager, R.mipmap.ic_launcher));
        mList.add(new DirectLinkVideoItem(this, "标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题2", "http://baobab.wdjcdn.com/14564977406580.mp4", mVideoPlayerManager, R.mipmap.ic_launcher));
        mList.add(new DirectLinkVideoItem(this, "标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题3", "http://baobab.wdjcdn.com/14564977406580.mp4", mVideoPlayerManager, R.mipmap.ic_launcher));
        mList.add(new DirectLinkVideoItem(this, "标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题4", "http://baobab.wdjcdn.com/14564977406580.mp4", mVideoPlayerManager, R.mipmap.ic_launcher));
        mList.add(new DirectLinkVideoItem(this, "标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题5", "http://baobab.wdjcdn.com/14564977406580.mp4", mVideoPlayerManager, R.mipmap.ic_launcher));
        mList.add(new DirectLinkVideoItem(this, "标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题6", "http://baobab.wdjcdn.com/14564977406580.mp4", mVideoPlayerManager, R.mipmap.ic_launcher));
        mList.add(new DirectLinkVideoItem(this, "标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题7", "http://baobab.wdjcdn.com/14564977406580.mp4", mVideoPlayerManager, R.mipmap.ic_launcher));
        mList.add(new DirectLinkVideoItem(this, "标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题8", "http://baobab.wdjcdn.com/14564977406580.mp4", mVideoPlayerManager, R.mipmap.ic_launcher));
        mList.add(new DirectLinkVideoItem(this, "标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题9", "http://baobab.wdjcdn.com/14564977406580.mp4", mVideoPlayerManager, R.mipmap.ic_launcher));
        mList.add(new DirectLinkVideoItem(this, "标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题10", "http://baobab.wdjcdn.com/14564977406580.mp4", mVideoPlayerManager, R.mipmap.ic_launcher));
        vshowListAdapter.setData(mList);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mList.isEmpty()) {
            listview.post(new Runnable() {
                @Override
                public void run() {
                    listItemsVisibilityCalculator.onScrollStateIdle(
                            mItemsPositionGetter,
                            listview.getFirstVisiblePosition(),
                            listview.getLastVisiblePosition());
                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mVideoPlayerManager.resetMediaPlayer();
    }

    class ScreenBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("ACTION_LANDSCAPE")) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                ListViewActivity.this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                Utils.hideNavKey(context);
//                fullScreen();
                fl_video.setVisibility(View.VISIBLE);
                ViewGroup parent = (ViewGroup) BaseApplication.getVshowVideoPlayer(ListViewActivity.this).getParent();
                if (parent != null) {
                    parent.removeAllViews();
                }
                mList.get(BaseApplication.position).removeItemPlayer();
                fl_video.addView(BaseApplication.getVshowVideoPlayer(ListViewActivity.this));
            } else if (action.equals("ACTION_PORTRAIT")) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                final WindowManager.LayoutParams attrs = ListViewActivity.this.getWindow().getAttributes();
                attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
                ListViewActivity.this.getWindow().setAttributes(attrs);
                ListViewActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                Utils.showNavKey(context, 0);
//                showToolbar();
                fl_video.removeAllViews();
                fl_video.setVisibility(View.GONE);
                mList.get(BaseApplication.position).setPlayer(BaseApplication.vshowVideoPlayer);
                if (!BaseApplication.vshowVideoPlayer.getVideoView().isPlaying()) {
                    BaseApplication.vshowVideoPlayer.setPause();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(screenBroadCast);
        if (BaseApplication.vshowVideoPlayer != null) {
            BaseApplication.vshowVideoPlayer.stop();
            BaseApplication.vshowVideoPlayer = null;
        }
    }
}
