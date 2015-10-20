package org.catrobat.paintroid.test.integration;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

import junit.framework.TestCase;

import org.catrobat.paintroid.PaintroidApplication;
import org.catrobat.paintroid.R;
import org.catrobat.paintroid.test.utils.PrivateAccess;
import org.catrobat.paintroid.test.utils.Utils;
import org.catrobat.paintroid.tools.ToolType;
import org.catrobat.paintroid.ui.DrawingSurface;

public class LayersTest extends BaseIntegrationTestClass {

    public LayersTest() throws Exception {
        super();
    }

    @Override
    public void setUp()
    {
        super.setUp();
        mSolo.clickOnMenuItem(getActivity().getString(R.string.menu_new_image));
        mSolo.sleep(30);
        mSolo.clickOnMenuItem(getActivity().getString(R.string.menu_new_image_empty_image));
        mSolo.sleep(30);
    }

    public void testLayersOpen() {
        mSolo.clickOnView(mButtonLayers);
        mSolo.sleep(30);

        String layers_title = getActivity().getString(R.string.layers_title);

        assertTrue("Layer title not found. Couldn't open Layers menu",
                mSolo.waitForText(layers_title, 1, TIMEOUT, true, false));

        mSolo.goBack();
    }

    public void testCreateSelectDeleteLayer() {
        mSolo.clickOnView(mButtonLayers);
        mSolo.sleep(30);
        String new_layer_name = "Layer 1";
        mSolo.clickOnMenuItem(getActivity().getString(R.string.layer_new));
        mSolo.sleep(30);
        assertTrue("New created Layer 'Layer 1' not found",
                mSolo.waitForText(new_layer_name, 1, TIMEOUT, true, false));

        mSolo.clickOnMenuItem(new_layer_name);
        mSolo.sleep(30);
        mSolo.clickOnMenuItem(getActivity().getString(R.string.layer_delete));
        mSolo.sleep(30);
        assertFalse("Couldn't Delete Layer 'Layer 1'",
                mSolo.waitForText(new_layer_name, 1, TIMEOUT, true, false));
        mSolo.goBack();
    }
    public void testRenameLayer() {
        String testName = "RenamedLayer";
        mSolo.clickOnView(mButtonLayers);
        mSolo.sleep(30);
        mSolo.clickOnMenuItem(getActivity().getString(R.string.layer_new));
        mSolo.sleep(30);
        mSolo.clickOnMenuItem("Layer 1");
        mSolo.sleep(30);
        mSolo.clickOnMenuItem(getActivity().getString(R.string.layer_rename));
        mSolo.sleep(30);
        mSolo.typeText(0, testName);
        mSolo.sleep(30);
        mSolo.clickOnMenuItem(getActivity().getString(R.string.layer_ok));
        mSolo.sleep(30);
        assertTrue("Couldn't rename Layer", mSolo.waitForText(testName));
        mSolo.sleep(30);
        mSolo.goBack();
    }

    public void testLockLayer() throws SecurityException, IllegalArgumentException, NoSuchFieldException,
            IllegalAccessException {
        assertTrue("Waiting for DrawingSurface", mSolo.waitForView(DrawingSurface.class, 1, TIMEOUT));

        PointF screenPoint = new PointF(mScreenWidth / 2, mScreenHeight / 2);
        PointF canvasPoint = Utils.getCanvasPointFromScreenPoint(screenPoint);

        selectTool(ToolType.BRUSH);
        mSolo.sleep(30);
        mSolo.clickOnScreen(screenPoint.x, screenPoint.y);
        mSolo.sleep(30);

        int colorBeforeErase = PaintroidApplication.drawingSurface.getPixel(canvasPoint);
        assertEquals("After painting black, pixel should be black", Color.BLACK, colorBeforeErase);

        mSolo.clickOnView(mButtonLayers);
        mSolo.sleep(30);
        mSolo.clickOnMenuItem(getActivity().getString(R.string.layer_lock));
        mSolo.sleep(30);
        mSolo.goBack();

        selectTool(ToolType.ERASER);

        mSolo.clickOnScreen(screenPoint.x, screenPoint.y);
        mSolo.sleep(SHORT_SLEEP);

        int colorAfterErase = PaintroidApplication.drawingSurface.getPixel(canvasPoint);
        assertEquals("Because of lock, erase should not work", Color.BLACK, colorAfterErase);

        mSolo.sleep(30);
        mSolo.goBack();
    }

    public void testVisibleLayer() throws SecurityException, IllegalArgumentException, NoSuchFieldException,
            IllegalAccessException {
        assertTrue("Waiting for DrawingSurface", mSolo.waitForView(DrawingSurface.class, 1, TIMEOUT));
        mSolo.clickOnMenuItem(getActivity().getString(R.string.menu_new_image));
        mSolo.sleep(30);
        mSolo.clickOnMenuItem(getActivity().getString(R.string.menu_new_image_empty_image));
        mSolo.sleep(30);

        PointF screenPoint = new PointF(mScreenWidth / 2, mScreenHeight / 2);
        PointF canvasPoint = Utils.getCanvasPointFromScreenPoint(screenPoint);

        selectTool(ToolType.BRUSH);
        mSolo.sleep(30);
        mSolo.clickOnScreen(screenPoint.x, screenPoint.y);
        mSolo.sleep(30);

        int colorLayerVisible = PaintroidApplication.drawingSurface.getPixel(canvasPoint);
        assertEquals("After painting black, pixel should be black", Color.BLACK, colorLayerVisible);

        mSolo.clickOnView(mButtonLayers);
        mSolo.sleep(30);
        mSolo.clickOnMenuItem(getActivity().getString(R.string.layer_visible));
        mSolo.sleep(30);
        mSolo.goBack();

        int colorLayerInvisible = PaintroidApplication.drawingSurface.getVisiblePixel(canvasPoint);
        assertEquals("After Layer set to invisible color should be transparent", Color.TRANSPARENT, colorLayerInvisible);

        mSolo.sleep(30);
        mSolo.goBack();
    }

    public void testMergeLayers() {
        assertTrue("Waiting for DrawingSurface", mSolo.waitForView(DrawingSurface.class, 1, TIMEOUT));
        mSolo.clickOnView(mButtonLayers);
        mSolo.sleep(30);
        mSolo.clickOnMenuItem(getActivity().getString(R.string.layer_new));
        mSolo.sleep(30);
        mSolo.clickOnMenuItem(getActivity().getString(R.string.layer_merge));
        mSolo.sleep(30);
        mSolo.clickOnMenuItem("Layer 1");
        mSolo.sleep(30);
        assertTrue(mSolo.searchText("Layer 0/Layer 1"));
    }

    public void testMergeButtonBugfix() {
        assertTrue("Waiting for DrawingSurface", mSolo.waitForView(DrawingSurface.class, 1, TIMEOUT));
        mSolo.clickOnView(mButtonLayers);
        mSolo.sleep(30);
        mSolo.clickOnMenuItem(getActivity().getString(R.string.layer_new));
        mSolo.sleep(30);
        mSolo.clickOnMenuItem(getActivity().getString(R.string.layer_merge));
        mSolo.sleep(30);
        mSolo.goBack();
        mSolo.sleep(30);
        mSolo.clickOnView(mButtonLayers);
        mSolo.sleep(30);
        mSolo.clickOnMenuItem("Layer 1");
        mSolo.sleep(30);
        assertFalse(mSolo.searchText("Layer 0/Layer 1"));
    }

    public void testOpacityChange() {
        assertTrue("Waiting for DrawingSurface", mSolo.waitForView(DrawingSurface.class, 1, TIMEOUT));
        mSolo.sleep(30);
        PointF screenPoint = new PointF(mScreenWidth / 2, mScreenHeight / 2);
        PointF canvasPoint = Utils.getCanvasPointFromScreenPoint(screenPoint);
        selectTool(ToolType.BRUSH);
        mSolo.clickOnScreen(screenPoint.x, screenPoint.y);
        mSolo.sleep(10);
        int opacityPixel = PaintroidApplication.drawingSurface.getPixel(canvasPoint);
        assertEquals("Checking Opaque Point", Color.BLACK, opacityPixel);
        mSolo.sleep(10);
        mSolo.clickOnView(mButtonLayers);
        mSolo.sleep(10);
        mSolo.setProgressBar(0, 50);
        mSolo.sleep(10);
        SeekBar opacitySeekbar = (SeekBar) mSolo.getView(R.id.seekbar_layer_opacity);
        mSolo.sleep(10);
        assertEquals("SetOpacity Seekbar", opacitySeekbar.getProgress(), 50);
        mSolo.goBack();
        mSolo.sleep(50);
        int comparePixel = Color.argb(255,0,0,0);
        //TODO RIGHT PIXEL VALUES
        opacityPixel = PaintroidApplication.drawingSurface.getPixel(canvasPoint);
        assertEquals("Checking transparent Point", comparePixel, opacityPixel);
        mSolo.sleep(30);
        mSolo.goBack();
    }

}