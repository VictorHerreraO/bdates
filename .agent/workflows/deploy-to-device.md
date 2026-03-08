---
description: How to build and deploy the app to a specific device serial
---

1. Identify the serial of the target device:
```bash
// turbo
adb devices
```

2. Run the deployment script with the target serial:
```bash
// turbo
./scripts/deploy.sh <device-serial>
```

This script will automatically:
- Build the debug APK (`assembleDebug`)
- Install specifically to the target device (`adb -s`)
- Launch the main activity on that device
