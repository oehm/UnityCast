package com.oehm.unitycastv2;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.MediaRouteButton;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.util.Log;
import android.view.View;

import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.CastMediaControlIntent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.oehm.unitycastv2.UnityGenerated.UnityPlayerActivity;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    protected static final String INTENT_EXTRA_CAST_DEVICE = "CastDevice";

    private MediaRouter mMediaRouter;
    private MediaRouteSelector mMediaRouteSelector;
    private MediaRouteButton mMediaRouteButton;
    private int mRouteCount = 0;
    private MediaRouterButtonView mMediaRouterButtonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check for GooglePlayServices
        int googlePlayServicesCheck = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (googlePlayServicesCheck != ConnectionResult.SUCCESS) {

            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(googlePlayServicesCheck, this, 0);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    finish();
                }
            });
            dialog.show();
        }

        // set the view
        setContentView(R.layout.activity_main);

        // create the MediaRouter and the MediaRouteSelector
        mMediaRouter = MediaRouter.getInstance(getApplicationContext());
        mMediaRouteSelector = new MediaRouteSelector.Builder()
                .addControlCategory(
                        CastMediaControlIntent.categoryForCast(getString(R.string.remote_display_app_id)))
                .build();

        // Set the MediaRouteButton selector for device discovery.
        mMediaRouterButtonView = (MediaRouterButtonView) findViewById(R.id.media_route_button_view);
        if (mMediaRouterButtonView != null) {
            mMediaRouteButton = mMediaRouterButtonView.getMediaRouteButton();
            mMediaRouteButton.setRouteSelector(mMediaRouteSelector);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMediaRouter.addCallback(mMediaRouteSelector, mMediaRouterCallback,
                MediaRouter.CALLBACK_FLAG_REQUEST_DISCOVERY);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMediaRouter.removeCallback(mMediaRouterCallback);
    }

    private final MediaRouter.Callback mMediaRouterCallback =
            new MediaRouter.Callback() {
                @Override
                public void onRouteAdded(MediaRouter router, MediaRouter.RouteInfo route) {
                    if (++mRouteCount >= 1) {
                        // Show the button when a device is discovered.
                        if (mMediaRouterButtonView != null) {
                            mMediaRouterButtonView.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onRouteRemoved(MediaRouter router, MediaRouter.RouteInfo route) {
                    if (--mRouteCount <= 0) {
                        // Hide the button if there are no devices discovered.
                        if (mMediaRouterButtonView != null) {
                            mMediaRouterButtonView.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onRouteSelected(MediaRouter router, MediaRouter.RouteInfo info) {
                    Log.d(TAG, "onRouteSelected");
                    CastDevice castDevice = CastDevice.getFromBundle(info.getExtras());
                    if (castDevice != null) {
                        Intent intent = new Intent(MainActivity.this,
                                CastingActivity.class);
                        intent.putExtra(INTENT_EXTRA_CAST_DEVICE, castDevice);
                        startActivity(intent);
                    }
                }

                @Override
                public void onRouteUnselected(MediaRouter router, MediaRouter.RouteInfo info) {
                }
            };

    public void startUnityLocalOnly(View view) {
        // Do something in response to button
        Intent intent = new Intent(MainActivity.this,
                CastingActivity.class);
        startActivity(intent);
    }
}
