package com.oehm.unitycastv2;

import android.view.SurfaceHolder;

import com.unity3d.player.UnityPlayer;

public class UnitySurfaceHolderCallback implements SurfaceHolder.Callback {

    private int mDisplayIndex;
    private UnityPlayer mUnityPlayer;

    public UnitySurfaceHolderCallback(UnityPlayer unityPlayer, int displayIndex){
        mUnityPlayer = unityPlayer;
        mDisplayIndex = displayIndex;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mUnityPlayer.displayChanged(mDisplayIndex, holder.getSurface());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        mUnityPlayer.displayChanged(mDisplayIndex, holder.getSurface());
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mUnityPlayer.displayChanged(mDisplayIndex, null);
    }
}