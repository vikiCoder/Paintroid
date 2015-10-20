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

import org.catrobat.paintroid.PaintroidApplication;
import org.catrobat.paintroid.R;
import org.catrobat.paintroid.command.implementation.TextToolCommand;
import org.catrobat.paintroid.tools.implementation.TextTool;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

@SuppressLint("ValidFragment")
public final class TextToolDialog extends DialogFragment implements
        OnClickListener, DialogInterface.OnClickListener {

    private static final String NOT_INITIALIZED_ERROR_MESSAGE =
                                      "TextToolDialog has not been initialized. Call init() first!";

    private static TextToolDialog instance;
    private TextToolCommand command;
    private Context mContext;
    private EditText mTextContent;
    private ToggleButton toggleUnderline,toggleItalic,toggleBold;
    private Spinner font_spinner;
    private Spinner size_spinner;

    public TextToolCommand getCommand(){
        return command;
    }

    @SuppressLint("ValidFragment")
    private TextToolDialog(Context context) {
        Paint currentPaint = new Paint();
        mContext = context;
        command = new TextToolCommand(currentPaint, new PointF(0,0));
       }

    public static TextToolDialog getInstance() {
        if (instance == null) {
            throw new IllegalStateException(NOT_INITIALIZED_ERROR_MESSAGE);
        }
        return instance;
    }

    public static void init(Context context) {
        instance = new TextToolDialog(context);
    }

    public void setPaint(Paint currentPaint) {
        command.setPaint(currentPaint);
    }




    @TargetApi(11)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflator = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new CustomAlertDialogBuilder(mContext);

        builder.setTitle(R.string.texttool_title);
        View view = inflator.inflate(R.layout.dialog_texttool, null);

        toggleBold=(ToggleButton)view.findViewById(R.id.toggleBold);
        toggleBold.setOnClickListener(new ToggleButton.OnClickListener() {
            @Override
            public void onClick(View button) {
                if (command != null) {
                    command.setBold(toggleBold.isChecked());
                    ((TextTool) PaintroidApplication.currentTool).createAndSetBitmap();
                }
            }
        });


        toggleItalic=(ToggleButton)view.findViewById(R.id.toggleItalic);
        toggleItalic.setOnClickListener(new ToggleButton.OnClickListener() {
            @Override
            public void onClick(View button) {
                if (command != null) {
                    command.setItalic(toggleItalic.isChecked());
                    ((TextTool) PaintroidApplication.currentTool).createAndSetBitmap();
                }
            }
        });

        toggleUnderline=(ToggleButton)view.findViewById(R.id.toggleUnderlined);
        toggleUnderline.setOnClickListener(new ToggleButton.OnClickListener() {
            @Override
            public void onClick(View button) {
                if (command != null) {
                    command.setUnderlined(toggleUnderline.isChecked());
                    ((TextTool) PaintroidApplication.currentTool).createAndSetBitmap();
                }
            }
        });

        mTextContent = (EditText) view.findViewById(R.id.texttool_dialog_textcontent);
        mTextContent.addTextChangedListener(new TextWatcher() {
             public void afterTextChanged(Editable s) {
                if(command != null)
                {
                    command.setTextContent(mTextContent.getText().toString());
                    ((TextTool) PaintroidApplication.currentTool).createAndSetBitmap();
                }
             }
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {
             }

             public void onTextChanged(CharSequence s, int start, int before, int count) {
             }
         });

        builder.setView(view);
        builder.setNeutralButton(R.string.done, this);


        font_spinner = (Spinner) view.findViewById(R.id.font_spinner);
        ArrayAdapter<CharSequence> font_adapter = ArrayAdapter.createFromResource(
                mContext, R.array.font_array, android.R.layout.simple_spinner_item);
        font_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        font_spinner.setAdapter(font_adapter);

        font_spinner.setBackgroundColor(Color.GRAY);
        font_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View button,
                                       int position, long id) {
                if (command != null) {
                    command.setFont(adapterView.getItemAtPosition(position).toString());
                    ((TextTool) PaintroidApplication.currentTool).createAndSetBitmap();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        size_spinner = (Spinner) view.findViewById(R.id.size_spinner);
        final ArrayAdapter<CharSequence> size_adapter = ArrayAdapter.createFromResource(
                mContext, R.array.size_array, android.R.layout.simple_spinner_item);
        size_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        size_spinner.setAdapter(size_adapter);

        size_spinner.setBackgroundColor(Color.GRAY);
        size_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View button,
                                       int position, long id) {
                if(command != null)
                {
                    command.setTextSize(
                          Integer.parseInt(adapterView.getItemAtPosition(position).toString()) * 3);
                    ((TextTool)PaintroidApplication.currentTool).createAndSetBitmap();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mTextContent.setTextSize(20);

        Dialog text_dialog = builder.create();

        WindowManager.LayoutParams window_params = text_dialog.getWindow().getAttributes();
        window_params.gravity = Gravity.BOTTOM;
        text_dialog.getWindow().setDimAmount(0.0f);
        text_dialog.getWindow().setAttributes(window_params);
        return text_dialog;
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
        if(PaintroidApplication.currentTool instanceof TextTool)
            ((TextTool)PaintroidApplication.currentTool).changeBoxPosition();
        if(command != null) {
            mTextContent.setText(command.getTextContent());
            toggleBold.setChecked(command.isBold());
            toggleItalic.setChecked(command.isItalic());
            toggleUnderline.setChecked(command.isUnderlined());
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        switch (which) {
            case AlertDialog.BUTTON_NEUTRAL:
                command.setTextContent(this.mTextContent.getText().toString());
                command.setTextSize(Integer.parseInt(size_spinner.getSelectedItem().toString()) * 3);
                command.setBold(toggleBold.isChecked());
                command.setItalic(toggleItalic.isChecked());
                command.setUnderlined(toggleUnderline.isChecked());
                command.setFont(font_spinner.getSelectedItem().toString());

                if(PaintroidApplication.currentTool instanceof TextTool)
                    ((TextTool)PaintroidApplication.currentTool).createAndSetBitmap();

                //IndeterminateProgressDialog.getInstance().show();
                //PaintroidApplication.commandManager.commitCommand(command);
                dismiss();
                break;

        }
    }
}
