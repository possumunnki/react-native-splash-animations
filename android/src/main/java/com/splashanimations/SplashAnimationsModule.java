package com.splashanimations;

import android.app.Activity;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;


@ReactModule(name = SplashAnimationsModule.MODULE_NAME)
public class SplashAnimationsModule extends ReactContextBaseJavaModule {
    public static final String MODULE_NAME = "SplashAnimations";
    private final ReactApplicationContext reactContext;

    public SplashAnimationsModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @ReactMethod
    public void hide() {
        final Activity activity = getCurrentActivity();
        if (activity != null) {
            AnimatedLoadingScreen.hide(activity);
        }
    }
}
