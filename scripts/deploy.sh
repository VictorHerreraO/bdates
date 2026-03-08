#!/bin/bash

# Script to build and deploy the app to a specific Android device/emulator.
# Usage: ./scripts/deploy.sh [device-serial]

set -e

# Function to show help
show_help() {
    echo "Usage: $0 [device-serial]"
    echo ""
    echo "Options:"
    echo "  [device-serial]  Optional. The serial of the device to deploy to."
    echo "                   If not provided and multiple devices are connected,"
    echo "                   the script will list available devices and exit."
    echo ""
    echo "Available devices:"
    adb devices
}

# If help is requested
if [[ "$1" == "-h" || "$1" == "--help" ]]; then
    show_help
    exit 0
fi

TARGET_SERIAL=$1

# Check connected devices
DEVICE_COUNT=$(adb devices | grep -v "List of devices attached" | grep "device$" | wc -l)

if [ "$DEVICE_COUNT" -eq 0 ]; then
    echo "Error: No devices connected."
    exit 1
fi

if [ -z "$TARGET_SERIAL" ]; then
    if [ "$DEVICE_COUNT" -gt 1 ]; then
        echo "Multiple devices detected. Please specify a target serial."
        adb devices
        exit 1
    else
        # Only one device, grab its serial
        TARGET_SERIAL=$(adb devices | grep -v "List of devices attached" | grep "device$" | awk '{print $1}')
        echo "Using single detected device: $TARGET_SERIAL"
    fi
fi

echo "Building debug APK..."
./scripts/build-apk.sh

APK_PATH=$(find app/build/outputs/apk -name "app-debug.apk" | head -n 1)

if [ ! -f "$APK_PATH" ]; then
    echo "Error: APK not found at $APK_PATH"
    exit 1
fi

echo "Installing to $TARGET_SERIAL..."
adb -s "$TARGET_SERIAL" install -r "$APK_PATH"

echo "Launching HomeNavigationActivity on $TARGET_SERIAL..."
adb -s "$TARGET_SERIAL" shell am start -n com.soyvictorherrera.bdates.debug/com.soyvictorherrera.bdates.core.HomeNavigationActivity

echo "Deployment complete!"
