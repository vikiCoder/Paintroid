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

package org.catrobat.paintroid.tools;

import org.catrobat.paintroid.MainActivity;
import org.catrobat.paintroid.PaintroidApplication;
import org.catrobat.paintroid.R;
import org.catrobat.paintroid.tools.implementation.CropTool;
import org.catrobat.paintroid.tools.implementation.CursorTool;
import org.catrobat.paintroid.tools.implementation.DrawTool;
import org.catrobat.paintroid.tools.implementation.EraserTool;
import org.catrobat.paintroid.tools.implementation.FillTool;
import org.catrobat.paintroid.tools.implementation.FlipTool;
import org.catrobat.paintroid.tools.implementation.GeometricFillTool;
import org.catrobat.paintroid.tools.implementation.ImportTool;
import org.catrobat.paintroid.tools.implementation.LineTool;
import org.catrobat.paintroid.tools.implementation.MoveZoomTool;
import org.catrobat.paintroid.tools.implementation.PipetteTool;
import org.catrobat.paintroid.tools.implementation.ReplaceColorTool;
import org.catrobat.paintroid.tools.implementation.RotationTool;
import org.catrobat.paintroid.tools.implementation.StampTool;
import org.catrobat.paintroid.tools.implementation.TextTool;
import org.catrobat.paintroid.tools.implementation.BlurTool;

import android.app.Activity;
import android.app.AlertDialog;

public class ToolFactory {

	public static Tool createTool(Activity context, ToolType toolType) {
		switch (toolType) {
		case BRUSH:
			return new DrawTool(context, toolType);
		case CURSOR:
			return new CursorTool(context, toolType);
		case ELLIPSE:
			return new GeometricFillTool(context, toolType);
		case STAMP:
			return new StampTool(context, toolType);
		case IMPORTPNG:
			return new ImportTool(context, toolType);
		case PIPETTE:
			return new PipetteTool(context, toolType);
		case FILL:
			return new FillTool(context, toolType);
		case CROP:
			return new CropTool(context, toolType);
		case RECT:
			return new GeometricFillTool(context, toolType);
		case ERASER:
			return new EraserTool(context, toolType);
		case FLIP:
			return new FlipTool(context, toolType);
		case LINE:
                return new LineTool(context, toolType);
		case MOVE:
		case ZOOM:
			return new MoveZoomTool(context, toolType);
		case ROTATE:
			return new RotationTool(context, toolType);
        case TEXTTOOL:
            return new TextTool(context, toolType);
        case REPLACECOLORTOOL:
            return new ReplaceColorTool(context, toolType);
        case BLUR:
            int currentApiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentApiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                return new BlurTool(context, toolType);
            } else{
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(R.string.dialog_old_android_version_alert)
                        .setTitle(R.string.dialog_old_android_version_alert_title);
                AlertDialog dialog = builder.create();
                dialog.show();
                return null;
            }
		default:
			break;
		}
		return new DrawTool(context, ToolType.BRUSH);

	}

}
