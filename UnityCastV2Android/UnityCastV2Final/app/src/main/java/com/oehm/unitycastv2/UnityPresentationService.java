package com.oehm.unitycastv2;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.media.MediaRouter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.google.android.gms.cast.CastPresentation;
import com.google.android.gms.cast.CastRemoteDisplayLocalService;
import com.unity3d.player.UnityPlayer;

public class UnityPresentationService extends CastRemoteDisplayLocalService
{

    private static final String TAG = "UnityPresentationServic";


    public UnityPlayer mUnityPlayer;

    private CastPresentation mPresentation;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onCreatePresentation(Display display) {
        createPresentation(display);
    }

    @Override
    public void onDismissPresentation() {
        dismissPresentation();
    }

    private void dismissPresentation() {

        if (mPresentation != null) {
            mPresentation.dismiss();
            mPresentation = null;
        }
    }

    private void createPresentation(Display display) {

        dismissPresentation();

        mPresentation = new UnityCastPresentation(this, display);

        try {
            mPresentation.show();
        } catch (WindowManager.InvalidDisplayException ex) {
            Log.e(TAG, "Unable to show presentation, display was removed.", ex);
            dismissPresentation();
        }
    }


    /**
     * The presentation to show on the first screen (the TV).
     * <p>
     * Note that this display may have different metrics from the display on
     * which the main activity is showing so we must be careful to use the
     * presentation's own {@link Context} whenever we load resources.
     * </p>
     */
    private class UnityCastPresentation extends CastPresentation {

        private final String TAG = "UnityCastPresentation";

        public UnityCastPresentation(Context context, Display display) {
            super(context, display);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //set Content on tv
            if(mUnityPlayer != null){

                setContentView(R.layout.presentation_main);

                //attach unity to the surface
                SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceViewPresentation);
                surfaceView.getHolder().addCallback(new UnitySurfaceHolderCallback(mUnityPlayer, 1));


                Log.d(TAG, "Chromecast should display stuff.");

            }
            else {

                setContentView(R.layout.presentation_main);
                Log.e(TAG, "unity player was not assigned in time.");
            }

        }

        @Override
        protected void onStart() {
            super.onStart();

//            mUnityPlayer.resume();
//            mUnityPlayer.windowFocusChanged(true);
        }

        @Override
        protected void onStop() {
//            mUnityPlayer.quit();
            super.onStop();
        }

        @Override public void onWindowFocusChanged(boolean hasFocus)
        {
            super.onWindowFocusChanged(hasFocus);
//            mUnityPlayer.windowFocusChanged(hasFocus);
        }
    }
}
