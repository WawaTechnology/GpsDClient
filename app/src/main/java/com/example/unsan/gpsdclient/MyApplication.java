package com.example.unsan.gpsdclient;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

/**
 * Created by Unsan on 16/5/18.
 */

public class MyApplication extends Application {
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity,
                                          Bundle savedInstanceState) {

                // new activity created; force its orientation to portrait
                activity.setRequestedOrientation(
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }


        });
    }
}
