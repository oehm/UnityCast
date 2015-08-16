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

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.google.android.gms.cast.CastPresentation;
import com.google.android.gms.cast.CastRemoteDisplayLocalService;

/**
 * Service to keep the remote display running even when the app goes into the background
 */
public class PresentationService extends CastRemoteDisplayLocalService {

    private static final String TAG = "PresentationService";

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
        mPresentation = new TestCastPresentation(this, display);

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
    private class TestCastPresentation extends CastPresentation {

        private final String TAG = "TestCastPresentation";

        public TestCastPresentation(Context context, Display display) {
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
