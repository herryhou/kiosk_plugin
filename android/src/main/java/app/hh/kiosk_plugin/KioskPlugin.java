package app.hh.kiosk_plugin;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import android.app.Activity;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.widget.Toast;
import android.provider.Settings.Secure;
//import android.content.IntentFilter;
// import android.os.Build.VERSION;
// import android.os.Build.VERSION_CODES;
// import android.util.Log;

public class KioskPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
  private MethodChannel channel;
  private Activity attachedActivity = null;
  private Context appContext;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "app.hh.kiosk/kiosk_plugin");
    channel.setMethodCallHandler(this);
    appContext = flutterPluginBinding.getApplicationContext();
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getPlatformVersion")) {
      final String id = Secure.getString(appContext.getContentResolver(), Secure.ANDROID_ID);
      result.success("Android " + android.os.Build.VERSION.RELEASE + " " + id);
    } else if (call.method.equals("startKioskMode")){
      if (attachedActivity != null){
        //final ViewGroup view = attachedActivity.findViewById(android.R.id.content).getChildAt(0);
        enableKioskMode();
      }
    }else if (call.method.equals("stopKioskMode")){
      if (attachedActivity != null){
        //final ViewGroup view = attachedActivity.findViewById(android.R.id.content).getChildAt(0);
        disableKioskMode();
      }
    } else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
    appContext = null;
  }

  @Override
  public void onDetachedFromActivity(){
    attachedActivity = null;
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
      attachedActivity = null;
  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
    attachedActivity = binding.getActivity();
    enableKioskMode();
  }

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding){
    attachedActivity = binding.getActivity();
    // activeDeviceAdmin(c, dpm, deviceAdmin);
    setLockTaskPackages();
    enableKioskMode();
  }

  private void showToast(String text) {
    Toast.makeText(appContext, text, Toast.LENGTH_SHORT).show();
  }

  // private void activeDeviceAdmin(Activity c, DevicePolicyManager dpm, ComponentName deviceAdmin){
  //   if (!dpm.isAdminActive(deviceAdmin)) {
  //     showToast("App is not a device admin !!");

  //     final Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
  //     intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceAdmin);
  //     c.startActivity(intent);
  //   } else {
  //     showToast("App is a device admin");
  //   }
  // }

  private void setLockTaskPackages(){
    final Activity a = attachedActivity;
    final DevicePolicyManager dpm = (DevicePolicyManager) a.getSystemService(Context.DEVICE_POLICY_SERVICE);
    final String appPackageName = a.getPackageName();
    final ComponentName deviceAdmin = new ComponentName(a, MyDeviceAdminReceiver.class);
    
    if (dpm.isDeviceOwnerApp(appPackageName)) {
      final String[] appPackageNames = {appPackageName};
      dpm.setLockTaskPackages(deviceAdmin, appPackageNames);
      showToast("App is device owner");
    } else {
      showToast("App is not the device owner !!");
    }
  }

  private void enableKioskMode(){
    if (attachedActivity != null) {
      final Activity a = attachedActivity;
      final DevicePolicyManager dpm = (DevicePolicyManager) a.getSystemService(Context.DEVICE_POLICY_SERVICE);
      final String appPackageName = a.getPackageName();
      
      if (dpm.isLockTaskPermitted(appPackageName)) {
        a.startLockTask();
      } else {
        showToast("Kiosk in Pin screen mode");
      }
    }
  }

  private void disableKioskMode(){
    if (attachedActivity != null){
      attachedActivity.stopLockTask();
    }
  }
}
