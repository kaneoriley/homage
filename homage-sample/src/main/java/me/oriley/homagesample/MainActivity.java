/*
 * Copyright (C) 2016 Kane O'Riley
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.oriley.homagesample;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String KEY_CURRENT_ITEM = "currentItem";
    private static final int DEFAULT_ITEM = R.id.nav_expandable;

    @NonNull
    private FragmentManager mFragmentManager;

    @SuppressWarnings("FieldCanBeLocal")
    @NonNull
    private NavigationView mNavigationView;

    @NonNull
    private DrawerLayout mDrawerLayout;

    @NonNull
    private ActionBarDrawerToggle mToggle;

    @IdRes
    private int mCurrentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFragmentManager = getSupportFragmentManager();

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (drawerLayout == null || navigationView == null) {
            throw new IllegalStateException("Required views not found");
        }

        mDrawerLayout = drawerLayout;
        mToggle = new NoSpinDrawerToggle(this, mDrawerLayout, toolbar);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        mNavigationView = navigationView;
        mNavigationView.setNavigationItemSelectedListener(this);


        if (savedInstanceState == null) {
            onNavigationItemSelected(DEFAULT_ITEM);
        } else {
            mCurrentItem = savedInstanceState.getInt(KEY_CURRENT_ITEM, DEFAULT_ITEM);
            mNavigationView.setCheckedItem(mCurrentItem);
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_CURRENT_ITEM, mCurrentItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return onNavigationItemSelected(item.getItemId());
    }

    private boolean onNavigationItemSelected(int id) {
        // Handle navigation view item clicks here.
        switch (id) {
            case R.id.nav_expandable:
                openDrawerFragment(ExpandableCardIconFragment.class, id);
                break;
            case R.id.nav_popup:
                openDrawerFragment(PopupCardIconFragment.class, id);
                break;
            case R.id.nav_expandable_no_icons:
                openDrawerFragment(ExpandableCardFragment.class, id);
                break;
            case R.id.nav_popup_no_icons:
                openDrawerFragment(PopupCardFragment.class, id);
                break;
            case R.id.nav_dark_expandable:
                openDrawerFragment(DarkExpandableCardFragment.class, id);
                break;
            case R.id.nav_dark_popup:
                openDrawerFragment(DarkCardPopupFragment.class, id);
                break;
            case R.id.nav_expandable_no_card:
                openDrawerFragment(ExpandableIconFragment.class, id);
                break;
            case R.id.nav_expandable_no_card_no_icons:
                openDrawerFragment(ExpandableFragment.class, id);
                break;
            case R.id.nav_dark_popup_no_card_no_icons:
                openDrawerFragment(DarkPopupFragment.class, id);
                break;
        }

        closeDrawer();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDrawerLayout.removeDrawerListener(mToggle);
    }

    private void closeDrawer() {
        mDrawerLayout.closeDrawers();
    }

    private void openDrawerFragment(@NonNull final Class<? extends Fragment> fragmentClass,
                                    @IdRes int menuId) {
        Fragment fragment = getCurrentFragment();
        if (fragment == null || !fragmentClass.isInstance(fragment)) {
            showFragment(Fragment.instantiate(this, fragmentClass.getName()));
        } else {
            closeDrawer();
        }

        mCurrentItem = menuId;
        mNavigationView.setCheckedItem(menuId);
    }

    @Nullable
    private Fragment getCurrentFragment() {
        return mFragmentManager.findFragmentById(R.id.fragment);
    }

    private void showFragment(@NonNull Fragment fragment) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.replace(R.id.fragment, fragment, defaultTag(fragment.getClass()));
        ft.commit();
    }

    @NonNull
    private static String defaultTag(@NonNull Class<? extends Fragment> fragmentClass) {
        return fragmentClass.getName();
    }
}
