package com.example.ahmohame.timewastingtracker;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import 	android.widget.GridView;
import 	android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.content.Intent;
import android.provider.Settings;
import android.os.Build;
import android.app.usage.UsageStatsManager;
import android.app.usage.UsageStats;

import java.util.Collection;
import java.util.Arrays;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.List;
import android.content.Context;
import 	android.app.AppOpsManager;
import android.os.Process;
import java.util.Comparator;
import android.app.AlertDialog;

public class MainActivity extends AppCompatActivity {

    GridView grid;
    AppData[] apps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        grid=(GridView)findViewById(R.id.grid);
        grid.setVisibility(View.GONE);

        if(!UsageStatsUtility.checkForPermission(this))
        {
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Please Give Usage Access Permission");
            dlgAlert.setTitle("Permission Needed!");
            dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                }
            });
            dlgAlert.setCancelable(false);
            dlgAlert.create().show();
        }
        else
        {
            grid.setVisibility(View.VISIBLE);
            UsageStatsUtility.getUsageList(0, System.currentTimeMillis(),this);
            UsageStatsUtility.getAppNameFromPackageForAllApps(this);
            UsageStatsUtility.removeNulls();
            Collection<AppData> values = UsageStatsUtility.mappedByPackageName.values();
            apps = values.toArray(new AppData[values.size()]);


            Arrays.sort(apps, new Comparator<AppData>() {
                @Override
                public int compare(AppData object1, AppData object2) {
                    return Long.valueOf(object2.totalTime).compareTo(Long.valueOf(object1.totalTime));
                }
            });

            CustomGrid adapter = new CustomGrid(this, apps);

            grid.setAdapter(adapter);
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Toast.makeText(MainActivity.this, "You Clicked at " +apps[position].appName, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!UsageStatsUtility.checkForPermission(this))
        {
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Please Give Usage Access Permission");
            dlgAlert.setTitle("Permission Needed!");
            dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                }
            });
            dlgAlert.setCancelable(false);
            dlgAlert.create().show();
        }
    }
}
