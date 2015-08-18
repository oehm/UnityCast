/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.oehm.unitycastv2;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.support.v7.media.MediaRouter.RouteInfo;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.CastMediaControlIntent;
import com.google.android.gms.cast.CastRemoteDisplayLocalService;
import com.google.android.gms.common.api.Status;
import com.unity3d.player.UnityPlayer;

/**
 * <h3>CastRemoteDisplayActivity</h3>
 * <p>
 * This code shows how to create an activity that renders some content on a
 * Cast device using a {@link com.google.android.gms.cast.CastPresentation}.
 * </p>
 * <p>
 * The activity uses the {@link MediaRouter} API to select a cast route
 * using a menu item.
 * When a presentation display is available, we stop
 * showing content in the main activity and instead start a {@link CastRemoteDisplayLocalService}
 * that will create a {@link com.google.android.gms.cast.CastPresentation} to render content on the
 * cast remote display. When the cast remote display is removed, we revert to showing content in
 * the main activity. We also write information about displays and display-related events
 * to the Android log which you can read using <code>adb logcat</code>.
 * </p>
 */
public class CastingActivity extends AppCompatActivity
{
    protected UnityPlayer mUnityPlayer;

    private final String TAG = "CastingActivity";

    private MediaRouter mMediaRouter;
    private MediaRouteSelector mMediaRouteSelector;

    private CastDevice mCastDevice;

    private SurfaceHolder mSurfaceHolder;
    private SurfaceHolder.Callback mSurfaceHolderCallback;

    /**
     * Initialization of the Activity after it is first created. Must at least
     * call {@link android.app.Activity#setContentView setContentView()} to
     * describe what is to be displayed in the screen.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        getWindow().setFormat(PixelFormat.RGBX_8888); // <--- This makes xperia play happy

        mUnityPlayer = new UnityPlayer(this);
//        if (mUnityPlayer.getSettings ().getBoolean ("hide_status_bar", true))
//        {
//            getWindow ().setFlags (WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        }

//        setContentView(mUnityPlayer);

        setContentView(R.layout.activity_casting);

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceViewActivity);
        mSurfaceHolder = surfaceView.getHolder();

        mSurfaceHolderCallback = new UnitySurfaceHolderCallback(mUnityPlayer, 0);

        mSurfaceHolder.addCallback(mSurfaceHolderCallback);

        mUnityPlayer.requestFocus();



        //setup casting control
        mMediaRouter = MediaRouter.getInstance(getApplicationContext());
        mMediaRouteSelector = new MediaRouteSelector.Builder()
                .addControlCategory(
                        CastMediaControlIntent.categoryForCast(getString(R.string.remote_display_app_id)))
                .build();
        if (isRemoteDisplaying()) {
            // The Activity has been recreated and we have an active remote display session,
            // so we need to set the selected device instance
            mCastDevice = CastDevice
                    .getFromBundle(mMediaRouter.getSelectedRoute().getExtras());
        } else {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                mCastDevice = extras.getParcelable(MainActivity.INTENT_EXTRA_CAST_DEVICE);
            }
        }

        mMediaRouter.addCallback(mMediaRouteSelector, mMediaRouterCallback,
                MediaRouter.CALLBACK_FLAG_REQUEST_DISCOVERY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUnityPlayer.resume();

        if (!isRemoteDisplaying()) {
            if (mCastDevice != null) {
                startCastService(mCastDevice);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mUnityPlayer.pause();

        mMediaRouter.unselect(MediaRouter.UNSELECT_REASON_STOPPED);
    }

    @Override
    protected void onDestroy() {
        mUnityPlayer.quit();
        super.onDestroy();
        mMediaRouter.removeCallback(mMediaRouterCallback);
    }


    // This ensures the layout will be correct.
    @Override public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
    }

    // Notify Unity of the focus change.
    @Override public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }


    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    @Override public boolean dispatchKeyEvent(KeyEvent event)
    {
        if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
            return mUnityPlayer.injectEvent(event);
        return super.dispatchKeyEvent(event);
    }

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
    @Override public boolean onKeyUp(int keyCode, KeyEvent event)     { return mUnityPlayer.injectEvent(event); }
    @Override public boolean onKeyDown(int keyCode, KeyEvent event)   { return mUnityPlayer.injectEvent(event); }
    @Override public boolean onTouchEvent(MotionEvent event)          { return mUnityPlayer.injectEvent(event); }
    /*API12*/ public boolean onGenericMotionEvent(MotionEvent event)  { return mUnityPlayer.injectEvent(event); }


    private boolean isRemoteDisplaying() {
        return CastRemoteDisplayLocalService.getInstance() != null;
    }

    private void initError() {
        Toast toast = Toast.makeText(
                getApplicationContext(), R.string.stream_display_message, Toast.LENGTH_SHORT);
        mMediaRouter.selectRoute(mMediaRouter.getDefaultRoute());
        toast.show();
    }

    private final MediaRouter.Callback mMediaRouterCallback =
            new MediaRouter.Callback() {
                @Override
                public void onRouteSelected(MediaRouter router, RouteInfo info) {
                    // Should not happen since this activity will be closed if there
                    // is no selected route
                }

                @Override
                public void onRouteUnselected(MediaRouter router, RouteInfo info) {
                    if (isRemoteDisplaying()) {
                        CastRemoteDisplayLocalService.stopService();
                    }
                    mCastDevice = null;
                    CastingActivity.this.finish();
                }
            };

    private void startCastService(CastDevice castDevice) {
        Intent intent = new Intent(CastingActivity.this,
                CastingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(
                CastingActivity.this, 0, intent, 0);

        CastRemoteDisplayLocalService.NotificationSettings settings =
                new CastRemoteDisplayLocalService.NotificationSettings.Builder()
                        .setNotificationPendingIntent(notificationPendingIntent).build();

        CastRemoteDisplayLocalService.startService(CastingActivity.this,
                UnityPresentationService.class, getString(R.string.remote_display_app_id),
                castDevice, settings,
                new CastRemoteDisplayLocalService.Callbacks() {
                    @Override
                    public void onRemoteDisplaySessionStarted(
                            CastRemoteDisplayLocalService service) {
                        Log.d(TAG, "onServiceStarted");

                        UnityPresentationService unityService;
                        try {
                            unityService = (UnityPresentationService) service;
                        }
                        catch (ClassCastException e){
                            Log.e(TAG, "the service was not a UnityPresentationService.");
                            return;
                        }
                        unityService.mUnityPlayer = mUnityPlayer;
                    }

                    @Override
                    public void onRemoteDisplaySessionError(Status errorReason) {
                        Log.d(TAG, "onServiceError: " + errorReason.getStatusCode());
                        initError();

                        mCastDevice = null;
                        CastingActivity.this.finish();
                    }
                });
    }



}

//
//if(mSurfaceHolder.getSurface().isValid()){
//        Canvas canvas = mSurfaceHolder.lockCanvas();
//        Paint paint = new Paint();
//        paint.setColor(Color.BLUE);
//        paint.setStrokeWidth(10);
//
//        paint.setStyle(Paint.Style.STROKE);
//        canvas.drawCircle(50, 50, 30, paint);
//
//        paint.setStyle(Paint.Style.FILL);
//        canvas.drawCircle(300, 300, 200, paint);
//
//        mSurfaceHolder.unlockCanvasAndPost(canvas);
//        }
//        else {
//        Log.e(TAG, "Surface not valid");
//        }