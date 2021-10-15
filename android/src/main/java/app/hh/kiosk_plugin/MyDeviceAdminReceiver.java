package app.hh.kiosk_plugin;

import android.app.admin.DeviceAdminReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.app.admin.DevicePolicyManager;
import android.os.UserManager;

// import android.widget.Toast;

public class MyDeviceAdminReceiver extends DeviceAdminReceiver {
    @Override
    public void onEnabled(Context context, Intent intent) {
        // super.onEnabled(context, intent);
        // Log.e("Device Admin: ", "Enabled");
        Common.showToast(context, "[Device Admin enabled]");
        // Common.becomeHomeActivity(context);
    }
    @Override
    public String onDisableRequested(Context context, Intent intent) {
        return "Admin disable requested";
    }
    @Override
    public void onDisabled(Context context, Intent intent) {
        // super.onDisabled(context, intent);
        // Log.e("Device Admin: ", "Disabled");
        Common.showToast(context, "[Device Admin disabled]");
    }
    @Override
    public void onPasswordChanged(Context context, Intent intent) {
        // super.onPasswordChanged(context, intent);
        // Log.e("Device Admin: ", "Password changed");
        Common.showToast(context, "[Device Admin: Password changed]");
    }

    @Override
    public void onLockTaskModeEntering(Context context, Intent intent,
            String pkg) {
        final DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        // final String appPackageName = a.getPackageName();
        final ComponentName deviceAdmin = new ComponentName(context, MyDeviceAdminReceiver.class);

        dpm.addUserRestriction(deviceAdmin,UserManager.DISALLOW_SAFE_BOOT);
        dpm.addUserRestriction(deviceAdmin,UserManager.DISALLOW_CREATE_WINDOWS);
        dpm.addUserRestriction(deviceAdmin,UserManager.DISALLOW_SYSTEM_ERROR_DIALOGS);
        dpm.addUserRestriction(deviceAdmin,UserManager.DISALLOW_MOUNT_PHYSICAL_MEDIA);
        dpm.addUserRestriction(deviceAdmin,UserManager.DISALLOW_FACTORY_RESET);
        dpm.addUserRestriction(deviceAdmin,UserManager.DISALLOW_BLUETOOTH);
        dpm.addUserRestriction(deviceAdmin,UserManager.DISALLOW_USB_FILE_TRANSFER);
        dpm.addUserRestriction(deviceAdmin,UserManager.DISALLOW_CONFIG_WIFI);
    
        Common.showToast(context, "[Kiosk Mode enabled]");
    }

    @Override
    public void onLockTaskModeExiting(Context context, Intent intent) {
        final DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        // final String appPackageName = a.getPackageName();
        final ComponentName deviceAdmin = new ComponentName(context, MyDeviceAdminReceiver.class);

        dpm.clearUserRestriction(deviceAdmin,UserManager.DISALLOW_SAFE_BOOT);
        dpm.clearUserRestriction(deviceAdmin,UserManager.DISALLOW_CREATE_WINDOWS);
        dpm.clearUserRestriction(deviceAdmin,UserManager.DISALLOW_SYSTEM_ERROR_DIALOGS);
        dpm.clearUserRestriction(deviceAdmin,UserManager.DISALLOW_MOUNT_PHYSICAL_MEDIA);
        dpm.clearUserRestriction(deviceAdmin,UserManager.DISALLOW_FACTORY_RESET);
        dpm.clearUserRestriction(deviceAdmin,UserManager.DISALLOW_BLUETOOTH);
        dpm.clearUserRestriction(deviceAdmin,UserManager.DISALLOW_USB_FILE_TRANSFER);
        dpm.clearUserRestriction(deviceAdmin,UserManager.DISALLOW_CONFIG_WIFI);
        Common.showToast(context, "[Kiosk Mode disabled]");
    }

    // public static ComponentName getComponentName(Context context) {
    //     return new ComponentName(context.getApplicationContext(), MyDeviceAdminReceiver.class);
    // } 
}