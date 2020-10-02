package com.splashanimations;

import android.app.Activity;
import android.graphics.Point;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

public class AnimatedLoadingScreen {
    private static final HideAnimationType DEFAULT_HIDE_ANIMATION = HideAnimationType.FADE_OUT;
    private static final long DEFAULT_DURATION = 500L;

    private static int drawableResId;
    private static float screenWidth;
    private static float screenHeight;
    private static HideAnimationType hideAnimationType;

    private static long hideAnimationDuration;
    private static boolean isVisible;

    static {
        hideAnimationType = DEFAULT_HIDE_ANIMATION;
        hideAnimationDuration = DEFAULT_DURATION;
        drawableResId = -1;
    }

    public static void setHideAnimation(HideAnimationType animationType) {
        setHideAnimation(animationType, DEFAULT_DURATION);
    }

    public static void setHideAnimation(HideAnimationType animationType, Long duration) {
        hideAnimationType = animationType;
        hideAnimationDuration = duration;
    }


    public static void show(int drawableResId, Activity parentActivity) {
        SplashView splashView = new SplashView(parentActivity);
        isVisible = true;
        AnimatedLoadingScreen.drawableResId = drawableResId;
        splashView.setSplashDrawable(drawableResId);
        splashView.setId(drawableResId);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        Window window = parentActivity.getWindow();
        Point point = new Point();
        WindowManager windowManager = window.getWindowManager();
        windowManager.getDefaultDisplay().getSize(point);
        screenWidth = (float)point.x;
        screenHeight = (float)point.y;
        window.addContentView(splashView, layoutParams);
    }

    public static void hide(Activity activity) {
        if(isVisible) {
            SplashView splashView = activity.findViewById(drawableResId);
            if (HideAnimationType.NO_ANIMATION.equals(hideAnimationType)) {
                SplashAnimator.removeView(splashView);
            } else if (HideAnimationType.FADE_OUT.equals(hideAnimationType)) {
                SplashAnimator.fadeOut(splashView, hideAnimationDuration);
            } else if (HideAnimationType.HIDE_LEFT.equals(hideAnimationType)) {
                SplashAnimator.hideLeft(splashView, screenWidth, hideAnimationDuration);
            } else if (HideAnimationType.HIDE_RIGHT.equals(hideAnimationType)) {
                SplashAnimator.hideRight(splashView, screenWidth, hideAnimationDuration);
            } else if (HideAnimationType.HIDE_DOWN.equals(hideAnimationType)) {
                SplashAnimator.hideDown(splashView, screenHeight, hideAnimationDuration);
            } else if (HideAnimationType.HIDE_UP.equals(hideAnimationType)) {
                SplashAnimator.hideUp(splashView, screenHeight, hideAnimationDuration);
            }
        }
    }

    public static void hide(Activity activity, Long duration) {
        hideAnimationDuration = duration;
        hide(activity);
    }

    public static void hide(Activity activity, HideAnimationType animationType) {
        hideAnimationType = animationType;
        hide(activity);
    }

    public static void hide(Activity activity, HideAnimationType animationType, Long duration) {
        setHideAnimation(animationType, duration);
        hide(activity);
    }
}
