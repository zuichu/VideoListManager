package me.zuichu.videolistmanager.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zuichu.videolistmanager.R;
import me.zuichu.videolistmanager.base.BaseApplication;
import me.zuichu.videolistmanager.utils.Utils;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.widget.media.IRenderView;
import tv.danmaku.ijk.media.player.widget.media.IjkVideoView;
import tv.danmaku.ijk.media.player.widget.media.Options;

/**
 * Created by office on 2017/5/26.
 */
public class VshowVideoPlayer extends FrameLayout implements View.OnClickListener, IMediaPlayer.OnPreparedListener, IMediaPlayer.OnInfoListener, SeekBar.OnSeekBarChangeListener, IMediaPlayer.OnErrorListener {
    private Context context;
    private View rootView;
    @Bind(R.id.fl_root)
    FrameLayout fl_root;
    @Bind(R.id.ijkvideoview)
    IjkVideoView ijkVideoView;
    @Bind(R.id.iv_screen)
    ImageView iv_screen;
    @Bind(R.id.ll_control)
    LinearLayout ll_control;
    @Bind(R.id.iv_play)
    ImageView iv_play;
    @Bind(R.id.seekBar)
    SeekBar seekBar;
    @Bind(R.id.tv_current)
    TextView tv_current;
    @Bind(R.id.tv_duration)
    TextView tv_duration;
    @Bind(R.id.ll_loading)
    LinearLayout ll_loading;
    @Bind(R.id.tv_progress)
    TextView tv_progress;
    @Bind(R.id.rl_title)
    RelativeLayout rl_title;
    @Bind(R.id.iv_back)
    ImageView iv_back;
    @Bind(R.id.tv_title)
    TextView tv_title;
    private boolean max = false;
    private IjkMediaPlayer ijkMediaPlayer;
    private boolean isPause = false;
    private TimerTask timerTask;
    private Timer timer;
    private long duration = 0;
    private long seek = 0;
    private String url = "http://baobab.wdjcdn.com/14564977406580.mp4";

    public VshowVideoPlayer(Context context) {
        super(context);
        init(context);
    }

    public VshowVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VshowVideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        rootView = LayoutInflater.from(context).inflate(R.layout.layout_vshowvideoplayer, this);
        ButterKnife.bind(rootView);
        iv_screen.setOnClickListener(this);
        iv_play.setOnClickListener(this);
        fl_root.setOnClickListener(this);
        iv_screen.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
        iv_back.setOnClickListener(this);
    }

    public void start(String videoUrl) {
//        this.url = videoUrl;
        Options options = new Options();
        options.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 0);//是否硬解码，0软解码，1硬解码
        options.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 1);// 是否自动启动播放，0不是，1是
        ijkVideoView.setOptions(options);
        ijkVideoView.setCurrentAspectRatio(IRenderView.AR_ASPECT_FIT_PARENT);
        ijkVideoView.setOnPreparedListener(this);
        ijkVideoView.setOnInfoListener(this);
        ijkVideoView.setOnErrorListener(this);
        ijkVideoView.setVideoPath(url);
        ijkVideoView.setOnPreparedListener(this);
        ijkVideoView.start();
    }

    public void stop() {
        ijkVideoView.stopPlayback();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_play:
                if (ijkVideoView.isPlaying()) {
                    ijkVideoView.pause();
                    isPause = true;
                    iv_play.setImageResource(R.mipmap.icon_play);
                } else {
                    isPause = false;
                    ijkVideoView.start();
                    iv_play.setImageResource(R.mipmap.icon_pause);
                }
                break;
            case R.id.fl_root:
                if (ll_control.getVisibility() == View.VISIBLE) {
                    ll_control.setVisibility(View.INVISIBLE);
                    rl_title.setVisibility(View.INVISIBLE);
                } else {
                    ll_control.setVisibility(View.VISIBLE);
                    if (Utils.getScreenOrientation((Activity) context) == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                        rl_title.setVisibility(View.VISIBLE);
                    }
                    Utils.setControlLive(ll_control, rl_title);
                }
                break;
            case R.id.iv_screen:
                if (!max) {
                    BaseApplication.vshowVideoPlayer = this;
                    Intent intent = new Intent("ACTION_LANDSCAPE");
                    context.sendBroadcast(intent);
                    rl_title.setVisibility(View.VISIBLE);
                } else {
                    BaseApplication.vshowVideoPlayer = this;
                    Intent intent = new Intent("ACTION_PORTRAIT");
                    context.sendBroadcast(intent);
                    rl_title.setVisibility(View.GONE);
                }
                max = !max;
                break;
            case R.id.iv_back:
                BaseApplication.vshowVideoPlayer = this;
                Intent intent = new Intent("ACTION_PORTRAIT");
                context.sendBroadcast(intent);
                rl_title.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra) {
        switch (what) {
            case IjkMediaPlayer.MEDIA_INFO_BUFFERING_START:
                ll_loading.setVisibility(View.VISIBLE);
                tv_progress.setText("缓冲中..." + Formatter.formatFileSize(context, ijkMediaPlayer.getTcpSpeed()) + "/S");
                break;
            case IjkMediaPlayer.MEDIA_INFO_BUFFERING_END:
            case IjkMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                ll_loading.setVisibility(View.GONE);
                Utils.setControlLive(ll_control, rl_title);
                if (isPause) {
                    ijkVideoView.pause();
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        ijkMediaPlayer = (IjkMediaPlayer) iMediaPlayer;
        timerTask = null;
        timer = null;
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (ijkVideoView != null && !isPause) {
                    Message msg = new Message();
                    msg.what = 0;
                    handler.sendMessage(msg);
                }
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0, 1000);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            if (ijkMediaPlayer != null) {
                seek = (progress * ijkMediaPlayer.getDuration()) / 100;
                tv_current.setText(Utils.generateTime(seek));
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (ijkMediaPlayer != null) {
            seek = (seekBar.getProgress() * ijkMediaPlayer.getDuration()) / 100;
            ijkVideoView.seekTo((int) seek);
            if (!ijkVideoView.isPlaying()) {
                ll_loading.setVisibility(View.VISIBLE);
                ijkVideoView.start();
                iv_play.setImageResource(R.mipmap.icon_pause);
                isPause = false;
            }
            tv_current.setText(Utils.generateTime(seek));
            if (seekBar.getProgress() == 100) {
                setSeekToBegin();
            }
        }
    }

    public void setSeekToBegin() {
        if (ijkMediaPlayer != null) {
            seekBar.setProgress(0);
            ijkVideoView.stopPlayback();
            iv_play.setImageResource(R.mipmap.icon_play);
            tv_current.setText("00:00");
            seek = 0;
            isPause = true;
        }
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (ijkMediaPlayer != null) {
                        tv_current.setText(Utils.generateTime(ijkMediaPlayer.getCurrentPosition()));
                        duration = ijkMediaPlayer.getDuration();
                        if (duration != 0) {
                            seekBar.setProgress((int) ((ijkMediaPlayer.getCurrentPosition() * 100) / duration));
                            tv_duration.setText(Utils.generateTime(duration));
                        }
                    }
                    break;
            }
            return false;
        }
    });

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int what, int extra) {
        switch (what) {
            case IjkMediaPlayer.MEDIA_ERROR_MALFORMED:
                Utils.showToast(context, "MEDIA_ERROR_MALFORMED !");
                break;
            case IjkMediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Utils.showToast(context, "MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK!");
                break;
            case IjkMediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                Utils.showToast(context, "MEDIA_ERROR_UNSUPPORTED!");
                reLink();
                break;
            case IjkMediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Utils.showToast(context, "MEDIA_ERROR_SERVER_DIED !");
                break;
            case IjkMediaPlayer.MEDIA_ERROR_IO:
                Utils.showToast(context, "MEDIA_ERROR_IO !");
                reLink();
                break;
            case IjkMediaPlayer.MEDIA_INFO_TIMED_TEXT_ERROR:
                Utils.showToast(context, "MEDIA_INFO_TIMED_TEXT_ERROR !");
                break;
            case IjkMediaPlayer.MEDIA_ERROR_TIMED_OUT:
                Utils.showToast(context, "链接超时");
                reLink();
                break;
            case IjkMediaPlayer.MEDIA_ERROR_UNKNOWN:
            default:
                Utils.showToast(context, "获取不到视频源");
                reLink();
                break;
        }
        return true;
    }

    public void reLink() {
        isPause = false;
        ijkVideoView.releaseWithoutStop();
        ijkVideoView.setVideoPath(url);
        ll_loading.setVisibility(View.VISIBLE);
        ijkVideoView.start();
    }

    public IjkVideoView getVideoView() {
        return ijkVideoView;
    }

    public void setPause() {
        iv_play.setImageResource(R.mipmap.icon_play);
        ijkVideoView.pause();
    }
}
