package com.pi.launchargs;

import android.os.Bundle;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** LaunchargsPlugin */
public class LaunchArgsPlugin implements ActivityAware, FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;

  private List<String> args = new ArrayList<>();

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "launchargs");
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    Bundle bundle = binding.getActivity().getIntent().getExtras();
    args.clear();
    if(bundle != null){
      Set<String> keys = bundle.keySet();
      for (String key : keys) {
        args.add("--" + key);
        args.add(String.valueOf(bundle.get(key)));
      }
    }
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
    args.clear();
  }

  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
    Bundle bundle = binding.getActivity().getIntent().getExtras();
    args.clear();
    if(bundle != null){
      Set<String> keys = bundle.keySet();
      for (String key : keys) {
        args.add("--" + key);
        args.add(String.valueOf(bundle.get(key)));
      }
    }
  }

  @Override
  public void onDetachedFromActivity() {
    args.clear();
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else if (call.method.equals("args")) {
      result.success(args);
    } else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}
