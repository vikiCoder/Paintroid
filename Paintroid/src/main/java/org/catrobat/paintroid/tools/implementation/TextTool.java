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

package org.catrobat.paintroid.tools.implementation;

import org.catrobat.paintroid.PaintroidApplication;
import org.catrobat.paintroid.R;
import org.catrobat.paintroid.command.Command;
import org.catrobat.paintroid.command.implementation.TextToolCommand;
import org.catrobat.paintroid.dialog.IndeterminateProgressDialog;
import org.catrobat.paintroid.dialog.TextToolDialog;
import org.catrobat.paintroid.tools.ToolType;
import org.catrobat.paintroid.ui.DrawingSurface;
import org.catrobat.paintroid.ui.TopBar.ToolButtonIDs;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.View.OnClickListener;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.widget.EditText;
import android.view.inputmethod.EditorInfo;


public class TextTool extends BaseToolWithRectangleShape {

    //private TextToolCommand command;
    private Context mContext;
    private EditText mInputText;

    protected static final int BOX_OFFSET = 10;

    public TextTool(Context context, ToolType toolType) {
        super(context, toolType);
        super.setResizePointsVisible(false);
        super.setResizeable(false);
    }

    @Override
    public void setDrawPaint(Paint paint) {
        // necessary because of timing in MainActivity and Eraser
        super.setDrawPaint(paint);
        createAndSetBitmap();
    }

    public void changeBoxPosition(){
        DrawingSurface surface = PaintroidApplication.drawingSurface;
        mToolPosition = new PointF(surface.getBitmapWidth()/2, surface.getBitmapHeight()/10);
    }

    public void createAndSetBitmap() {
        TextToolCommand command = TextToolDialog.getInstance().getCommand();

        Paint textpaint = new Paint();
        textpaint.setTextSize(command.getTextSize());
        textpaint.setAntiAlias(DEFAULT_ANTIALISING_ON);
        textpaint.setStyle(Paint.Style.FILL);

        textpaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textpaint.setColor(command.getColor());

        textpaint.setUnderlineText(command.isUnderlined());
        textpaint.setFakeBoldText(command.isBold());
        if(command.isItalic())
            textpaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));

        mBoxWidth = textpaint.measureText(command.getTextContent()) + 2 * BOX_OFFSET;
        if(textpaint.getTextSize() == 0)
            mBoxHeight = 1;
        else
            mBoxHeight = textpaint.getTextSize() + 2 * BOX_OFFSET;

        Bitmap bitmap = Bitmap.createBitmap((int) mBoxWidth, (int) mBoxHeight,
                Bitmap.Config.ARGB_8888);
        Canvas drawCanvas = new Canvas(bitmap);

        drawCanvas.drawText(command.getTextContent(), BOX_OFFSET,
                            BOX_OFFSET + textpaint.getTextSize(), textpaint);

        mDrawingBitmap = bitmap;
    }

    @Override
    public void resetInternalState() {
    }


    @Override
    protected void onClickInBox() {
        Command command = TextToolDialog.getInstance().getCommand();
        PointF new_position = new PointF(mToolPosition.x - mBoxWidth/2 + BOX_OFFSET,
                                         mToolPosition.y + mBoxHeight/2 - BOX_OFFSET);
        ((TextToolCommand)command).setTextPosition(new_position);
        PaintroidApplication.commandManager.commitCommand(command);
    }

    @Override
    protected void drawToolSpecifics(Canvas canvas) {

    }

    @Override
    public int getAttributeButtonResource(ToolButtonIDs buttonNumber) {
        switch (buttonNumber) {
            case BUTTON_ID_PARAMETER_TOP:
                return getStrokeColorResource();
            case BUTTON_ID_PARAMETER_BOTTOM_1:
                return R.drawable.icon_menu_texttool;
            case BUTTON_ID_PARAMETER_BOTTOM_2:
                return R.drawable.icon_menu_color_palette;
            default:
                return super.getAttributeButtonResource(buttonNumber);
        }
    }

    @Override
    public void attributeButtonClick(ToolButtonIDs buttonNumber) {
        switch (buttonNumber) {
            case BUTTON_ID_PARAMETER_BOTTOM_1:
                showTextToolDialog();
                break;
            case BUTTON_ID_PARAMETER_BOTTOM_2:
            case BUTTON_ID_PARAMETER_TOP:
                showColorPicker();
        }
    }
}
