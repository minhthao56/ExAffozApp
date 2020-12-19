package com.affozapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WifiModule extends ReactContextBaseJavaModule {

    private static  final  String SHORT_LENGTH = "SHORT_LENGTH";
    private static  final  String LONG_LENGTH = "LONG_LENGTH";
    private WifiManager wifiManager;
    private WifiBroadcastReceiver wifiReceiver;

    public WifiModule (ReactApplicationContext context){
        super(context);

    }

    @NonNull
    @Override
    public String getName() {
        return "WifiModule";
    }

    @Nullable
    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constant =  new HashMap<>();
        constant.put(SHORT_LENGTH, Toast.LENGTH_SHORT);
        constant.put(LONG_LENGTH, Toast.LENGTH_LONG);

        return constant;
    }


    class WifiBroadcastReceiver extends BroadcastReceiver {

        private WifiManager wifiManager;
        private Promise promise;

        public WifiBroadcastReceiver( WifiManager wifiManager, Promise promise){
            super();
            this.wifiManager = wifiManager;
            this.promise = promise;

        }

        @Override
        public void onReceive(Context context, Intent intent)   {
            context.unregisterReceiver(this);
//            boolean ok = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
//            if (ok)  {
//                List<ScanResult> list = this.wifiManager.getScanResults();
//                JSONArray wifiArray = new JSONArray();
//
//
//            }  else {
//
//            }

            try {
                List<ScanResult> results = this.wifiManager.getScanResults();
                JSONArray wifiArray = new JSONArray();

                for (ScanResult result : results) {
                    JSONObject wifiObject = new JSONObject();
                    if (!result.SSID.equals("")) {
                        try {
                            wifiObject.put("SSID", result.SSID);
                            wifiObject.put("BSSID", result.BSSID);
                            wifiObject.put("capabilities", result.capabilities);
                            wifiObject.put("frequency", result.frequency);
                            wifiObject.put("level", result.level);
                        } catch (Exception e) {
                            this.promise.reject(e.getMessage());
                            return;
                        }
                        wifiArray.put(wifiObject);
                    }
                }
                this.promise.resolve(wifiArray.toString());
                return;
            } catch (Exception e) {
                this.promise.reject(e.getMessage());
                return;
            }

        }
    }
    @ReactMethod
    public void StartScanWifi(Promise promise)  {
        try {
            this.wifiManager = (WifiManager) getReactApplicationContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            this.wifiManager.startScan();
            // Instantiate broadcast receiver
            this.wifiReceiver = new WifiBroadcastReceiver(wifiManager, promise);
            // Register the receiver
            getReactApplicationContext().getApplicationContext().registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        }catch (Exception e){
            promise.reject(e.getMessage());
        }
    }

    @ReactMethod
    public  void  ShowText(String content, int duration){
        Toast.makeText(getReactApplicationContext(), content, duration).show();
    }

}
