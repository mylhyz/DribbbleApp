package io.lhyz.android.dribbble.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.lhyz.android.dribbble.AppPreference;
import io.lhyz.android.dribbble.AppStart;
import io.lhyz.android.dribbble.base.DefaultSubscriber;
import io.lhyz.android.dribbble.base.app.BaseActivity;
import io.lhyz.android.dribbble.data.DribbbleService;
import io.lhyz.android.dribbble.data.bean.User;
import io.lhyz.android.dribbble.data.source.DribbbleRepository;
import io.lhyz.android.dribbble.main.debut.DebutFragment;
import io.lhyz.android.dribbble.main.debut.DebutPresenter;
import io.lhyz.android.dribbble.main.following.FollowingFragment;
import io.lhyz.android.dribbble.main.following.FollowingPresenter;
import io.lhyz.android.dribbble.main.playoffs.PlayoffsFragment;
import io.lhyz.android.dribbble.main.playoffs.PlayoffsPresenter;
import io.lhyz.android.dribbble.main.popular.PopularFragment;
import io.lhyz.android.dribbble.main.popular.PopularPresenter;
import io.lhyz.android.dribbble.main.recent.RecentFragment;
import io.lhyz.android.dribbble.main.recent.RecentPresenter;
import io.lhyz.android.dribbble.main.team.TeamFragment;
import io.lhyz.android.dribbble.main.team.TeamPresenter;
import io.viper.android.dribbble.R;
import io.viper.android.dribbble.databinding.ActMainBinding;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@AndroidEntryPoint
public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar mToolbar;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    TabLayout mTabLayout;
    ViewPager mViewPager;
    @Inject
    DribbbleService mDribbbleService;
    @Inject
    AppPreference mAppPref;
    @Inject
    DribbbleRepository mDribbbleRepository;

    @Override
    protected View getBindingLayout() {
        ActMainBinding binding = ActMainBinding.inflate(getLayoutInflater());
        mToolbar = binding.appBarLayout.toolbar;
        mDrawerLayout = binding.drawerLayout;
        mNavigationView = binding.navView;
        mTabLayout = binding.appBarLayout.tabLayout;
        mViewPager = binding.appBarLayout.viewPager;
        return binding.getRoot();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();

        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);

        ArrayList<TabInfo> tabInfoArrayList = new ArrayList<>();

//        RecentFragment recentFragment = RecentFragment.newInstance();
//        new RecentPresenter(recentFragment, mDribbbleRepository);
//        tabInfoArrayList.add(new TabInfo(recentFragment, "Recent"));

        PopularFragment popularFragment = PopularFragment.newInstance();
        new PopularPresenter(popularFragment, mDribbbleRepository);
        tabInfoArrayList.add(new TabInfo(popularFragment, "Popular"));

//        FollowingFragment followingFragment = FollowingFragment.newInstance();
//        new FollowingPresenter(followingFragment, mDribbbleRepository);
//        tabInfoArrayList.add(new TabInfo(followingFragment, "Following"));
//
//        DebutFragment debutFragment = DebutFragment.newInstance();
//        new DebutPresenter(debutFragment, mDribbbleRepository);
//        tabInfoArrayList.add(new TabInfo(debutFragment, "Debuts"));
//
//        TeamFragment teamFragment = TeamFragment.newInstance();
//        new TeamPresenter(teamFragment, mDribbbleRepository);
//        tabInfoArrayList.add(new TabInfo(teamFragment, "Teams"));
//
//        PlayoffsFragment playoffsFragment = PlayoffsFragment.newInstance();
//        new PlayoffsPresenter(playoffsFragment, mDribbbleRepository);
//        tabInfoArrayList.add(new TabInfo(playoffsFragment, "Playoffs"));

        ViewPagerAdapter adapter = new ViewPagerAdapter(
                getSupportFragmentManager(),
                tabInfoArrayList);

        final int pos = mAppPref.readTabPosition();
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(6);
        mViewPager.setCurrentItem(pos);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestUser();
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
    public boolean onNavigationItemSelected(MenuItem item) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAppPref.saveTabPosition(mTabLayout.getSelectedTabPosition());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //非正常退出
        mAppPref.saveTabPosition(mTabLayout.getSelectedTabPosition());
    }

    private void initData() {
        final String token = mAppPref.readToken();

        if (token == null) {
            startActivity(new Intent(this, AppStart.class));
            finish();
        }
    }

    private void requestUser() {
        //如果缓存了user数据，那么直接拿来用
        final User user = mAppPref.readUser();
        if (user != null) {
            showUser(user);
            return;
        }
        mDribbbleService.getUser().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mUserSubscriber);
    }

    private final DefaultSubscriber<User> mUserSubscriber = new DefaultSubscriber<User>() {
        @Override
        public void onSuccess(User user) {
            mAppPref.saveUser(user);
            showUser(user);
        }

        @Override
        public void onError(Throwable e) {
            showError(e.getMessage());
        }
    };

    private void showUser(User user) {
        View headerView = mNavigationView.getHeaderView(0);
        SimpleDraweeView imgAvatar = (SimpleDraweeView) headerView.findViewById(R.id.img_avatar);
        TextView tvName = (TextView) headerView.findViewById(R.id.tv_name);
        TextView tvHost = (TextView) headerView.findViewById(R.id.tv_host);

        imgAvatar.setImageURI(Uri.parse(user.getAvatarUrl()));

        tvName.setText(user.getName());
        tvHost.setText(user.getHost());

        if (mAppPref.isFirstStart()) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    private void showError(String message) {
        Snackbar.make(findViewById(R.id.view_pager), message, Snackbar.LENGTH_LONG).show();
    }
}
