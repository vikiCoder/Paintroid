/**
 * Paintroid: An image manipulation application for Android.
 * Copyright (C) 2010-2015 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catrobat.paintroid.dialog;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import org.catrobat.paintroid.PaintroidApplication;
import org.catrobat.paintroid.R;
import org.catrobat.paintroid.command.implementation.TextToolCommand;
import org.catrobat.paintroid.tools.implementation.TextTool;

@SuppressLint("ValidFragment")
public final class ResizeImageDialog extends DialogFragment implements
        OnClickListener, DialogInterface.OnClickListener {

    private static final String NOT_INITIALIZED_ERROR_MESSAGE = "ResizeImageDialog has not been initialized. Call init() first!";

    private static ResizeImageDialog instance;
    private Context mContext;
    private EditText mImageWidth;
    private EditText mImageHeight;
    private CheckBox mMaintainAspectRatio;

    private boolean disableListener = false;

    @SuppressLint("ValidFragment")
    private ResizeImageDialog(Context context) {
        Paint currentPaint = new Paint();
        mContext = context;
    }

    public static ResizeImageDialog getInstance() {
        if (instance == null) {
            throw new IllegalStateException(NOT_INITIALIZED_ERROR_MESSAGE);
        }
        return instance;
    }

    public static void init(Context context) {
        instance = new ResizeImageDialog(context);
    }

    @TargetApi(11)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflator = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new CustomAlertDialogBuilder(mContext);

        View view = inflator.inflate(R.layout.dialog_resizeimage, null);

        mImageWidth = (EditText) view.findViewById(R.id.resizeimage_dialog_imagewidth);
        mImageHeight = (EditText) view.findViewById(R.id.resizeimage_dialog_imageheight);
        mMaintainAspectRatio = (CheckBox) view.findViewById(R.id.resizeimage_dialog_maintain_aspect_ratio);


        mMaintainAspectRatio.setChecked(true);

        int x = PaintroidApplication.drawingSurface.getBitmapWidth();
        int y = PaintroidApplication.drawingSurface.getBitmapHeight();
        final double ratio = 1.0 * x / y;

        mImageWidth.setText(x + "");
        mImageWidth.setSelection(mImageWidth.getText().length());
        mImageHeight.setText(y + "");

       TextWatcher widthWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(disableListener==true)return;
                disableListener=true;
                try {
                    if (mMaintainAspectRatio.isChecked()) {
                        mImageHeight.setText((int) ((new Double(mImageWidth.getText().toString()) / ratio)) + "");
                    }
                } catch (Exception e) {
                    mImageHeight.setText("0");
                }
                disableListener=false;
            }


            public void afterTextChanged(Editable s) {


            }
        };

       TextWatcher heightWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(disableListener==true)return;
                disableListener=true;
                try {
                    if (mMaintainAspectRatio.isChecked()) {
                        mImageWidth.setText((int) ((new Double(mImageHeight.getText().toString()) * ratio)) + "");
                    }
                } catch (Exception e) {
                    mImageWidth.setText("0");
                }
                disableListener=false;

            }

            public void afterTextChanged(Editable s) {


            }
        };

        mMaintainAspectRatio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(disableListener==true)return;
                    disableListener=true;
                    try {
                        if (mMaintainAspectRatio.isChecked()) {
                            mImageHeight.setText((int) ((new Double(mImageWidth.getText().toString()) / ratio)) + "");
                        }
                    } catch (Exception e) {
                        mImageHeight.setText("0");
                    }
                    disableListener=false;
                }
            }
        );

        mImageWidth.addTextChangedListener(widthWatcher);
        mImageHeight.addTextChangedListener(heightWatcher);


        builder.setView(view);
        builder.setNeutralButton(R.string.done, this);

        return builder.create();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.stroke_ibtn_circle:
                break;

            case R.id.stroke_ibtn_rect:
                break;

            case R.id.stroke_rbtn_circle:
                break;

            case R.id.stroke_rbtn_rect:
                break;
            default:
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }
    @Override
    public void onClick(DialogInterface dialog, int which) {

        switch (which) {
            case AlertDialog.BUTTON_NEUTRAL:

                try {
                    Bitmap old=PaintroidApplication.drawingSurface.getBitmapCopy();
                    old = getResizedBitmap(old,new Integer(mImageWidth.getText()+""),new Integer(mImageHeight.getText()+""));
                    PaintroidApplication.drawingSurface.setBitmap(old);
                } catch(Exception e) {
                    showErrorMessage();
                } catch( Error e) {
                    showErrorMessage();
                }
                
                break;
        }
    }

    private void showErrorMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(R.string.dialog_out_of_memory_alert)
                .setTitle(R.string.dialog_out_of_memory_alert_title);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
