package com.splashanimations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.UiThreadUtil;


public class SplashAnimator {
    public static void fadeOut(View view, long duration) {
        view.setAlpha(1.0F);
        view.animate()
                .setDuration(duration)
                .alpha(0.0F)
                .setListener((new SplashAnimatorListener(view)))
                .setInterpolator((new DecelerateInterpolator())).start();
    }

    public static void hideLeft(View view, float screenWidth, long duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", new float[]{0.0F, -screenWidth});
        animator.setDuration(duration);
        animator.addListener((new SplashAnimatorListener(view)));
        animator.start();
    }

    public static void hideRight(View view, float screenWidth, long duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", new float[]{0.0F, screenWidth});
        animator.setDuration(duration);
        animator.addListener((new SplashAnimatorListener(view)));
        animator.start();
    }

    public static void hideDown(View view, float screenHeight, long duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", new float[]{0.0F, screenHeight});
        animator.setDuration(duration);
        animator.addListener((new SplashAnimatorListener(view)));
        animator.start();
    }

    public static void hideUp(View view, float screenHeight, long duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", new float[]{0.0F, -screenHeight});
        animator.setDuration(duration);
        animator.addListener((new SplashAnimatorListener(view)));
        animator.start();
    }

    public static void removeView(final View view) {
        UiThreadUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ViewParent viewParent = view.getParent();
                ViewGroup viewGroup = (ViewGroup) viewParent;
                if (viewGroup != null) {
                    viewGroup.removeView(view);
                }
            }
        });
    }

    public static class SplashAnimatorListener extends AnimatorListenerAdapter {
        private final View view;

        SplashAnimatorListener(View view) {
            super();
            this.view = view;
        }

        public void onAnimationEnd(@Nullable Animator animation) {
            removeView(this.view);
        }

        public void onAnimationCancel(@Nullable Animator animation) {
            removeView(this.view);
        }
    }
}
