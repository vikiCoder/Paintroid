package org.catrobat.paintroid.test.integration.dialog;

import android.graphics.Paint;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import org.catrobat.paintroid.PaintroidApplication;
import org.catrobat.paintroid.R;
import org.catrobat.paintroid.test.integration.BaseIntegrationTestClass;
import org.catrobat.paintroid.test.utils.PrivateAccess;
import org.catrobat.paintroid.tools.implementation.BaseTool;
import org.catrobat.paintroid.ui.DrawingSurface;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.logging.Logger;

public class BlurPickerDialogIntegrationTest extends BaseIntegrationTestClass {

    public BlurPickerDialogIntegrationTest() throws Exception {
        super();
    }

    @Override
    @Before
    protected void setUp() {
        super.setUp();
    }

    @Override
    @After
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testBlurPickerDialog() throws SecurityException, IllegalArgumentException, NoSuchFieldException,
            IllegalAccessException {
        assertTrue("Waiting for DrawingSurface", mSolo.waitForView(DrawingSurface.class, 1, TIMEOUT));
        mSolo.clickOnView(mMenuBottomTool);
        mSolo.clickOnText("Blur");
        mSolo.clickOnView(mMenuBottomParameter1);
        mSolo.sleep(2000);
       ArrayList<ProgressBar> progressBars = mSolo.getCurrentViews(ProgressBar.class);
        assertEquals(progressBars.size(), 2);
        SeekBar blurSizeBar = (SeekBar) progressBars.get(0);
        SeekBar blurIntensityBar = (SeekBar) progressBars.get(1);
        assertEquals(blurSizeBar.getProgress(), 25);
        assertEquals(blurIntensityBar.getProgress(), 10);
        int newBlurSize = 100;
        int newBlurIntensity = 15;
        int paintStrokeWidth = -1;
        mSolo.setProgressBar(0, newBlurSize);
        mSolo.setProgressBar(1, newBlurIntensity);
        assertTrue("Waiting for set blur size ", mSolo.waitForView(LinearLayout.class, 1, TIMEOUT));
        assertEquals(blurSizeBar.getProgress(), newBlurSize);
        assertTrue("Waiting for set blur intensity ", mSolo.waitForView(LinearLayout.class, 1, TIMEOUT));
        assertEquals(blurIntensityBar.getProgress(), newBlurIntensity);
        Paint strokePaint = (Paint) PrivateAccess.getMemberValue(BaseTool.class, PaintroidApplication.currentTool,
                "mCanvasPaint");
        paintStrokeWidth = (int) strokePaint.getStrokeWidth();
        Logger.getLogger("PAINTROID").fine("strokePaint width :" + paintStrokeWidth);

        assertEquals(paintStrokeWidth, newBlurSize);

        mSolo.clickOnButton(mSolo.getString(R.string.done));
        assertTrue("Waiting for Tool to be ready", mSolo.waitForActivity("MainActivity", TIMEOUT));
        strokePaint = (Paint) PrivateAccess.getMemberValue(BaseTool.class, PaintroidApplication.currentTool,
                "mCanvasPaint");
        paintStrokeWidth = (int) strokePaint.getStrokeWidth();
        assertEquals(paintStrokeWidth, newBlurSize);
        mSolo.goBack();
    }
}
