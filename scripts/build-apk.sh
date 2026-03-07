#!/bin/bash

# Exit on error
set -e

# Define lock directory (using a directory is atomic in POSIX)
LOCKDIR="/tmp/bdates-build.lock"

# Function to remove lock
remove_lock() {
    if [ -d "$LOCKDIR" ]; then
        rmdir "$LOCKDIR"
    fi
}

# Ensure lock is removed even if the script is interrupted
trap remove_lock EXIT INT TERM

# Acquire lock (serialized: if the lock exists, wait for it)
if [ -d "$LOCKDIR" ]; then
    echo "Another build is in progress. Waiting for the lock..."
    while [ -d "$LOCKDIR" ]; do
        sleep 2
    done
fi

# Try to create the lock directory. If it fails, wait and try again.
# This loop handles potential race conditions between the initial check and creation.
while ! mkdir "$LOCKDIR" 2>/dev/null; do
    sleep 2
done

# Navigate to the project root
cd "$(dirname "$0")/.."

echo "Building the Android project..."
./gradlew assembleDebug

# Find the APK
APK_PATH=$(find app/build/outputs/apk -name "app-debug.apk" | head -n 1)

if [ -f "$APK_PATH" ]; then
    echo "APK produced successfully: $APK_PATH"
else
    echo "Error: APK not found."
    exit 1
fi
