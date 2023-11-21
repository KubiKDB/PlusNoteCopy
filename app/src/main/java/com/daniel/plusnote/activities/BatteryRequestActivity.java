package com.daniel.plusnote.activities;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.daniel.plusnote.R;

import java.util.List;

public class BatteryRequestActivity extends AppCompatActivity {

    @SuppressLint({"BatteryLife", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.battery_optimization_layout);
        Button allow = findViewById(R.id.allow);
//        Button not_allow = findViewById(R.id.not_allow);
        TextView textView = findViewById(R.id.greetings_text);
        textView.setText(
                "fast&easy notepad that allows you to make and organize your everyday notes, " +
                        "including setting reminders and getting notifications." +
                "        \nIn order to use all app's features you should grant some permissions: " +
                "        \n1. To ignore battery optimization." +
                "        \n2. To use camera." +
                "        \n3. To use microphone." +
                "        \n4. To use internal storage." +
                "        \nAlthough you have a choice not to give those permissions, " +
                        "you are strongly advised to grant them for proper functioning of this app.");

        allow.setOnClickListener(v -> {
            Intent ignoreBatteryOptimizationIntent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (pm.isIgnoringBatteryOptimizations(packageName)) {} else {
                ignoreBatteryOptimizationIntent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                ignoreBatteryOptimizationIntent.setData(Uri.parse("package:" + packageName));
            }
            if (ignoreBatteryOptimizationIntent.getAction() != null){
                startActivity(ignoreBatteryOptimizationIntent);
            }
            addAutoStartup();
            finish();
            SharedPreferences.Editor sharedPreferencesEditor =
                    PreferenceManager.getDefaultSharedPreferences(this).edit();
            sharedPreferencesEditor.putBoolean(
                    "COMPLETED_ONBOARDING_PREF_NAME", true);
            sharedPreferencesEditor.apply();
        });
//        not_allow.setOnClickListener(v -> {
//            SharedPreferences.Editor sharedPreferencesEditor =
//                    PreferenceManager.getDefaultSharedPreferences(this).edit();
//            sharedPreferencesEditor.putBoolean(
//                    "COMPLETED_ONBOARDING_PREF_NAME", true);
//            sharedPreferencesEditor.apply();
//            finish();
//        });
    }

    private void addAutoStartup() {

        try {
            Intent intent = new Intent();
            String manufacturer = android.os.Build.MANUFACTURER;
            if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
            } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
            } else if ("Letv".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
            } else if ("Honor".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
            }

            List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if  (list.size() > 0) {
                startActivity(intent);
            }
        } catch (Exception e) {
            Log.e("exc" , String.valueOf(e));
        }
    }

    @Override
    public void onBackPressed() {

    }
}