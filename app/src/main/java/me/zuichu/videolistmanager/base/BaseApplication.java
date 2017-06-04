package me.zuichu.videolistmanager.base;

import android.app.Application;
import android.content.Context;

import me.zuichu.videolistmanager.view.VshowVideoPlayer;

/**
 * Created by office on 2017/5/31.
 */
public class BaseApplication extends Application {
    public static VshowVideoPlayer vshowVideoPlayer;
    public static int position = 0;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static VshowVideoPlayer getVshowVideoPlayer(Context context) {
        if (vshowVideoPlayer == null) {
            vshowVideoPlayer = new VshowVideoPlayer(context);
        }
        return vshowVideoPlayer;
    }
}
