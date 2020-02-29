package com.adamm.sharenet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.adamm.sharenet.Database.AppDatabase;
import com.adamm.sharenet.Services.PostService;
import com.adamm.sharenet.fragment.MyPostsFragment;
import com.adamm.sharenet.fragment.PostsFragment;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Create the adapter that will return a fragment for each section via implementation of anonymous class of FragmentPagerAdapter
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] mFragments = new Fragment[] {//Initialize fragments views
                    new PostsFragment(),
                    new MyPostsFragment(),
            };
            private final String[] mFragmentNames = new String[] {//Tabs names array
                    getString(R.string.heading_posts),
                    getString(R.string.heading_my_posts),
            };
            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }
            @Override
            public int getCount() {
                return mFragments.length;
            }
            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }
        };

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // Floating button that launches NewPostActivity
        findViewById(R.id.fabNewPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewPostActivity.class));//Launch new activity for new post
            }
        });

        createPreferences();
        startService();
    }

    public void createPreferences()
    {
       SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear().apply();
        editor.putString("firstName", AppDatabase.getCurr_user().firstName);
        editor.putString("lastName", AppDatabase.getCurr_user().lastName);
        editor.putString("email", AppDatabase.getCurr_user().email);
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this,  SettingsActivity.class));
                return true;

            case R.id.action_logout:

                startActivity(new Intent(this, SignInActivity.class));
                finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppDatabase.destroyInstance();
        stopService();
        AppDatabase.postService.stopSelf();
    }
    Intent serviceIntent;
    public void startService() {
         serviceIntent = new Intent(this, PostService.class);
        ContextCompat.startForegroundService(this, serviceIntent);
    }
    public void stopService() {
        stopService(serviceIntent);
    }


}
