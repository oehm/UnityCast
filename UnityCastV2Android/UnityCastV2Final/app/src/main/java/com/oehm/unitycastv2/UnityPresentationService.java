package com.oehm.unitycastv2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.google.android.gms.cast.CastPresentation;
import com.google.android.gms.cast.CastRemoteDisplayLocalService;

public class UnityPresentationService extends CastRemoteDisplayLocalService {

    private static final String TAG = "UnityPresentationServic";

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
            setContentView(R.layout.presentation_main);

        }
    }
}
