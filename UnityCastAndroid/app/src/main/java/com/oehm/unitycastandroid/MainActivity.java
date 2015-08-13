package com.oehm.unitycastandroid;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.unity3d.player.UnityPlayerNativeActivity;


public class MainActivity extends UnityPlayerNativeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "onCreate", Toast.LENGTH_LONG).show();
    }
}
