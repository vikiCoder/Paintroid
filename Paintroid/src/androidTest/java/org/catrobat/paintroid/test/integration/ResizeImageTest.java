package org.catrobat.paintroid.test.integration;

import android.graphics.Color;
import android.graphics.PointF;
import android.widget.EditText;

import org.catrobat.paintroid.PaintroidApplication;
import org.catrobat.paintroid.R;
import org.catrobat.paintroid.test.utils.Utils;
import org.catrobat.paintroid.tools.ToolType;
import org.catrobat.paintroid.ui.DrawingSurface;

public class ResizeImageTest extends BaseIntegrationTestClass {

    public ResizeImageTest() throws Exception {
        super();
    }

    public void testResizeImage() {
        assertTrue("Waiting for DrawingSurface", mSolo.waitForView(DrawingSurface.class, 1, TIMEOUT));

        PointF screenPoint = new PointF(mScreenWidth / 2, mScreenHeight / 2);
        PointF canvasPoint = Utils.getCanvasPointFromScreenPoint(screenPoint);

        selectTool(ToolType.BRUSH);
        mSolo.sleep(30);
        mSolo.clickOnScreen(screenPoint.x, screenPoint.y);
        mSolo.sleep(30);

        mSolo.clickOnMenuItem("Resize image");
        mSolo.sleep(30);
        EditText imageWidth = (EditText) mSolo.getView(R.id.resizeimage_dialog_imagewidth);
        EditText imageHeight = (EditText) mSolo.getView(R.id.resizeimage_dialog_imageheight);

        String oldWidth = imageWidth.getText().toString();
        String oldHeight = imageHeight.getText().toString();

        final double ratio = 1.0 * Integer.parseInt(oldWidth) / Integer.parseInt(oldHeight);

        mSolo.clearEditText(imageWidth);
        mSolo.sleep(400);
        mSolo.enterText(imageWidth, "200");

        int newHeight = (int) (200 / ratio);
        mSolo.sleep(30);
        mSolo.clickOnMenuItem("Done");
        mSolo.sleep(30);

        mSolo.clickOnMenuItem("Resize image");
        mSolo.sleep(30);

        assertEquals(newHeight, Integer.parseInt(imageHeight.getText().toString()));
    }

    public void testTooBigImage() {
        assertTrue("Waiting for DrawingSurface", mSolo.waitForView(DrawingSurface.class, 1, TIMEOUT));

        PointF screenPoint = new PointF(mScreenWidth / 2, mScreenHeight / 2);
        PointF canvasPoint = Utils.getCanvasPointFromScreenPoint(screenPoint);

        mSolo.clickOnMenuItem("Resize image");
        mSolo.sleep(30);
        EditText imageWidth = (EditText) mSolo.getView(R.id.resizeimage_dialog_imagewidth);
        EditText imageHeight = (EditText) mSolo.getView(R.id.resizeimage_dialog_imageheight);

        String oldWidth = imageWidth.getText().toString();
        String oldHeight = imageHeight.getText().toString();

        mSolo.clearEditText(imageWidth);
        mSolo.sleep(40);
        mSolo.enterText(imageWidth, "10000");

        mSolo.sleep(30);
        mSolo.clickOnMenuItem("Done");
        mSolo.sleep(500);

        mSolo.goBack();

        mSolo.clickOnMenuItem("Resize image");
        mSolo.sleep(400);

        assertEquals(oldWidth, ((EditText) mSolo.getView(R.id.resizeimage_dialog_imagewidth)).getText().toString());
    }
}