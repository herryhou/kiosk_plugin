# kiosk_plugin

A Flutter plugin, enable app in `kiosk mode`, works for Android only.

Effects - 
* The status bar is blank
* Notifications and system information are hidden
* Home and Overview buttons are hidden
* Disable other apps (allowlist itself only)
* Keep on topmost (home screen disabled)
* Lock screen is disabled
* Keep the screen on (?)
* Block windows and overlays (ie. toasts, dialogs)
* more restartitions
  * forbidden SAFE_BOOT
  * forbidden CREATE_WINDOWS
  * forbidden SYSTEM_ERROR_DIALOGS
  * forbidden MOUNT_PHYSICAL_MEDIA
  * forbidden FACTORY_RESET
  * forbidden BLUETOOTH
  * forbidden USB_FILE_TRANSFER
  * forbidden CONFIG_WIFI


## Quick start

#### 1. In `pubspec.yaml`, add the `kiosk_plugin` as

```xml
dependencies:
  flutter:
    sdk: flutter

  kiosk_plugin:
    git:
      url: git://github.com/herryhou/kiosk_plugin.git
      ref: main
```

#### 2. import `kiosk_plugin.dart`. As app start, it'll go into `kiosk mode` automatically.
You can cstart/stop the `kiosk mode` if you want to.

```dart
import 'package:kiosk_plugin/kiosk_plugin.dart';

await KioskPlugin.startKioskMode();
await KioskPlugin.stopKioskMode();
```

#### To declare `Device Admin`
in `android/app/main/AndroidManifest.xml`, 
add `<receiver>...</<receiver>` directly below `</activity>`

```xml
       <receiver
            android:name="app.hh.kiosk_plugin.MyDeviceAdminReceiver"
            android:permission="android.permission.BIND_DEVICE_ADMIN">
            <meta-data
                android:name="android.app.device_admin"
                android:resource="@xml/device_admin" />
            <intent-filter>
                <action android:name="android.app.action.DEVICE_ADMIN_ENABLED" />
                <action android:name="android.app.action.DEVICE_ADMIN_DISABLED" />
            </intent-filter>
        </receiver>
```

#### To declare security policies,
create a file `android/app/src/main/res/xml/device_admin.xml` as shown below

```xml
<device-admin xmlns:android="http://schemas.android.com/apk/res/android">
  <uses-policies>
    <force-lock />
    <wipe-data />
    <encrypted-storage />
    <disable-camera />
  </uses-policies>
</device-admin>
```

#### Install the apk to phone,
then make it `Device Owner App` 
```bash
> adb shell dpm set-device-owner app.hh.kiosk_plugin_example/app.hh.kiosk_plugin.MyDeviceAdminReceiver 

#remove admin, only if `android:testOnly="true"` in `AndroidManfifest.xml`
> adb shell dpm remove-active-admin app.hh.kiosk_plugin_example/app.hh.kiosk_plugin.MyDeviceAdminReceiver 
```

#### Finally, restart the apk, it will enter 'kiok mode' automatically.
You can also call `await KioskPlugin.stopKioskMode()`


---

## Another way to setup `device owner app`
Create file `/data/system/device_owner_2.xml'

```xml
<?xml version='1.0' encoding='utf-8' standalone='yes' ?>
<root>
<device-owner package="app.hh.kiosk_plugin_example" name="" component="app.hh.kiosk_plugin_example/app.hh.kiosk_plugin.MyDeviceAdminReceiver" userRestrictionsMigrated="true" />
<device-owner-context userId="0" />
</root>
```

and /data/system/device_policies.xml                                 
```xml
<?xml version='1.0' encoding='utf-8' standalone='yes' ?>
<policies setup-complete="true">
<admin name="com.google.android.gms/com.google.android.gms.mdm.receivers.MdmDeviceAdminReceiver">
<policies flags="540" />
<strong-auth-unlock-timeout value="0" />
</admin>
<lock-task-component name="app.hh.kiosk_plugin_example" />
</policies>
```