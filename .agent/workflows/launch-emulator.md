---
description: How to launch the Android app into an emulator from the terminal
---

1. Change directory to the Android SDK emulator home:
```bash
cd $ANDROID_HOME/emulator
```

2. List all available Android Virtual Devices (AVDs):
```bash
./emulator -list-avds
```

3. Launch a specific emulator from the list using its name:
```bash
./emulator -avd <device-name>
```
