# How to Create and Distribute a New Release

This guide explains how to generate a signed APK using the project\'s CI/CD workflow and then distribute it via Firebase App Distribution.

### Step 1: Trigger the Release Workflow

The project is set up to automatically build a signed release when you push a new version tag to GitHub.

1.  **Ensure your local `main` branch is up to date:**
    ```bash
    git checkout main
    git pull origin main
    ```

2.  **Create and push a new version tag.** The tag must start with `v` (e.g., `v1.0.1`).
    ```bash
    # Example: Create and push a tag for version 1.0.1
    git tag v1.0.1
    git push origin v1.0.1
    ```

3.  **Monitor the workflow:** Go to the "Actions" tab in your GitHub repository to watch the release workflow run.

### Step 2: Download the Signed APK

1.  Once the workflow completes successfully, go to the **"Releases"** section of your repository.
2.  Find the new release corresponding to the tag you just pushed.
3.  Under the **Assets** for that release, download the signed APK file (it will likely be named `app-release-signed.apk` or similar).

### Step 3: Distribute with Firebase

1.  Open a terminal in the directory where you downloaded the APK.
2.  Run the following Firebase CLI command. Make sure you are logged in (`firebase login`).

    ```bash
    firebase appdistribution:distribute app-release.apk \
      --app 1:1027620647949:android:cd6f489670fb251548d144 \
      --groups stakeholders,qa-team,port-operators \
      --release-notes "MetalInspect MVP Beta pentru validare rapidă"
    ```
    *Note: Replace `app-release.apk` with the exact name of the file you downloaded.*

4.  If prompted, select your Firebase project:
    ```bash
    firebase use metalinspect-430c5
    ```

That\'s it! Your stakeholders will receive an invitation to download and test the new build.
