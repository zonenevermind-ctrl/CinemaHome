# Cinema Home — Android TV launcher prototype

A lightweight, no-third-party-library launcher prototype designed for Android TV devices including Android 9.

## Included
- Cinema-style dark home screen with a featured panel
- Quick picks for YouTube TV, Netflix, and Live TV when installed
- All-apps dialog
- Settings shortcut
- D-pad focus states and OK-to-open behavior
- Home intent filter so Android can offer it as a launcher

## Build in Android Studio
1. Install Android Studio on a computer.
2. Open this folder as an existing project.
3. Allow Gradle sync. Use JDK 11 for the Android Gradle Plugin 7.4.2.
4. Install Android SDK Platform 34 if prompted.
5. Choose **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
6. The APK should be at `app/build/outputs/apk/debug/app-debug.apk`.

This project source is provided; an APK has not been compiled or tested in this environment.

## Install over ADB
With the TV's developer options and ADB debugging enabled, connect to the TV and run from the folder containing the APK:

```cmd
adb install -r app-debug.apk
```

To test without changing the default launcher, install the APK first, then launch it with:

```cmd
adb shell monkey -p com.custom.cinemalauncher 1
```

When Android asks which Home app to use, choose **Cinema Home** and select **Just once** for the first test. Only select **Always** after checking the remote, apps, Live TV and casting.

## Rollback
If Cinema Home becomes the default and you want to return to Google TV, open Android Settings > Apps > Default apps / Home app (menu names vary by firmware) and select Google TV Launcher.

If the TV UI is difficult to reach, use ADB:
```cmd
adb shell cmd package resolve-activity --brief -a android.intent.action.MAIN -c android.intent.category.HOME
```
Then open Settings on the TV and change the Home app. Keep `com.google.android.tvlauncher` installed and enabled.

## Notes and limitations
- This is a starter prototype. It does not change TV system settings, remove PatchWall, or guarantee app availability.
- The Live TV shortcut is shown only if the package is launchable under `com.droidlogic.droidlivetv`.
- Some firmware uses a different package name for Live TV or has apps that only appear in the Leanback launcher query.
- This prototype is not yet validated on your exact MiTV-AXSO0 hardware. Test before making it the default.
