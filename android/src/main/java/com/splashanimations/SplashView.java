package com.splashanimations;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;


/**
 * This class implements View -class and sets drawable component that
 * covers whole screen except status bar and system navigation.
 *
 * @property splashDrawable drawable component that covers whole screen
 */
public class SplashView extends View {
    private Drawable splashDrawable;

    public SplashView(Context context) {
        super(context);
    }

    public void setSplashDrawable(Drawable drawable) {
        this.splashDrawable = drawable;
    }

    public final void setSplashDrawable(@DrawableRes int drawable) {
        this.splashDrawable = ContextCompat.getDrawable(this.getContext(), drawable);
        this.splashDrawable.setCallback((Drawable.Callback) this);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int leftBound;
        int topBound;
        int rightBound;
        int bottomBound;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            WindowInsets windowInsets = this.getRootWindowInsets();
            leftBound = -windowInsets.getSystemWindowInsetLeft();
            topBound = -windowInsets.getSystemWindowInsetTop();
            rightBound = this.getWidth() + windowInsets.getSystemWindowInsetRight();
            bottomBound = this.getHeight() + windowInsets.getSystemWindowInsetBottom();
        } else {
            Activity activity = (Activity) this.getContext();
            Window window = activity.getWindow();
            Rect rectangle = new Rect();
            window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
            leftBound = -rectangle.left;
            topBound = -rectangle.top;
            rightBound = rectangle.right - rectangle.left;
            bottomBound = rectangle.bottom - rectangle.top;
        }

        Drawable splashDrawable = this.splashDrawable;

        splashDrawable.setBounds(leftBound, topBound, rightBound, bottomBound);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        this.splashDrawable.draw(canvas);
    }

    protected boolean verifyDrawable(Drawable who) {
        return who == this.splashDrawable || super.verifyDrawable(who);
    }
}
