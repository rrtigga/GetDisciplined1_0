package com.spicycurryman.getdisciplined10.app;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.ibc.android.demo.appslist.app.ApkAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



public class InstalledAppActivity extends ActionBarActivity
        implements OnItemClickListener, SearchView.OnQueryTextListener {

    PackageManager packageManager;
    ListView apkList;
    private SearchView mSearchView;
    private TextView mStatusView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Light_appalled);

        SpannableString s = new SpannableString("Installed Apps");
        s.setSpan(new TypefaceSpan(this, "ralewaylight.otf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// Update the action bar title with the TypefaceSpan instance
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(s);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        actionBar.setHomeButtonEnabled(true);



        setContentView(R.layout.user_installed);





// Update the action bar title with the TypefaceSpan instance

        packageManager = InstalledAppActivity.this.getPackageManager();


		/*To filter out System apps*/

        apkList = (ListView) findViewById(R.id.applist);

        new LoadApplications(InstalledAppActivity.this.getApplicationContext()).execute();


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.block, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        mSearchView = (SearchView) searchItem.getActionView();

        setupSearchView(searchItem);

        return true;

    }





    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    private void setupSearchView(MenuItem searchItem) {

        if (isAlwaysExpanded()) {
            mSearchView.setIconifiedByDefault(false);
        } else {
            searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM
                    | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        }

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            List<SearchableInfo> searchables = searchManager.getSearchablesInGlobalSearch();

            // Try to use the "applications" global search provider
            SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
            for (SearchableInfo inf : searchables) {
                if (inf.getSuggestAuthority() != null
                        && inf.getSuggestAuthority().startsWith("applications")) {
                    info = inf;
                }
            }
            mSearchView.setSearchableInfo(info);
        }

        mSearchView.setOnQueryTextListener(this);
    }



    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }


    public boolean onClose() {
        mStatusView.setText("Closed!");
        return false;
    }

    protected boolean isAlwaysExpanded() {
        return false;
    }

    /**
     * Return whether the given PackageInfo represents a system package or not.
     * User-installed packages (Market or otherwise) should not be denoted as
     * system packages.
     *
     * @param pkgInfo
     * @return boolean
     */
    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true
                : false;
    }

    private boolean isSystemPackage1(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) ? false
                : true;
    }

    private boolean isSystemPackage2(PackageInfo pkgInfo) {
        return ((pkgInfo.packageName.contains("com.spicycurryman.getdisciplined10.app"))) ? false
                : true;
    }




// Don't need in Fragment
/*@Override
public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.block, menu);
   // super.onCreateOptionsMenu(menu,inflater);
}*/

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //ApkAdapter apkAdapter=(ApkAdapter)apkList.getAdapter();

    }


    private class LoadApplications extends AsyncTask<Void, Void, Void> {


        private ProgressDialog pDialog;
        List<PackageInfo> packageList1 = new ArrayList<PackageInfo>();

        public LoadApplications(Context context){
            Context mContext = context;
        }




        @Override
        protected Void doInBackground(Void... params) {

            List<PackageInfo> packageList = packageManager
                    .getInstalledPackages(PackageManager.GET_PERMISSIONS);




            for(PackageInfo pi : packageList) {
                boolean b = isSystemPackage(pi);
                boolean c = isSystemPackage1(pi);
                boolean d = isSystemPackage2(pi);



                if ((!b || !c ) && d ){
                        packageList1.add(pi);
                    }
                }







            //sort by application name

            final PackageItemInfo.DisplayNameComparator comparator = new PackageItemInfo.DisplayNameComparator(packageManager);

            Collections.sort(packageList1, new Comparator<PackageInfo>() {
                @Override
                public int compare(PackageInfo lhs, PackageInfo rhs) {
                    return comparator.compare(lhs.applicationInfo, rhs.applicationInfo);
                }
            });



            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(InstalledAppActivity.this);
            pDialog.setMessage("Loading your apps...");
            pDialog.show();

        }

        @Override
        protected void onPostExecute(Void result) {

            apkList.setAdapter(new ApkAdapter(InstalledAppActivity.this, packageList1, packageManager));

            if (pDialog.isShowing()){
                pDialog.dismiss();

            }


            super.onPostExecute(result);
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }



}