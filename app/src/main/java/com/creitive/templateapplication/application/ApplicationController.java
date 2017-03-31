package com.creitive.templateapplication.application;

import android.app.Application;


import com.creitive.templateapplication.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Functionality common to the application
 */

public class ApplicationController extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/lato-regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
