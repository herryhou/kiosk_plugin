import 'dart:async';

import 'package:flutter/services.dart';

class KioskPlugin {
  static const MethodChannel _channel =
      MethodChannel('app.hh.kiosk/kiosk_plugin');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<bool> startKioskMode() => _channel
      .invokeMethod<bool>('startKioskMode')
      .then((value) => value ?? false);

  static Future<bool> stopKioskMode() => _channel
      .invokeMethod<bool>('stopKioskMode')
      .then((value) => value ?? false);
}
