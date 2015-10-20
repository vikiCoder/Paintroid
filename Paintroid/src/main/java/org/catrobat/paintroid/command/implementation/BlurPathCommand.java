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
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.shapes.RoundRectShape;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;

import org.catrobat.paintroid.PaintroidApplication;
import org.catrobat.paintroid.tools.implementation.BlurTool;

public class BlurPathCommand extends BaseCommand {
	protected Path mPath;
    private static float BLUR_RADIUS = 15f;

	public BlurPathCommand(Paint paint, Path path, float radius) {
		super(paint);
		if (path != null) {
			mPath = new Path(path);
		}

        BLUR_RADIUS = radius;
	}

	@SuppressLint("NewApi")
    @Override
	public void run(Canvas canvas, Bitmap bitmap) {
		if ((canvas == null) || mPath == null) {
			Log.w(PaintroidApplication.TAG,
					"Object must not be null in PathCommand.");
			return;
		}

		RectF bounds = new RectF();
		mPath.computeBounds(bounds, true);
		Rect boundsCanvas = canvas.getClipBounds();

		if (boundsCanvas == null) {

			notifyStatus(NOTIFY_STATES.COMMAND_FAILED);
			return;
		}

		if (pathInCanvas(bounds, boundsCanvas)) {

            notifyStatus(NOTIFY_STATES.COMMAND_STARTED);

            Log.i("PATH", "My Path: " + mPath.toString());


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
            PathMeasure mPathMeasure = new PathMeasure(mPath,false);

            Bitmap bitmap2draw = getBluredPath(outputBitmap, mPathMeasure);
            canvas.drawBitmap(bitmap2draw,0,0,null);
			
            notifyStatus(NOTIFY_STATES.COMMAND_DONE);
		} else {

			notifyStatus(NOTIFY_STATES.COMMAND_FAILED);
		}
	}

	private boolean pathInCanvas(RectF rectangleBoundsPath,
			Rect rectangleBoundsCanvas) {
		RectF rectangleCanvas = new RectF(rectangleBoundsCanvas);

		float strokeWidth = mPaint.getStrokeWidth();

		rectangleBoundsPath.bottom = rectangleBoundsPath.bottom
				+ (strokeWidth / 2);
		rectangleBoundsPath.left = rectangleBoundsPath.left - (strokeWidth / 2);
		rectangleBoundsPath.right = rectangleBoundsPath.right
				+ (strokeWidth / 2);
		rectangleBoundsPath.top = rectangleBoundsPath.top - (strokeWidth / 2);

		return (RectF.intersects(rectangleCanvas, rectangleBoundsPath));
	}

    private  Bitmap getBluredPath(Bitmap bitmap, PathMeasure path_measure) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        float blur_counter = 0;
        float blur_step = 1f / path_measure.getLength();
        float[] blur_point = new float[2];

        while(blur_counter < 1) {
            path_measure.getPosTan(path_measure.getLength() * blur_counter, blur_point, null);
            blur_counter += blur_step;
            float blurSize = mPaint.getStrokeWidth() / 2;
            canvas.drawCircle(blur_point[0], blur_point[1], blurSize, paint);
        }
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}
