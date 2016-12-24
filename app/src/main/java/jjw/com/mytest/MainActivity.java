package jjw.com.mytest;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.toast.android.analytics.GameAnalytics;
import com.toast.android.analytics.common.Analytics;
import com.toast.android.analytics.googleplayservices.GooglePlayServicesManager;

import jjw.com.fragment.ItemFragment;
import jjw.com.fragment.dummy.DummyContent;
import jjw.com.fragment.model.ImageContent;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ItemFragment.OnListFragmentInteractionListener {

    private AdView mAdView;
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
         * toast cloud initialization
         */
        GameAnalytics.setDebugMode(true); // for debug

        int result = GameAnalytics.initializeSdk(getApplicationContext(), "8defe998920007c5d5f7b7f50ddc7865ebce8e5231c8918bccd5022bcf8430d3", "R0YnoB8b", "AppVersion", true);

        if (result != GameAnalytics.S_SUCCESS) {
            Log.d(TAG, "initialize error " + GameAnalytics.getResultMessage(result));

        }

        Thread t = new Thread(new Runnable() {
            public void run() {
                String AdId = GooglePlayServicesManager.getInstance().getAdvertiseID(getApplicationContext());
                GameAnalytics.setUserId(AdId, false);
            }
        });
        t.start();


        /**
         * navigation
         */

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        /**
         * Initialize the Mobile Ads SDK.
         */
        MobileAds.initialize(this, "ca-app-pub-7255673243724911~4319766789");

//        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
        // values/strings.xml.
        mAdView = (AdView) findViewById(R.id.ad_view);

        // Create an ad request. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);

    }







    @Override
    public void onBackPressed() {

        Snackbar.make(findViewById(R.id.drawer_layout), "onBackPressed", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
//            Intent goPicture = new Intent(this, PictureListActivity.class);
//
//            startActivity(goPicture);

            Snackbar.make(findViewById(R.id.drawer_layout), "기획중입니다.", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            // Handle the camera action
        }
//        else if (id == R.id.nav_gallery) {
//            GameAnalytics.traceEvent("gallery", "gallery_code", "FEVER", GameAnalytics.getVersion(), 1, 10);
//
//            Snackbar.make(findViewById(R.id.drawer_layout), "기획중입니다.", Snackbar.LENGTH_INDEFINITE)
//                    .setAction("Action", null).show();
//
//        } else if (id == R.id.nav_slideshow) {
//            Snackbar.make(findViewById(R.id.drawer_layout), "기획중입니다.", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();
//
//        } else if (id == R.id.nav_manage) {
//            Snackbar.make(findViewById(R.id.drawer_layout), "기획중입니다.", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();
//
//        } else if (id == R.id.nav_share) {
//            Snackbar.make(findViewById(R.id.drawer_layout), "기획중입니다.", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();
//
//        } else if (id == R.id.nav_send) {
//
//            Snackbar.make(findViewById(R.id.drawer_layout), "공사 예정입니다.", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onListFragmentInteraction(ImageContent.OneImageItem item) {
        Snackbar.make(findViewById(R.id.drawer_layout), "onListFragmentInteraction", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }


    /** Called when leaving the activity */
    @Override
    public void onPause() {
        // background 상태로 전환된 것을 서버에게  알림
        GameAnalytics.traceDeactivation(this);

        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();

        // foreground 상태로 전환된 것을 서버에게 알림
        GameAnalytics.traceActivation(this);

        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
