package com.example.ahmohame.timewastingtracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomGrid extends BaseAdapter{
    private Context mContext;
    private final AppData [] apps;

    public CustomGrid(Context c,AppData[] apps) {
        mContext = c;
        this.apps= apps;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return apps.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_single, null);
            TextView appName = (TextView) grid.findViewById(R.id.app_name);
            TextView totalTime = (TextView) grid.findViewById(R.id.total_time);
            ImageView appIcon = (ImageView)grid.findViewById(R.id.app_icon);
            int reversePosition = position;
            appName.setText(apps[reversePosition].appName);
            appIcon.setImageDrawable(apps[reversePosition].icon);
            totalTime.setText(mapTimeToString(apps[reversePosition].totalTime));
        } else {
            grid = (View) convertView;
        }
        return grid;
    }

    private String mapTimeToString(long timeMilliSec){
        String val = "";
        timeMilliSec /= 1000;
        if(timeMilliSec > 0)
        {
            val = (timeMilliSec % 60) + "s" + val;
            timeMilliSec /=60;
        }
        if(timeMilliSec > 0)
        {
            val = (timeMilliSec % 60) + "m" + val;
            timeMilliSec /=60;
        }
        if(timeMilliSec > 0)
        {
            val = (timeMilliSec % 60) + "h" + val;
            timeMilliSec /=60;
        }
        return val;
    }
}