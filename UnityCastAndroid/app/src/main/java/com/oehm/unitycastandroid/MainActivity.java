package com.oehm.unitycastandroid;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.libraries.cast.companionlibrary.cast.VideoCastManager;
import com.unity3d.player.UnityPlayerNativeActivity;


public class MainActivity extends UnityPlayerNativeActivity {

    private VideoCastManager mCastManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        VideoCastManager.checkGooglePlayServices(this);

        VideoCastManager.
                initialize(this, "794B7BBF", null, null)
                .enableFeatures(
                        VideoCastManager.FEATURE_LOCKSCREEN |
                        VideoCastManager.FEATURE_DEBUGGING);


        mCastManager = VideoCastManager.getInstance();

        Toast.makeText(this, "v3", Toast.LENGTH_LONG).show();


        mCastManager.startCastDiscovery();
    }
}
