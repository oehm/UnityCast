package com.oehm.unitycastv2;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Choreographer;
import android.view.Surface;
import android.view.View;

import java.io.FileDescriptor;

/**
 * Created by oehm on 15.08.2015.
 */
public class CustomUnityPlayerActivity extends UnityPlayerActivity
{

    private MediaRecorder mMediaRecorder;
    private Choreographer.FrameCallback mFrameCallback;
    private Choreographer mChoreographer;

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        class MyFrameCallback implements Choreographer.FrameCallback {

            @Override
            public void doFrame(long frameTimeNanos) {
                Log.d("Custom", "time: " + frameTimeNanos);
            }
        }

        mFrameCallback = new MyFrameCallback();

        mChoreographer = Choreographer.getInstance();
        mChoreographer.postFrameCallback(mFrameCallback);



//        int sleepTime = 33; // normal capture at 33ms / frame
//        //prepare MediaRecorder
//        mMediaRecorder = new MediaRecorder();
//        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
//        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.WEBM);
//        mMediaRecorder.setOutputFile(Environment.getExternalStorageDirectory().getPath() + "/UnityCast/test.webm");
//
//        mMediaRecorder.setVideoFrameRate(30);
//        mMediaRecorder.setVideoSize(352, 288);
//
//        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.VP8);
//
//
//        try{
//            mMediaRecorder.prepare();
//
//
//            Surface surface = mMediaRecorder.getSurface();
//
//            Paint paint = new Paint();
//            paint.setTextSize(16);
//            paint.setColor(Color.RED);
//            int i;
//
//            /* Test: draw 10 frames at 30fps before start
//             * these should be dropped and not causing malformed stream.
//             */
//            for(i = 0; i < 10; i++) {
//                Canvas canvas = surface.lockCanvas(null);
//                int background = (i * 255 / 99);
//                canvas.drawARGB(255, background, background, background);
//                String text = "Frame #" + i;
//                canvas.drawText(text, 100, 100, paint);
//                surface.unlockCanvasAndPost(canvas);
//                Thread.sleep(sleepTime);
//            }
//
//            mMediaRecorder.start();
//
//            /* Test: draw another 90 frames at 30fps after start */
//            for(i = 10; i < 100; i++) {
//                Canvas canvas = surface.lockCanvas(null);
//                int background = (i * 255 / 99);
//                canvas.drawARGB(255, background, background, background);
//                String text = "Frame #" + i;
//                canvas.drawText(text, 100, 100, paint);
//                surface.unlockCanvasAndPost(canvas);
//                Thread.sleep(sleepTime);
//            }
//
//            mMediaRecorder.stop();
//            mMediaRecorder.release();
//
//        } catch (Exception e) {
//            Log.v("record video failed ", e.toString());
//            mMediaRecorder.release();
//        }

    }

    @Override protected void onDestroy ()
    {
        mChoreographer.removeFrameCallback(mFrameCallback);

        super.onDestroy();
    }
}
