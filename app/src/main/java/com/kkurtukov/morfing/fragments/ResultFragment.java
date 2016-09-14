package com.kkurtukov.morfing.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.kkurtukov.morfing.R;
import com.kkurtukov.morfing.utilities.AnimatedGifEncoder;
import com.kkurtukov.morfing.utilities.debug.LLog;

import net.rdrei.android.dirchooser.DirectoryChooserConfig;
import net.rdrei.android.dirchooser.DirectoryChooserFragment;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kkurtukov on 03.09.2016.
 */
public class ResultFragment extends BaseFragment implements
        DirectoryChooserFragment.OnFragmentInteractionListener{
    private static final String TAG = ResultFragment.class.getSimpleName();
    private static final String FILE_NAME = "MorphingTest.gif";
    /*************************************
     * BINDS
     *************************************/
    @BindView(R.id.fragment_result_image)
    ImageView mMainImage;
    @BindView(R.id.fragment_result_second_image)
    ImageView mSecondImage;
    /*************************************
     * PRIVATE FIELDS
     *************************************/
    private MenuItem mNextMenuItem;
    private int mSelectedIconId;
    private int mAlpha;
    private int mCountOfFrames;
    private double mTimeOfAnimation;
    private AnimationDrawable mAnimationDrawable;
    private DirectoryChooserFragment mDialog;

    /*************************************
     * PUBLIC METHODS
     *************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LLog.e(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_result, container, false);
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        LLog.e(TAG, "onViewCreated");
        mAnimationDrawable.start();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        LLog.e(TAG, "onCreateOptionsMenu");
        inflater.inflate(R.menu.menu_main, menu);
        mNextMenuItem = menu.findItem(R.id.action_next);
        mNextMenuItem.setTitle(R.string.action_save);
        mNextMenuItem.setEnabled(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        LLog.e(TAG, "onOptionsItemSelected");
        switch (item.getItemId()) {
            case android.R.id.home:
                mMainActivity.showSettingsScreen(mSelectedIconId, mAlpha, mCountOfFrames, mTimeOfAnimation);
                break;
            case R.id.action_next:
                mDialog.show(mMainActivity.getFragmentManager(), null);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

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

    @Override
    public void onSelectDirectory(@NonNull String path) {
        LLog.e(TAG, "onSelectDirectory");
        File file = new File(path, FILE_NAME);
        saveToFile(file);
        mDialog.dismiss();
    }

    @Override
    public void onCancelChooser() {
        LLog.e(TAG, "onCancelChooser");
        mDialog.dismiss();
    }

    /*************************************
     * PRIVATE METHODS
     *************************************/
    private void init() {
        LLog.e(TAG, "init");

        final DirectoryChooserConfig config = DirectoryChooserConfig.builder()
                .newDirectoryName(getString(R.string.select_dir))
                .build();
        mDialog = DirectoryChooserFragment.newInstance(config);
        mDialog.setDirectoryChooserListener(this);

        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main);
        Bitmap mBitmap2 = BitmapFactory.decodeResource(getResources(), mSelectedIconId);
        mAnimationDrawable = new AnimationDrawable();
        mAnimationDrawable.setOneShot(false);
        int mainCount = (int)Math.round(mCountOfFrames * mTimeOfAnimation);
        int duration = (int)((mTimeOfAnimation / mainCount) * 1000);
        double alphaStep = mAlpha / mainCount;
        for (int i = 0; i < mainCount; i++){
            Paint paint = new Paint();
            paint.setAlpha((int) (alphaStep * i * 255 / 100));
            Bitmap bmOverlay = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), mBitmap.getConfig());
            Canvas canvas = new Canvas();
            canvas.setBitmap(bmOverlay);
            canvas.drawBitmap(mBitmap, new Matrix(), null);
            canvas.drawBitmap(mBitmap2, new Matrix(), paint);
            Drawable d = new BitmapDrawable(getResources(), bmOverlay);
            mAnimationDrawable.addFrame(d, duration);
            bmOverlay = null;
        }
        mMainImage.setImageDrawable(mAnimationDrawable);
    }

    private byte[] generateGif() {
        LLog.e(TAG, "generateGif");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.setDelay((int) mTimeOfAnimation * 1000);
        encoder.setFrameRate(mCountOfFrames);
        encoder.start(bos);
        for (int i = 0; i < mAnimationDrawable.getNumberOfFrames(); i++){
            encoder.addFrame(drawableToBitmap(mAnimationDrawable.getFrame(i)));
        }
        encoder.finish();

        return bos.toByteArray();
    }

    private void saveToFile(File file) {
        LLog.e(TAG, "saveToFile");
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(generateGif());
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
