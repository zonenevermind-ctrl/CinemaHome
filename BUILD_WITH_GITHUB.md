# Build the APK without installing Android Studio

This project includes a GitHub Actions workflow that builds the debug APK on a hosted runner. The APK is not precompiled in this download; the workflow creates it remotely.

1. Sign in to GitHub in a browser and create a new repository. A private repository is fine.
2. Upload the contents of this project folder to the repository (make sure `.github/workflows/build-apk.yml` is included) and commit them to the `main` branch.
3. Open the repository's **Actions** tab and select **Build Cinema Home APK**.
4. If prompted, enable Actions. Click **Run workflow**.
5. When the workflow finishes successfully, open that run and download the `Cinema-Home-debug-APK` artifact.
6. Extract the artifact ZIP; it contains `app-debug.apk`.

The APK is a debug build, not a Play Store release. It is not tested on your exact TV until installed and checked there. Keep Google TV Launcher enabled as a fallback.
