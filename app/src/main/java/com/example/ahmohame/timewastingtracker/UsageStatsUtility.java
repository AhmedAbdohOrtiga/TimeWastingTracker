package com.example.ahmohame.timewastingtracker;
import android.os.Build;
import android.app.usage.UsageStatsManager;
import android.app.usage.UsageStats;

import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.List;
import android.content.Context;
import 	android.app.AppOpsManager;
import android.os.Process;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import  java.util.Map;

public class UsageStatsUtility {
    public static HashMap<String,AppData> mappedByPackageName = new HashMap<String, AppData>();

    public static void getUsageList(long from, long to, Context context){
        mappedByPackageName.clear();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager mUsageStatsManager = (UsageStatsManager)context.getSystemService(Context.USAGE_STATS_SERVICE);
            // We get usage stats for the last 10 seconds
            Map<String, UsageStats> stats = mUsageStatsManager.queryAndAggregateUsageStats(0, to);
            // Sort the stats by the last time used
            if(stats != null) {
                for (String packageName : stats.keySet())
                {
                    UsageStats usageStats = stats.get(packageName);
                    if(usageStats!=null && usageStats.getTotalTimeInForeground()>0 && !androidStringNullCheck(usageStats.getPackageName()))
                    {
                        AppData app= new AppData();
                        app.packageName = usageStats.getPackageName();
                        app.totalTime = usageStats.getTotalTimeInForeground();
                        app.lastUsed = usageStats.getLastTimeUsed();
                        mappedByPackageName.put(app.packageName,app);
                    }
                }
            }
        }
    }

    private static boolean androidStringNullCheck(String val){
        boolean x = (val == null || val.equals(null)|| (val.trim()).isEmpty() || (val.trim()).equals("null"));
        if(x)
        {
            int y = -1;
        }
        return (val == null || val.equals(null)|| (val.trim()).isEmpty() || (val.trim()).equals("null"));
    }
    public static void getAppNameFromPackageForAllApps(Context context) {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = context.getPackageManager().queryIntentActivities( mainIntent, 0);

        for (ResolveInfo app : pkgAppsList) {
            AppData appData = mappedByPackageName.get(app.activityInfo.packageName);
            if (appData!=null) {
                appData.appName = app.activityInfo.loadLabel(context.getPackageManager()).toString();
                appData.icon = app.activityInfo.loadIcon(context.getPackageManager());
            }
        }
    }

    public static boolean checkForPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), context.getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }
}
