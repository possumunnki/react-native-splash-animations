package com.splashanimations;


/**
 * This class should work like enum. This class was made because enum class
 * didn't work with native module. It whrow IllegalArgumentException.
 * 
 */
public class HideAnimationType {
    public static final HideAnimationType NO_ANIMATION = new HideAnimationType();
    public static final HideAnimationType FADE_OUT = new HideAnimationType();
    public static final HideAnimationType HIDE_LEFT = new HideAnimationType();
    public static final HideAnimationType HIDE_RIGHT = new HideAnimationType();
    public static final HideAnimationType HIDE_UP = new HideAnimationType();
    public static final HideAnimationType HIDE_DOWN = new HideAnimationType();
}