package com.kkurtukov.morfing.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.kkurtukov.morfing.R;
import com.kkurtukov.morfing.utilities.debug.LLog;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kkurtukov on 04.09.2016.
 */
public class CustomImageViewWithCheckbox extends FrameLayout implements View.OnClickListener, Checkable {
    private static final String TAG = CustomImageViewWithCheckbox.class.getSimpleName();
    /*************************************
     * BINDS
     *************************************/
    @BindView(R.id.main_imageview)
    ImageView mMainImage;
    @BindView(R.id.checkbox_imageview)
    ImageView mCheckboxImage;

    /*************************************
     * PRIVATE FIELDS
     *************************************/
    private boolean mChecked;
    private int mMainIconResId;
    private int mCheckedIconResId;
    private OnToggleListener mListener;
    private int mNonCheckedIconResId;

    /*************************************
     * PUBLIC METHODS
     *************************************/
    public CustomImageViewWithCheckbox(Context context) {
        super(context);
        init(context);
    }

    public CustomImageViewWithCheckbox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomImageViewWithCheckbox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomImageViewWithCheckbox(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    /*************************************
     * PRIVATE METHODS
     *************************************/
    private void init(Context context) {
        LLog.e(TAG, "init");
        inflate(context, R.layout.view_imageview_with_checkbox, this);
        ButterKnife.bind(this, this);
        setClickable(true);
        setOnClickListener(this);
    }

    private void init(Context context, AttributeSet attributes) {
        LLog.e(TAG, "init");
        init(context);

        TypedArray styledAttributes = getContext().obtainStyledAttributes(attributes,
                R.styleable.CustomImageViewWithCheckbox);
        int attrsCount = styledAttributes.getIndexCount();

        for (int i = 0; i < attrsCount; i++) {
            int styledAttributesIndex = styledAttributes.getIndex(i);

            switch (styledAttributesIndex) {
                case R.styleable.CustomImageViewWithCheckbox_customImageViewWithCheckboxIconSrc:
                    if (styledAttributes.getResourceId(styledAttributesIndex, 0) > 0) {
                        mMainIconResId = styledAttributes.getResourceId(styledAttributesIndex, 0);
                        mMainImage.setImageResource(mMainIconResId);
                    }
                    break;
                case R.styleable.CustomImageViewWithCheckbox_customImageViewWithCheckboxCheckedIconSrc:
                    if (styledAttributes.getResourceId(styledAttributesIndex, 0) > 0) {
                        mCheckedIconResId = styledAttributes.getResourceId(styledAttributesIndex, 0);
                    }
                    break;
                case R.styleable.CustomImageViewWithCheckbox_customImageViewWithCheckboxNonCheckedIconSrc:
                    if (styledAttributes.getResourceId(styledAttributesIndex, 0) > 0) {
                        mNonCheckedIconResId = styledAttributes.getResourceId(styledAttributesIndex, 0);
                    }
                    break;
            }
        }

        styledAttributes.recycle();
    }

    /*************************************
     * PUBLIC METHODS
     *************************************/
    @Override
    public void onClick(View view) {
        LLog.e(TAG, "onClick");
        toggle();
    }

    @Override
    public void setChecked(boolean b) {
        LLog.e(TAG, "setChecked");
        mChecked = b;
        mCheckboxImage.setImageResource(mChecked ? mCheckedIconResId : mNonCheckedIconResId);
    }

    @Override
    public boolean isChecked() {
        LLog.e(TAG, "isChecked");
        return mChecked;
    }

    @Override
    public void toggle() {
        LLog.e(TAG, "toggle");
        setChecked(!mChecked);
        if (mListener != null) {
            mListener.onCustomCheckboxToggle(this, isChecked());
        }
    }

    public void setOnToggleListener(OnToggleListener listener) {
        LLog.e(TAG, "setOnToggleListener");
        mListener = listener;
    }

    public int getMainIconResId(){
        LLog.e(TAG, "getMainIconResId");
        return mMainIconResId;
    }

    /*************************************
     * PUBLIC INTERFACES
     *************************************/
    public interface OnToggleListener {

        void onCustomCheckboxToggle(CustomImageViewWithCheckbox imageViewWithCheckbox, boolean value);

    }
}
