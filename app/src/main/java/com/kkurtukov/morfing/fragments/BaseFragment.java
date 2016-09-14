package com.kkurtukov.morfing.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.kkurtukov.morfing.MainActivity;

/**
 * Created by kkurtukov on 03.09.2016.
 */
public class BaseFragment extends Fragment {
    private static final String TAG = BaseFragment.class.getSimpleName();
    /*************************************
     * PRIVATE FIELDS
     *************************************/
    protected MainActivity mMainActivity;

    /*************************************
     * PUBLIC METHODS
     *************************************/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainActivity = ((MainActivity) getActivity());
    }

}
