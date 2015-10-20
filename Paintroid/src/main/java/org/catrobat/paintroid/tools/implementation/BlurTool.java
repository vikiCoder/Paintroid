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

import org.catrobat.paintroid.MainActivity;
import org.catrobat.paintroid.PaintroidApplication;
import org.catrobat.paintroid.R;
import org.catrobat.paintroid.command.Command;
import org.catrobat.paintroid.command.implementation.BlurCommand;
import org.catrobat.paintroid.command.implementation.BlurPathCommand;
import org.catrobat.paintroid.command.implementation.BlurPointCommand;
import org.catrobat.paintroid.dialog.BlurPickerDialog;
import org.catrobat.paintroid.dialog.IndeterminateProgressDialog;
import org.catrobat.paintroid.tools.ToolType;
import org.catrobat.paintroid.ui.TopBar.ToolButtonIDs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

public class BlurTool extends BaseTool {

    protected final Path pathToDraw;
    protected PointF mInitialEventCoordinate;
    protected final PointF movedDistance;
    public static List<PointF> pathPoints;
    private float mBlurIntensity = 10;

    public BlurTool(Context context, ToolType toolType) {
        super(context, toolType);
        pathToDraw = new Path();
        pathToDraw.incReserve(1);
        movedDistance = new PointF(0f, 0f);
        pathPoints = new ArrayList<PointF>();
    }

    @Override
    public void draw(Canvas canvas) {
        int oldColor = mCanvasPaint.getColor();

        int newColor = Color.argb(100, 0, 0, 255);
        changePaintColor(newColor);
        canvas.drawPath(pathToDraw, mBitmapPaint);

        changePaintColor(oldColor);
    }

    @Override
    public boolean handleDown(PointF coordinate) {
        if (coordinate == null) {
            return false;
        }
        mInitialEventCoordinate = new PointF(coordinate.x, coordinate.y);
        mPreviousEventCoordinate = new PointF(coordinate.x, coordinate.y);
        pathToDraw.moveTo(coordinate.x, coordinate.y);
        pathPoints.add(new PointF(coordinate.x, coordinate.y));
        movedDistance.set(0, 0);
        return true;
    }

    @Override
    public boolean handleMove(PointF coordinate) {
        if (mInitialEventCoordinate == null || mPreviousEventCoordinate == null
                || coordinate == null) {
            return false;
        }
        pathToDraw.quadTo(mPreviousEventCoordinate.x,
                mPreviousEventCoordinate.y, coordinate.x, coordinate.y);
        pathToDraw.incReserve(1);
        movedDistance.set(
                movedDistance.x
                        + Math.abs(coordinate.x - mPreviousEventCoordinate.x),
                movedDistance.y
                        + Math.abs(coordinate.y - mPreviousEventCoordinate.y));
        mPreviousEventCoordinate.set(coordinate.x, coordinate.y);
        pathPoints.add(new PointF(coordinate.x, coordinate.y));

        return true;
    }

    @Override
    public boolean handleUp(PointF coordinate) {
        if (mInitialEventCoordinate == null || mPreviousEventCoordinate == null
                || coordinate == null) {
            return false;
        }
        movedDistance.set(
                movedDistance.x
                        + Math.abs(coordinate.x - mPreviousEventCoordinate.x),
                movedDistance.y
                        + Math.abs(coordinate.y - mPreviousEventCoordinate.y));
        boolean returnValue;
        if (MOVE_TOLERANCE < movedDistance.x
                || MOVE_TOLERANCE < movedDistance.y) {
            returnValue = addBlurPathCommand(coordinate);
        } else {
            returnValue = addBlurPointCommand(mInitialEventCoordinate);
        }
        return returnValue;
    }

    protected boolean addBlurPathCommand(PointF coordinate) {
        pathToDraw.lineTo(coordinate.x, coordinate.y);
        Paint mPreviousPaint = new Paint(
                PaintroidApplication.currentTool.getDrawPaint());
        Command command = new BlurPathCommand(mBitmapPaint, pathToDraw, mBlurIntensity);
        PaintroidApplication.commandManager.commitCommand(command);
        return true;
    }

    protected boolean addBlurPointCommand(PointF coordinate) {
        Paint mPreviousPaint = new Paint(
                PaintroidApplication.currentTool.getDrawPaint());
        Command command = new BlurPointCommand(mBitmapPaint, coordinate, mBlurIntensity);
        PaintroidApplication.commandManager.commitCommand(command);
        return true;
    }

    @Override
    public int getAttributeButtonResource(ToolButtonIDs toolButtonID) {
        switch (toolButtonID) {
            case BUTTON_ID_PARAMETER_BOTTOM_1:
                return R.drawable.icon_menu_strokes;
            case BUTTON_ID_PARAMETER_BOTTOM_2:
                return R.drawable.icon_menu_checkmark;
            default:
                return super.getAttributeButtonResource(toolButtonID);
        }
    }

    @Override
    public void attributeButtonClick(ToolButtonIDs toolButtonID) {
        switch (toolButtonID) {
            case BUTTON_ID_PARAMETER_BOTTOM_1:
                showBlurPicker();
                break;
            case BUTTON_ID_PARAMETER_BOTTOM_2:
                Paint mPreviousPaint = new Paint(
                        PaintroidApplication.currentTool.getDrawPaint());
                Command command = new BlurCommand(mPreviousPaint.getStrokeWidth());
                IndeterminateProgressDialog.getInstance().show();
                ((BlurCommand) command).addObserver(this);
                PaintroidApplication.commandManager.commitCommand(command);
                break;
            default:
                break;
        }
    }

    @Override
    public int getAttributeButtonColor(ToolButtonIDs buttonNumber) {
        switch (buttonNumber) {
            case BUTTON_ID_PARAMETER_TOP:
                return Color.TRANSPARENT;
            default:
                return super.getAttributeButtonColor(buttonNumber);
        }
    }
    void changeBlurIntensity(int blurIntensity)
    {
        mBlurIntensity =  blurIntensity;
    }
    protected void showBlurPicker() {
        BlurPickerDialog.OnBlurChangedListener mStroke = new BlurPickerDialog.OnBlurChangedListener() {
            @Override
            public void setCap(Paint.Cap cap) {
                changePaintStrokeCap(cap);
            }

            @Override
            public void setStroke(int strokeWidth) {
                changePaintStrokeWidth(strokeWidth);
            }

            @Override
           public void setIntensity(int blurIntensity){changeBlurIntensity(blurIntensity);}
        };
        BlurPickerDialog.getInstance().addBlurChangedListener(mStroke);
        BlurPickerDialog.getInstance().setCurrentPaint(mBitmapPaint);
        BlurPickerDialog.getInstance().show(
                ((MainActivity) mContext).getSupportFragmentManager(),
                "blurpicker");
    }

    @Override
    public void resetInternalState() {
        pathToDraw.reset();
        mInitialEventCoordinate = null;
        mPreviousEventCoordinate = null;
    }
}
