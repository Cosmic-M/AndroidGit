package com.example.cosmic_m.footballFakts;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Cosmic_M on 30.05.2017.
 */

public class ThumbnailDownloader<T> extends HandlerThread {
    private static final String TAG = "TT";
    private static final int SHOULD_DOWNLOAD = 0;
    private static final int FOR_LIST_ALL_MATCHES = 1;
    private ConcurrentMap<T, String> mRequestMap = new ConcurrentHashMap<>();
    private ConcurrentMap<T, String[]> mRM = new ConcurrentHashMap<>();
    private Handler mRequestHandler;
    private Handler mResponseHandler;
    private ThumbnailDownloadListener<T> mThumbnailDownloadListener;
    private DoubleThumbnailDownloadListener<T> mDoubleThumbnailDownloadListener;

    public interface ThumbnailDownloadListener<T> {
        void onThumbnailDownloaded(T target, Drawable drawable);
    }

    public interface DoubleThumbnailDownloadListener<T> {
        void onDoubleThumbnailDownloaded(T target, Drawable drawable1, Drawable drawable2);
    }

    public void setDoubleThumbnailDownloadListener(DoubleThumbnailDownloadListener<T> listener) {
        mDoubleThumbnailDownloadListener = listener;
    }

    public ThumbnailDownloader(Handler responseHandler) {
        super(TAG);
        mResponseHandler = responseHandler;
    }

    public void setThumbnailDownloadListener(ThumbnailDownloadListener<T> listener) {
        mThumbnailDownloadListener = listener;
    }

    public void newQueueThumbnail(T target, String url) {
        Log.i(TAG, "Got a URL " + url);
        if (url == null) {
            mRequestMap.remove(target);
        } else {
            mRequestMap.put(target, url);
            mRequestHandler.obtainMessage(SHOULD_DOWNLOAD, target).sendToTarget();
        }
    }



    public void queueThumbnail(T target, String url) {
        Log.i(TAG, "Got a URL " + url);
        if (url == null) {
            mRequestMap.remove(target);
        } else {
            mRequestMap.put(target, url);
            mRequestHandler.obtainMessage(SHOULD_DOWNLOAD, target).sendToTarget();
        }
    }

    public void queueThumbnail(T target, String urlHome, String urlAway) {
        Log.i(TAG, "Got a URL " + urlHome + " " + urlAway);
        if (urlHome == null & urlAway ==null) {
            mRM.remove(target);
        } else {
            mRM.put(target, new String[]{urlHome, urlAway});
            mRequestHandler.obtainMessage(FOR_LIST_ALL_MATCHES, target).sendToTarget();
        }
    }

    @Override
    public void onLooperPrepared() {
        mRequestHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == SHOULD_DOWNLOAD) {
                    T target = (T) msg.obj;
                    handleRequest(target);
                }
                else if (msg.what == FOR_LIST_ALL_MATCHES) {
                    T target = (T) msg.obj;
                    handleReq(target);
                }
            }
        };
    }//public void onLooperPrepared()

    /*
    * this method used by the LeagueStandingFragment class
     */
    private void handleRequest(final T target) {
        final String url = mRequestMap.get(target);
        if (url == null) {
            return;
        }
        Thread t = new Thread() {
            @Override
            public void run() {
                try

                {
                    //final Drawable drawable = new EmblemClubFetcher().getUrlBytes(url);
                    final Drawable drawable = null;
                    Log.i(TAG, "bitmap created");

                    mResponseHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mRequestMap.get(target) != url) {
                                return;
                            }
                            mRequestMap.remove(target);
                            mThumbnailDownloadListener.onThumbnailDownloaded(target, drawable);
                        }
                    });
                } catch (
                        Exception exc)

                {
                    exc.printStackTrace();
                }
            }
        };
        t.start();
    }//private void handleRequest(final T target)

    /*
* this method used by the ListAllMatchesOfTeamFragment class
 */
    private void handleReq(final T target) {
        String[] array = mRM.get(target);
        final String homeTeam = array[0];
        final String awayTeam = array[1];
        if (homeTeam == null & awayTeam == null) {
            return;
        }
        try {
            //final Drawable drawableHomeTeam = new EmblemClubFetcher().getUrlBytes(homeTeam);
            final Drawable drawableHomeTeam = null;
            Log.i(TAG, String.valueOf(drawableHomeTeam == null));
            //final Drawable drawableAwayTeam = new EmblemClubFetcher().getUrlBytes(awayTeam);
            final Drawable drawableAwayTeam = null;
            Log.i(TAG, String.valueOf(drawableAwayTeam == null));
            Log.i(TAG, "bitmap created");

            mResponseHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mRM.get(target)[0] != homeTeam && mRM.get(target)[1] != awayTeam) {
                        Log.i(TAG, "will work return!");
                        return;
                    }
                    mRM.remove(target);
                    mDoubleThumbnailDownloadListener
                            .onDoubleThumbnailDownloaded(target, drawableHomeTeam, drawableAwayTeam);
                }
            });
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }//private void handleRequest(final T target)

    public void clearQueue(){
        mRequestHandler.removeMessages(SHOULD_DOWNLOAD);
    }
}
