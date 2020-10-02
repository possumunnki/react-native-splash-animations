#  react-native-splash-animations
Animated splash screen API for React Native. There are 6 different hiding animations.

## Before you start
This package works **only with Android**.

This pacage is not forked but I took a lot of example by [react-native-bootsplash](https://github.com/zoontek/react-native-bootsplash) so some codes are totally copy-pasted from `react-native-bootsplash`.

## Getting started

`$ npm install react-native-splash-animations --save`

## Assets generation
To make it easy to implement splash screen I recommand to use script that creates the Android Drawable XML

`$ npx generate-bootsplash`


## Android

1. :warning: Skip to step 2 if you generated Android Drawable XML with script.
Create Image asset of your image by leftclicking and choosing `New > Image Asset`

Create `android/app/src/main/res/values/colors.xml` and add followings
```xml
<resources>
    <color name="splash_animation_background">#FFFFFF</color>
</resources>
```

Create `android/app/src/main/res/drawable/splashanimation.xml` which looks like followings:
```xml
<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android" android:opacity="opaque">
    <item android:drawable="@color/your_color" />
    <item android:id="@+id/logo">
        <!--Your image asset-->
        <bitmap android:src="@mipmap/your_image_asset" android:gravity="center"/>
    </item>
</layer-list>
```

2. Edit `android/app/src/main/res/values/styles.xml and add`
```xml
<resources>

    <!-- Base application theme. -->
    <style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
        <!-- Customize your theme here. -->
        <item name="android:textColor">#000000</item>
    </style>
    <!-- Add the following lines-->
    <style name="SplashTheme">
        <item name="windowActionBar">false</item>
        <item name="windowNoTitle">true</item>
        <item name="android:windowBackground">@drawable/splashanimation</item>
    </style>

</resources>
```

3. Edit `android/app/src/main/AndroidManifest.xml`

```xml

    <!-- ... -->

    <application
      android:name=".MainApplication"
      android:label="@string/app_name"
      android:icon="@mipmap/ic_launcher"
      android:roundIcon="@mipmap/ic_launcher_round"
      android:allowBackup="false"
      android:theme="@style/AppTheme">
        <activity
            android:name=".MainActivity"
            android:label="@string/app_name"
            android:configChanges="keyboard|keyboardHidden|orientation|screenSize|uiMode"
            android:launchMode="singleTask"
            android:windowSoftInputMode="adjustResize"
            android:exported="true"> <!-- Add this line -->
            <!-- Remove <intent-filter>-->
        </activity>
        <!-- Add following lines -->
        <activity
            android:name="com.splashanimations.SplashAnimationActivity"
            android:theme="@style/SplashTheme"
            android:launchMode="singleTask"
            >
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        <!-- ... -->
    </application>
    <!-- ... -->
```
4. Edit `android/app/src/main/java/com/yourprojectname/MainActivity.java`
``` java
public class MainActivity extends ReactActivity {

  // ...

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Displays your splash screen
    AnimatedLoadingScreen.show(R.drawable.splashanimation, this);
    // Sets hiding animation
    AnimatedLoadingScreen.setHideAnimation(HideAnimationType.HIDE_DOWN, 1000L);
  }
}
```

### AnimatedLoadingScreen.setHideAnimation(HideAnimationType hideAnimationType, Long duration)
By using this function, you can set different hide animations and duration of animation.

HideAnimationType:
* NO_ANIMATION - Hides without animation
* FADE_OUT - Hides with fade out effect
* HIDE_LEFT - Hides by moving the screen to left side
* HIDE_RIGHT - Hides by moving the screen to right side
* HIDE_UP - Hides by moving the screen to up side
* HIDE_DOWN - Hides by moving the screen to down side

Duration should be Long, so `1000L = 1s`
Default animation is `FADE_OUT` and duration `500L`


Then in your React Native project, you can use `hide()` function to hide displayed splash screen with animation.

Example:
```javascript
import React, { useEffect } from "react";
import { Text } from "react-native";
import SplashAnimations from 'react-native-splash-animations';

function App() {
  useEffect(() => {
    // After initials (e.g. fetching)
    SplashAnimations.hide();
  }, []);

  return <Text>Hello World!</Text>;
}
```
