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

package org.catrobat.paintroid.command.implementation;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;

import org.catrobat.paintroid.PaintroidApplication;

public class BlurPointCommand extends BaseCommand {
	protected PointF mPoint;
    private static float BLUR_RADIUS = 15f;

	public BlurPointCommand(Paint paint, PointF point, float radius) {
		super(paint);
		if (point != null) {
			mPoint = new PointF(point.x, point.y);
		}
        BLUR_RADIUS = radius;
	}

	@SuppressLint("NewApi")
    @Override
	public void run(Canvas canvas, Bitmap bitmap) {
		if (canvas == null || mPoint == null) {
			Log.w(PaintroidApplication.TAG,
					"Object must not be null in PointCommand.");
			return;
		}

        notifyStatus(NOTIFY_STATES.COMMAND_STARTED);

        int width = Math.round(bitmap.getWidth());
        int height = Math.round(bitmap.getHeight());

        Bitmap inputBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript renderScript = RenderScript.create(PaintroidApplication.applicationContext);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        Allocation tmpIn = Allocation.createFromBitmap(renderScript, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        float blurSize =  mPaint.getStrokeWidth()/2;

        Bitmap bitmap2draw = getBluredCircle(outputBitmap, mPoint.x, mPoint.y, blurSize);
        canvas.drawBitmap(bitmap2draw,0,0,null);

        notifyStatus(NOTIFY_STATES.COMMAND_DONE);
	}

    private  Bitmap getBluredCircle(Bitmap bitmap, float point_x, float point_y, float blur_radius) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                                            bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        canvas.drawCircle(point_x, point_y, blur_radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}
