package com.example.ahmohame.timewastingtracker;
import android.content.pm.PackageManager;
import android.os.Build;
import android.app.usage.UsageStatsManager;
import android.app.usage.UsageStats;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.List;
import android.content.Context;
import 	android.app.AppOpsManager;
import android.os.Process;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import  java.util.Map;
import android.content.pm.ApplicationInfo;

public class UsageStatsUtility {
    public static HashMap<String, AppData> mappedByPackageName = new HashMap<String, AppData>();
    static HashSet<String> appNames= new HashSet<>();

    public static void getUsageList(long from, long to, Context context){
        mappedByPackageName.clear();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager mUsageStatsManager = (UsageStatsManager)context.getSystemService(Context.USAGE_STATS_SERVICE);
            // We get usage stats for the last 10 seconds
            Map<String, UsageStats> stats = mUsageStatsManager.queryAndAggregateUsageStats(from, to);
            // Sort the stats by the last time used
            if(stats != null) {
                for (String packageName : stats.keySet())
                {
                    UsageStats usageStats = stats.get(packageName);
                    AppData app = mappedByPackageName.get(packageName);
                    if(usageStats!=null && usageStats.getTotalTimeInForeground()>=1000 && !androidStringNullCheck(usageStats.getPackageName()))
                    {
                        if(app == null)
                        {
                            app = new AppData();
                        }
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
        return (val == null || val.equals(null)|| (val.trim()).isEmpty() || (val.trim()).equals("null"));
    }
    public static void getAppNameFromPackageForAllApps(Context context) {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = context.getPackageManager().queryIntentActivities( mainIntent, 0);

        final PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo app : packages) {
            AppData appData = mappedByPackageName.get(app.packageName);
            if (appData!=null)
            {
                appData.appName = app.loadLabel(pm).toString();
                appData.icon = app.loadIcon(pm);
            }
        }
    }

    public static void removeNulls(){
        for(Iterator<Map.Entry<String, AppData>> it = mappedByPackageName.entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry<String, AppData> entry = it.next();
            if (androidStringNullCheck((entry.getValue().appName)) || entry.getValue().icon == null) {
                it.remove();
            }
        }
    }
    private static boolean isSystemPackage(ResolveInfo app) {
        return (app.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }

    public static boolean checkForPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), context.getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }
}
