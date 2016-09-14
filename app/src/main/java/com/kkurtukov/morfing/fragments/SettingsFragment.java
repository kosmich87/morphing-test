package com.kkurtukov.morfing.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.kkurtukov.morfing.R;
import com.kkurtukov.morfing.utilities.debug.LLog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.METValidator;

import java.text.DecimalFormat;
import java.text.ParseException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kkurtukov on 03.09.2016.
 */
public class SettingsFragment extends BaseFragment {
    private static final String TAG = SettingsFragment.class.getSimpleName();
    /*************************************
     * BINDS
     *************************************/
    @BindView(R.id.fragment_settings_main_image)
    ImageView mMainImage;
    @BindView(R.id.fragment_settings_second_image)
    ImageView mSecondImage;
    @BindView(R.id.fragment_settings_second_image_alpha)
    SeekBar mAlphaSeekBar;
    @BindView(R.id.count_of_frame)
    MaterialEditText mCountOfFrameEdit;
    @BindView(R.id.time_of_animation)
    MaterialEditText mTimeOfAnimationEdit;
    /*************************************
     * PRIVATE FIELDS
     *************************************/
    private MenuItem mNextMenuItem;
    private int mSelectedIconId;
    private int mAlpha;
    private int mCountOfFrames;
    private double mTimeOfAnimation;
    /*************************************
     * PUBLIC METHODS
     *************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LLog.e(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, rootView);
        init();
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LLog.e(TAG, "onCreate");
        setHasOptionsMenu(true);
        mMainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        LLog.e(TAG, "onCreateOptionsMenu");
        inflater.inflate(R.menu.menu_main, menu);
        mNextMenuItem = menu.findItem(R.id.action_next);
        mNextMenuItem.setTitle(R.string.action_finish);
        super.onCreateOptionsMenu(menu, inflater);
        checkValues();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        LLog.e(TAG, "onOptionsItemSelected");
        switch (item.getItemId()) {
            case android.R.id.home:
                mMainActivity.showStartScreen(mSelectedIconId);
                break;
            case R.id.action_next:
                mMainActivity.showResultScreen(mSelectedIconId, mAlpha, mCountOfFrames, mTimeOfAnimation);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*************************************
     * PUBLIC METHODS
     *************************************/
    public void setSelectedIconId(int selectedIconId) {
        mSelectedIconId = selectedIconId;
    }

    public void setAlpha(int alpha) {
        mAlpha = alpha;
    }

    public void setCountOfFrames(int countOfFrames) {
        mCountOfFrames = countOfFrames;
    }

    public void setTimeOfAnimation(double timeOfAnimation) {
        mTimeOfAnimation = timeOfAnimation;
    }

    /*************************************
     * PRIVATE METHODS
     *************************************/
    private void init() {
        LLog.e(TAG, "init");
        mSecondImage.setImageResource(mSelectedIconId);
        mAlphaSeekBar.setProgress(mAlpha);
        mSecondImage.setAlpha((float) mAlpha / 100);
        mCountOfFrameEdit.setText(String.valueOf(mCountOfFrames));
        mTimeOfAnimationEdit.setText(String.valueOf(mTimeOfAnimation));

        mAlphaSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mSecondImage.setAlpha((float) seekBar.getProgress() / 100);
                mAlpha = seekBar.getProgress();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mCountOfFrameEdit.addValidator(new METValidator(getString(R.string.count_of_frame_error)) {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                boolean valid = true;
                if (isEmpty || Integer.valueOf(text.toString()) > 15 || Integer.valueOf(text.toString()) == 0) {
                    valid = false;
                }
                return valid;
            }
        });
        mCountOfFrameEdit.setValidateOnFocusLost(true);

        mCountOfFrameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkValues();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkValues();
                if (editable.length() > 0) {
                    mCountOfFrames = Integer.valueOf(editable.toString());
                }
            }
        });

        mTimeOfAnimationEdit.addValidator(new METValidator(getString(R.string.count_of_frame_error)) {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                boolean valid = true;
                if (isEmpty || Double.valueOf(text.toString()) > 6.0 || Double.valueOf(text.toString()) < 0.5) {
                    valid = false;
                }
                return valid;
            }
        });
        mTimeOfAnimationEdit.setValidateOnFocusLost(true);

        mTimeOfAnimationEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                LLog.e(TAG, "onKey");
                if (i == KeyEvent.KEYCODE_ENTER){
                    DecimalFormat df = new DecimalFormat("0.0");
                    String formatted = df.format(mTimeOfAnimation);
                    formatted = formatted.replaceAll(",", ".");
                    mTimeOfAnimationEdit.setText(formatted);
                    mTimeOfAnimationEdit.setSelection(formatted.length());
                }
                return false;
            }
        });
        mTimeOfAnimationEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                LLog.e(TAG, "onTextChanged");
                checkValues();
                if (charSequence.length() > 0){
                    /*mTimeOfAnimationEdit.removeTextChangedListener(this);
                    double parsed = Double.parseDouble(charSequence.toString());
                    DecimalFormat df = new DecimalFormat("#.#");
                    String formatted = df.format(parsed);
                    formatted = formatted.replaceAll(",", ".");
                    mTimeOfAnimationEdit.setText(formatted);
                    mTimeOfAnimationEdit.setSelection(formatted.length());
                    mTimeOfAnimationEdit.addTextChangedListener(this);*/
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                LLog.e(TAG, "afterTextChanged");
                /*mTimeOfAnimationEdit.removeTextChangedListener(this);

                try {
                    int inilen, endlen;
                    inilen = mTimeOfAnimationEdit.getText().length();

                    String v = s.toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "");
                    Number n = df.parse(v);
                    int cp = mTimeOfAnimationEdit.getSelectionStart();
                    mTimeOfAnimationEdit.setText(df.format(n));
                    endlen = mTimeOfAnimationEdit.getText().length();
                    int sel = (cp + (endlen - inilen));
                    if (sel > 0 && sel <= mTimeOfAnimationEdit.getText().length()) {
                        mTimeOfAnimationEdit.setSelection(sel);
                    } else {
                        // place cursor at the end?
                        mTimeOfAnimationEdit.setSelection(mTimeOfAnimationEdit.getText().length() - 1);
                    }
                } catch (NumberFormatException nfe) {
                    // do nothing?
                } catch (ParseException e) {
                    // do nothing?
                }

                mTimeOfAnimationEdit.addTextChangedListener(this);*/

                checkValues();
                if (s.length() > 0) {
                    mTimeOfAnimation = Double.valueOf(s.toString());
                }
            }
        });
    }

    private void checkValues() {
        LLog.e(TAG, "checkValues");
        mNextMenuItem.setEnabled(mTimeOfAnimationEdit.validate() && mCountOfFrameEdit.validate());
    }
}
