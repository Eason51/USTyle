package com.example.ustyle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ustyle.data.AfterPost;
import com.example.ustyle.data.Global;
import com.example.ustyle.data.PostRequest;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    private boolean logged_in = false;

    public void checkUserStatus(){
        String requestBaseURL = Global.BaseURL.queryBaseURL;
        String androidID = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        String queryString = String.format("db.collection(\"users\")" +
                ".where({androidID: \"%s\"})" +
                ".limit(100).get()", androidID);

        AfterPost afterPost = new AfterPost() {
            @Override
            public void processAfterPost(JSONObject resultJSON) {
                try {

                    if(resultJSON.getJSONArray("data").length() == 0)
                    {
                        Global.User.loggedIn = false;
                        Global.User.checked = true;
                        return;
                    }

                    String dataJSONString = resultJSON.getJSONArray("data").getString(0);
                    JSONObject dataJSON = new JSONObject(dataJSONString);
                    Global.User.userID = dataJSON.getString("_id");
                    Global.User.androidID = dataJSON.getString("androidID");
                    Global.User.userName = dataJSON.getString("userName");
                    Global.User.itsc = dataJSON.getString("itsc");
                    Global.User.loggedIn = true;
                    Global.User.checked = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        new PostRequest(
                new PostRequest.PostRequestResult() {
                    @Override
                    public void processResult(JSONObject resultJSON) {
                        if(Global.User.checked)
                        {
                            if(Global.User.loggedIn) {
                                navigationView.getMenu().clear();
                                navigationView.inflateMenu(R.menu.menu);
                                View headerView = navigationView.getHeaderView(0);
                                TextView navUsername = (TextView) headerView.findViewById(R.id.navUsernameText);
                                navUsername.setText(Global.User.userName);
                            }
                        }

                    }
                },
                afterPost,
                this
        )
        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, queryString, requestBaseURL);

    }

    public void initializeData(){
        Global.context = this;
        Global.retrieveAccessToken();
        checkUserStatus();

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        initializeData();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // change navigation menu and username once logged in


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();


        // Create an instance of the tab layout from the view.
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        // Set the text for each tab.
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label1));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label2));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label3));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label4));
        // Set the tabs to fill the entire layout.
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Use PagerAdapter to manage page views in fragments.
        // Each page is represented by its own fragment.
        final ViewPager viewPager = findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        // Setting a listener for clicks.
        viewPager.addOnPageChangeListener(new
                TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        viewPager.setOffscreenPageLimit(3);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                Log.d("tab: ", Integer.toString(tab.getPosition()));
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if(item.getItemId() == R.id.action_signin){

            String requestBaseURL = Global.BaseURL.queryBaseURL;
            String androidID = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            String queryString = String.format("db.collection(\"users\")" +
                    ".where({androidID: \"%s\"})" +
                    ".limit(100).get()", androidID);

            new PostRequest(
                    new PostRequest.PostRequestResult() {
                        @Override
                        public void processResult(JSONObject resultJSON) {
                            try {
                                JSONObject pagerJSON = resultJSON.getJSONObject("pager");
                                int resultCount = pagerJSON.getInt("Total");

                                if(resultCount == 0)
                                {
                                    Intent signinIntent = new Intent(MainActivity.this, SigninActivity.class);
                                    startActivity(signinIntent);
                                    return;
                                }

                                JSONArray dataJSONArr = resultJSON.getJSONArray("data");
                                String dataJSONString = dataJSONArr.getString(0);
                                JSONObject dataJSON = new JSONObject(dataJSONString);
                                Global.User.userID = dataJSON.getString("_id");
                                Global.User.androidID = dataJSON.getString("androidID");
                                Global.User.userName = dataJSON.getString("userName");
                                Global.User.itsc = dataJSON.getString("itsc");
                                Global.User.loggedIn = true;
                                Global.User.checked = true;

                                navigationView.getMenu().clear();
                                navigationView.inflateMenu(R.menu.menu);
                                View headerView = navigationView.getHeaderView(0);
                                TextView navUsername = (TextView) headerView.findViewById(R.id.navUsernameText);
                                navUsername.setText(Global.User.userName);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            )
                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, queryString, requestBaseURL);


        }
        else if(item.getItemId() == R.id.action_settings){
        }
        else if(item.getItemId() == R.id.action_logout){
            Global.User.userID = "";
            Global.User.androidID = "";
            Global.User.userName = "";
            Global.User.itsc = "";
            Global.User.loggedIn = false;

            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.menu_new);
            View headerView = navigationView.getHeaderView(0);
            TextView navUsername = (TextView) headerView.findViewById(R.id.navUsernameText);
            navUsername.setText("Username");
        }
        return false;
    }
}