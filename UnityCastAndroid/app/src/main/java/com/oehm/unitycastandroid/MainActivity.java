package com.oehm.unitycastandroid;

import android.os.Bundle;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.widget.Toast;

import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.CastMediaControlIntent;
import com.unity3d.player.UnityPlayerNativeActivity;


public class MainActivity extends UnityPlayerNativeActivity {

    private MediaRouter mMediaRouter;
    private MediaRouteSelector mMediaRouteSelector;
    private MediaRouter.Callback mMediaRouterCallback;
    private CastDevice mSelectedDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toast.makeText(this, "v2", Toast.LENGTH_LONG).show();

        // Configure Cast device discovery
        mMediaRouter = MediaRouter.getInstance(getApplicationContext());
        mMediaRouteSelector = new MediaRouteSelector.Builder()
                .addControlCategory(CastMediaControlIntent.categoryForCast(getResources()
                        .getString(R.string.app_id))).build();//should be the required categories
        mMediaRouterCallback = new MyMediaRouterCallback();

    }

    private class MyMediaRouterCallback extends MediaRouter.Callback {

        @Override
        public void onRouteSelected(MediaRouter router, MediaRouter.RouteInfo info) {
            mSelectedDevice = CastDevice.getFromBundle(info.getExtras());

            //launchReceiver();
        }

        @Override
        public void onRouteUnselected(MediaRouter router, MediaRouter.RouteInfo info) {
            //teardown(false);
            mSelectedDevice = null;
        }
    }
}
