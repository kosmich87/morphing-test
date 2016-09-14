package com.kkurtukov.morfing;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.kkurtukov.morfing.fragments.ResultFragment;
import com.kkurtukov.morfing.fragments.SettingsFragment;
import com.kkurtukov.morfing.fragments.StartFragment;
import com.kkurtukov.morfing.utilities.debug.LLog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int DEFAULT_ICON_VALUE = 0;
    public static final int DEFAULT_ALPHA = 0;
    public static final int DEFAULT_COUNT_OF_FRAMES = 6;
    public static final double DEFAULT_TIME_OF_ANIMATION = 2.0;
    /*************************************
     * BINDS
     *************************************/
    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    /*************************************
     * PRIVATE FIELDS
     *************************************/
    private int mSelectedIconId;
    private int mAlpha;
    private int mCountOfFrames;
    private double mTimeOfAnimation;

    /*************************************
     * LIFECYCLE METHODS
     *************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LLog.e(TAG, "onCreate");
        setContentView(R.layout.activity_main);
        LLog.setDebuggable(BuildConfig.DEBUG);
        ButterKnife.bind(this);
        initToolbar();
        init();
    }

    @Override
    protected void onDestroy() {
        LLog.e(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        LLog.e(TAG, "onBackPressed");

        FragmentManager fm = getSupportFragmentManager();
        List<Fragment> fragments = fm.getFragments();
        Fragment lastFragment = fragments.get(fragments.size() - 1) != null ?
                fragments.get(fragments.size() - 1) : fragments.get(fragments.size() - 2);

        for (Fragment f : fragments) {
            LLog.e(TAG, "onBackPressed - fragments - " + (f != null ? f.getClass().getSimpleName() : "null"));
        }

        if (lastFragment != null) {
            if (lastFragment instanceof SettingsFragment) {
                showStartScreen(mSelectedIconId);
                return;
            } else if (lastFragment instanceof ResultFragment) {
                showSettingsScreen(mSelectedIconId, mAlpha, mCountOfFrames, mTimeOfAnimation);
                return;
            }
        }

        super.onBackPressed();

    }

    /*************************************
     * PROTECTED METHODS
     *************************************/
    protected void initToolbar() {
        LLog.e(TAG, "initToolbar");
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    /*************************************
     * PUBLIC METHODS
     *************************************/
    public Toolbar getToolbar() {
        LLog.e(TAG, "getToolbar");
        return mToolbar;
    }

    public void showStartScreen(int mSelectedIconId) {
        LLog.e(TAG, "showStartScreen");
        StartFragment startFragment = new StartFragment();
        startFragment.setSelectedIconId(mSelectedIconId);
        showFragment(startFragment, true);
    }

    public void showSettingsScreen(int selectedIconId, int mAlpha, int mCountOfFrames, double mTimeOfAnimation) {
        LLog.e(TAG, "showSettingsScreen");
        mSelectedIconId = selectedIconId;
        SettingsFragment settingsFragment = new SettingsFragment();
        settingsFragment.setSelectedIconId(selectedIconId);
        settingsFragment.setAlpha(mAlpha);
        settingsFragment.setCountOfFrames(mCountOfFrames);
        settingsFragment.setTimeOfAnimation(mTimeOfAnimation);
        showFragment(settingsFragment, true);
    }

    public void showResultScreen(int selectedIconId, int alpha, int countOfFrames, double timeOfAnimation) {
        LLog.e(TAG, "showResultScreen");
        mAlpha = alpha;
        mCountOfFrames = countOfFrames;
        mTimeOfAnimation = timeOfAnimation;
        ResultFragment resultFragment = new ResultFragment();
        resultFragment.setSelectedIconId(selectedIconId);
        resultFragment.setAlpha(mAlpha);
        resultFragment.setCountOfFrames(mCountOfFrames);
        resultFragment.setTimeOfAnimation(mTimeOfAnimation);
        showFragment(resultFragment, true);
    }

    /*************************************
     * PRIVATE METHODS
     *************************************/
    private void init() {
        LLog.e(TAG, "init");
        showStartScreen(DEFAULT_ICON_VALUE);
    }

    private void showFragment(Fragment fragment, boolean addToBackStack) {
        LLog.e(TAG, "showFragment");
        String tag = fragment.getClass().getSimpleName();
        if (addToBackStack) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment, tag)
                    .addToBackStack(null)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment, tag)
                    .commit();
        }
    }
}
