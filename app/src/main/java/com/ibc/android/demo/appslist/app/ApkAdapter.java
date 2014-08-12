package com.ibc.android.demo.appslist.app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.spicycurryman.getdisciplined10.app.R;

import java.util.List;

//

public class ApkAdapter extends BaseAdapter {




    SharedPreferences sharedPrefs;
    List<PackageInfo> packageList;
    Activity context;
    PackageManager packageManager;
    boolean[] itemChecked;

    String PACKAGE_NAME;





    public ApkAdapter(Activity context, List<PackageInfo> packageList,
                      PackageManager packageManager) {
        super();
        this.context = context;
        this.packageList = packageList;
        this.packageManager = packageManager;
        itemChecked = new boolean[packageList.size()];



    }


    private class ViewHolder {
        TextView apkName;
        CheckBox ck1;
    }

    public int getCount() {
        return packageList.size();
    }

    public Object getItem(int position) {
        return packageList.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.installed_apps, null);
            holder = new ViewHolder();

            holder.apkName = (TextView) convertView
                    .findViewById(R.id.appname);
            holder.ck1= (CheckBox)convertView
                    .findViewById(R.id.checkBox1);

            convertView.setTag(holder);
            //holder.ck1.setTag(packageList.get(position));

        } else {

            holder = (ViewHolder) convertView.getTag();
        }



        // ViewHolder holder = (ViewHolder) convertView.getTag();
        PackageInfo packageInfo = (PackageInfo) getItem(position);



        Drawable appIcon = packageManager
                .getApplicationIcon(packageInfo.applicationInfo);

        PACKAGE_NAME = packageInfo.packageName;

        final String appName = packageManager.getApplicationLabel(
                packageInfo.applicationInfo).toString();
        appIcon.setBounds(0, 0, 80, 80);
        holder.apkName.setCompoundDrawables(appIcon, null, null, null);
        holder.apkName.setCompoundDrawablePadding(15);
        holder.apkName.setText(appName);

        holder.ck1.setChecked(false);


        if (itemChecked[position])
            holder.ck1.setChecked(true);
        else
            holder.ck1.setChecked(false);

        Log.d("just loaded??", appName);


        Log.d("just loaded 2?", appName+position);


        for(int i= 0; i<packageList.size(); i++){
            sharedPrefs = context.getSharedPreferences(String.valueOf(i), Context.MODE_PRIVATE);
            holder.ck1.setChecked(sharedPrefs.getBoolean(String.valueOf(i),false));

        }

        holder.ck1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {





                SharedPreferences.Editor editor = context.getSharedPreferences(String.valueOf(position), Context.MODE_PRIVATE).edit();

                if (holder.ck1.isChecked()) {
                    itemChecked[position] = true;
                    holder.ck1.setChecked(true);
                    Log.i("This is", " checked: " + position);
                    editor.putBoolean(String.valueOf(position), true);
                    Log.d("put true", appName+position);

                    editor.apply();

                } else {
                    itemChecked[position] = false;
                    holder.ck1.setChecked(false);
                    Log.i("This is", " not checked: " + position);
                    editor.putBoolean(String.valueOf(position), false);
                    Log.d("put false", appName+position);

                    editor.apply();

                }

            }


        });




        return convertView;

    }




}