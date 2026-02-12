# Modern Android Calculator App

A production-ready Calculator app built with Kotlin and Jetpack Compose.

## Features
- **Basic & Scientific Calculations**: Supports complex expressions including trigonometry, logs, and powers.
- **Modern UI**: Clean, Neumorphic-inspired design that adapts to system themes (Dark/Light).
- **History**: Keeps track of your calculations.
- **Utils**: Copy result to clipboard and share with other apps.

## Prerequisites
- Android Studio Hedgehog or newer.
- JDK 11 or 17 (embedded in Android Studio).
- Android SDK API 34.

## How to Run
1. Open Android Studio.
2. Select **Open** and navigate to this folder.
3. Wait for Gradle Sync to complete.
4. Connect an Android device or start an Emulator.
5. Click **Run** (Green Play button).

## How to Generate Signed APK
1. Go to **Build > Generate Signed Bundle / APK**.
2. Select **APK** and click Next.
3. **Key store path**: Click "Create new..." if you don't have one.
   - Fill in the location and passwords.
   - Key alias: `key0` (or your choice).
4. Click **Next**.
5. Select **Release**.
6. Check **V1 (Jar Signature)** and **V2 (Full APK Signature)**.
7. Click **Create**.
8. The APK will be generated in `app/release/`.

## Architecture
- **MVVM**: Separation of concerns with ViewModel handling logic.
- **Jetpack Compose**: Modern declarative UI.
- **State Management**: `CalculatorState` acts as the single source of truth.
