package com.example.ahmohame.timewastingtracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import 	android.widget.GridView;
import 	android.widget.AdapterView;
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

public class MainActivity extends AppCompatActivity {

    GridView grid;
    AppData[] apps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!UsageStatsUtility.checkForPermission(this))
        {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
        else
        {
            UsageStatsUtility.getUsageList(0,System.currentTimeMillis(),this);
            UsageStatsUtility.getAppNameFromPackageForAllApps(this);

        }


        Collection<AppData> values = UsageStatsUtility.mappedByPackageName.values();
        apps = values.toArray(new AppData[values.size()]);

        Arrays.sort(apps, new Comparator<AppData>() {
            @Override
            public int compare(AppData object1, AppData object2) {
                return Long.valueOf(object2.totalTime).compareTo(Long.valueOf(object1.totalTime));
            }
        });

        CustomGrid adapter = new CustomGrid(this, apps);
        grid=(GridView)findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                int reversedPosition = position;
                Toast.makeText(MainActivity.this, "You Clicked at " +apps[reversedPosition].appName, Toast.LENGTH_SHORT).show();

            }
        });

    }
}
