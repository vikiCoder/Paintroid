/**
 *  Paintroid: An image manipulation application for Android.
 *  Copyright (C) 2010-2015 The Catrobat Team
 *  (<http://developer.catrobat.org/credits>)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catrobat.paintroid.dialog;

import java.util.ArrayList;

import org.catrobat.paintroid.R;
import org.catrobat.paintroid.tools.implementation.BlurTool;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public final class BlurPickerDialog extends DialogFragment implements
        OnClickListener, DialogInterface.OnClickListener {

    private static final String NOT_INITIALIZED_ERROR_MESSAGE = "BlurPickerDialog has not been initialized. Call init() first!";

    private static BlurPickerDialog instance;
    private ArrayList<OnBlurChangedListener> mBlurChangedListener;
    private Paint mCurrentPaint;
    private Context mContext;
    private TextView mBlurSizeAmountText;
    private TextView mBlurIntensityAmountText;
    private SeekBar mBlurSizeSeekBar;
    private SeekBar mBlurIntensitySeekBar;
    private int mBlurIntensity = 10;
    private RadioButton mRbtnCircle;
    private RadioButton mRbtnRect;
    private static final int MIN_Blur_SIZE = 1;

    public interface OnBlurChangedListener {
        public void setCap(Cap cap);

        public void setStroke(int stroke);

        public void setIntensity(int blurIntensity);
    }

    public class OnBlurChangedWidthSeekBarListener implements
            SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            if (progress < MIN_Blur_SIZE) {
                progress = MIN_Blur_SIZE;
                seekBar.setProgress(progress);
            }
            if(seekBar.equals(mBlurIntensitySeekBar))
            {
                updateBlurIntensity(progress);
                mBlurIntensityAmountText.setText(""+progress);

            }else {
                updateStrokeChange(progress);
                mBlurSizeAmountText.setText(""+progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }

    @SuppressLint("ValidFragment")
    private BlurPickerDialog(Context context) {

        mBlurChangedListener = new ArrayList<BlurPickerDialog.OnBlurChangedListener>();
        mContext = context;
    }

    public static BlurPickerDialog getInstance() {
        if (instance == null) {
            throw new IllegalStateException(NOT_INITIALIZED_ERROR_MESSAGE);
        }
        return instance;
    }

    public static void init(Context context) {
        instance = new BlurPickerDialog(context);
    }

    public void setCurrentPaint(Paint currentPaint) {
        mCurrentPaint = currentPaint;
        updateStrokeCap(currentPaint.getStrokeCap());
        updateStrokeChange((int) currentPaint.getStrokeWidth());
    }

    public void addBlurChangedListener(OnBlurChangedListener listener) {
        mBlurChangedListener.add(listener);
    }

    public void removeBlurChangedListener(OnBlurChangedListener listener) {
        mBlurChangedListener.remove(listener);
    }

    private void updateStrokeChange(int strokeWidth) {
        for (OnBlurChangedListener listener : mBlurChangedListener) {
            if (listener == null) {
                mBlurChangedListener.remove(listener);
            }
            listener.setStroke(strokeWidth);
        }
    }

    private void updateBlurIntensity(int blurIntensity)
    {
        for (OnBlurChangedListener listener : mBlurChangedListener) {
            listener.setIntensity(blurIntensity);
            mBlurIntensity = blurIntensity;

        }
    }
    private void updateStrokeCap(Cap cap) {
        for (OnBlurChangedListener listener : mBlurChangedListener) {
            if (listener == null) {
                mBlurChangedListener.remove(listener);
            }
            listener.setCap(cap);
        }
    }

    @TargetApi(11)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflator = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new CustomAlertDialogBuilder(mContext);
        // if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
        // builder = new AlertDialog.Builder(mContext);
        //
        // } else {
        // builder = new AlertDialog.Builder(mContext,
        // AlertDialog.THEME_HOLO_DARK);
        // }
        builder.setTitle(R.string.stroke_title);
        View view = inflator.inflate(R.layout.dialog_blur, null);

        ImageButton btn_circle = (ImageButton) view
                .findViewById(R.id.stroke_ibtn_circle);
        btn_circle.setOnClickListener(this);

        ImageButton btn_rect = (ImageButton) view
                .findViewById(R.id.stroke_ibtn_rect);
        btn_rect.setOnClickListener(this);

        mRbtnCircle = (RadioButton) view.findViewById(R.id.stroke_rbtn_circle);
        mRbtnCircle.setOnClickListener(this);

        mRbtnRect = (RadioButton) view.findViewById(R.id.stroke_rbtn_rect);
        mRbtnRect.setOnClickListener(this);

        mBlurSizeSeekBar = (SeekBar) view
                .findViewById(R.id.blur_size_seek_bar);
        mBlurIntensitySeekBar = (SeekBar) view
                .findViewById(R.id.blur_intensity_seek_bar);

        mBlurSizeSeekBar.setMax(100);
        mBlurIntensitySeekBar.setMax(25);
        TextView name = (TextView)view.findViewById(R.id.blur_size_text);
        name.setText("Blur radius");
        TextView name2 = (TextView)view.findViewById(R.id.blur_intensity_text);
        name2.setText("Blur intensity");

        TextView shape = (TextView)view.findViewById(R.id.stroke_width_shape_text);
        shape.setVisibility(View.GONE);

        RadioGroup radio = (RadioGroup)view.findViewById(R.id.stroke_rbtn);
        radio.setVisibility(View.GONE);

        mBlurSizeSeekBar
                .setOnSeekBarChangeListener(new OnBlurChangedWidthSeekBarListener());
        mBlurIntensitySeekBar
                .setOnSeekBarChangeListener(new OnBlurChangedWidthSeekBarListener());

        mBlurSizeAmountText = (TextView) view
                .findViewById(R.id.blur_size_amount_text);
        mBlurIntensityAmountText = (TextView) view
                .findViewById(R.id.blur_intensity_amount_text);

        builder.setView(view);
        builder.setNeutralButton(R.string.done, this);

        return builder.create();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.stroke_ibtn_circle:
                updateStrokeCap(Cap.ROUND);
                mRbtnCircle.setChecked(true);
                break;

            case R.id.stroke_ibtn_rect:
                updateStrokeCap(Cap.SQUARE);
                mRbtnRect.setChecked(true);
                break;

            case R.id.stroke_rbtn_circle:
                updateStrokeCap(Cap.ROUND);
                break;

            case R.id.stroke_rbtn_rect:
                updateStrokeCap(Cap.SQUARE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        /*if (mCurrentPaint.getStrokeCap() == Cap.ROUND) {
            mRbtnCircle.setChecked(true);
        } else {
            mRbtnRect.setChecked(true);
        }*/
        mCurrentPaint.setStrokeCap(Cap.ROUND);
        mBlurSizeSeekBar.setProgress((int) mCurrentPaint.getStrokeWidth());
        mBlurIntensitySeekBar.setProgress(mBlurIntensity);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        switch (which) {
            case AlertDialog.BUTTON_NEUTRAL:
                dismiss();
                break;

        }
    }
}
