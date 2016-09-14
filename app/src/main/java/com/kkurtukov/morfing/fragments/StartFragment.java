package com.kkurtukov.morfing.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kkurtukov.morfing.R;
import com.kkurtukov.morfing.utilities.debug.LLog;
import com.kkurtukov.morfing.views.CustomImageViewWithCheckbox;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kkurtukov on 03.09.2016.
 */
public class StartFragment extends BaseFragment implements CustomImageViewWithCheckbox.OnToggleListener {
    private static final String TAG = StartFragment.class.getSimpleName();
    /*************************************
     * BINDS
     *************************************/
    @BindView(R.id.fragment_start_main_image)
    ImageView mMainImage;
    @BindView(R.id.fragment_start_first_image)
    CustomImageViewWithCheckbox mFirstImage;
    @BindView(R.id.fragment_start_second_image)
    CustomImageViewWithCheckbox mSecondImage;
    @BindView(R.id.fragment_start_third_image)
    CustomImageViewWithCheckbox mThirdImage;
    /*************************************
     * PRIVATE FIELDS
     *************************************/
    private MenuItem mNextMenuItem;
    private int mSelectedIconId;

    /*************************************
     * PUBLIC METHODS
     *************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LLog.e(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_start, container, false);
        ButterKnife.bind(this, rootView);
        init();
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LLog.e(TAG, "onCreate");
        setHasOptionsMenu(true);
        mMainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        LLog.e(TAG, "onCreateOptionsMenu");
        inflater.inflate(R.menu.menu_main, menu);
        mNextMenuItem = menu.findItem(R.id.action_next);
        super.onCreateOptionsMenu(menu, inflater);
        checkValues();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        LLog.e(TAG, "onOptionsItemSelected");
        switch (item.getItemId()) {
            case R.id.action_next:
                mMainActivity.showSettingsScreen(mSelectedIconId,
                        mMainActivity.DEFAULT_ALPHA,
                        mMainActivity.DEFAULT_COUNT_OF_FRAMES,
                        mMainActivity.DEFAULT_TIME_OF_ANIMATION);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCustomCheckboxToggle(CustomImageViewWithCheckbox imageViewWithCheckbox, boolean value) {
        LLog.e(TAG, "onCustomCheckboxToggle");
        switch (imageViewWithCheckbox.getId()) {
            case R.id.fragment_start_first_image:
                mSelectedIconId = mFirstImage.getMainIconResId();
                mSecondImage.setChecked(false);
                mThirdImage.setChecked(false);
                break;
            case R.id.fragment_start_second_image:
                mSelectedIconId = mSecondImage.getMainIconResId();
                mFirstImage.setChecked(false);
                mThirdImage.setChecked(false);
                break;
            case R.id.fragment_start_third_image:
                mSelectedIconId = mThirdImage.getMainIconResId();
                mFirstImage.setChecked(false);
                mSecondImage.setChecked(false);
                break;
        }
        checkValues();
    }

    public void setSelectedIconId(int selectedIconId) {
        LLog.e(TAG, "setSelectedIconId");
        mSelectedIconId = selectedIconId;
    }

    /*************************************
     * PRIVATE METHODS
     *************************************/
    private void init() {
        LLog.e(TAG, "init");
        mFirstImage.setOnToggleListener(this);
        mSecondImage.setOnToggleListener(this);
        mThirdImage.setOnToggleListener(this);
        if (mSelectedIconId > mMainActivity.DEFAULT_ICON_VALUE){
            if (mFirstImage.getMainIconResId() == mSelectedIconId){
                mFirstImage.setChecked(true);
            }else if (mSecondImage.getMainIconResId() == mSelectedIconId){
                mSecondImage.setChecked(true);
            }else if (mThirdImage.getMainIconResId() == mSelectedIconId){
                mThirdImage.setChecked(true);
            }
        }
    }

    private void checkValues() {
        LLog.e(TAG, "checkValues");
        mNextMenuItem.setEnabled(mFirstImage.isChecked() ||
                mSecondImage.isChecked() ||
                mThirdImage.isChecked());
    }
}
