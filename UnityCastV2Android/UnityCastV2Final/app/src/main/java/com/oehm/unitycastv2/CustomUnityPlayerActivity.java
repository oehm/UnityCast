package com.oehm.unitycastv2;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Choreographer;
import android.view.Surface;
import android.view.View;

import java.io.FileDescriptor;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by oehm on 15.08.2015.
 */
public class CustomUnityPlayerActivity extends UnityPlayerActivity
{

    private MediaRecorder mMediaRecorder;
//    private Choreographer.FrameCallback mFrameCallback;
//    private Choreographer mChoreographer;

    private Timer mTimer;
    private Surface mSurface;
    private Paint mPaint;
    private int mFrameCount;

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

//        class MyFrameCallback implements Choreographer.FrameCallback {
//
//            @Override
//            public void doFrame(long frameTimeNanos) {
//                Log.d("Custom", "time: " + frameTimeNanos);
//            }
//        }

//        mFrameCallback = new MyFrameCallback();
//
//        mChoreographer = Choreographer.getInstance();
//        mChoreographer.postFrameCallback(mFrameCallback);



        int sleepTime = 33; // normal capture at 33ms / frame
        //prepare MediaRecorder
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.WEBM);
//        mMediaRecorder.setOutputFile(Environment.getExternalStorageDirectory().getPath() + "/UnityCast/test.webm");
        mMediaRecorder.setOutputFile(Environment.getExternalStorageDirectory().getPath() + "/UnityCast/test.webm");

        mMediaRecorder.setVideoFrameRate(30);
        mMediaRecorder.setVideoSize(352, 288);

        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.VP8);


        try{
            mMediaRecorder.prepare();


            mSurface = mMediaRecorder.getSurface();

            mPaint = new Paint();
            mPaint.setTextSize(16);
            mPaint.setColor(Color.RED);


            mMediaRecorder.start();

        } catch (Exception e) {
            Log.v("record video failed ", e.toString());
            mMediaRecorder.release();
        }

        //start test drawing
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                drawTestFrame();
            }
        }, 0, sleepTime);

    }

    public void drawTestFrame(){
        Canvas canvas = mSurface.lockCanvas(null);
        canvas.drawARGB(255, 255, 255, 255);
        String text = "Frame #" + mFrameCount;
        canvas.drawText(text, 100, 100, mPaint);
        mSurface.unlockCanvasAndPost(canvas);
        mFrameCount++;
    }

    @Override protected void onDestroy ()
    {
//        mChoreographer.removeFrameCallback(mFrameCallback);


        mMediaRecorder.stop();
        mMediaRecorder.release();
        mTimer.cancel();
        super.onDestroy();
    }
}
