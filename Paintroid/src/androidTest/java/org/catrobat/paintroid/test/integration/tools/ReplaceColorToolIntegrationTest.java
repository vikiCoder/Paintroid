package org.catrobat.paintroid.test.integration.tools;

import android.graphics.Color;
import android.graphics.PointF;

import org.catrobat.paintroid.PaintroidApplication;
import org.catrobat.paintroid.test.integration.BaseIntegrationTestClass;
import org.catrobat.paintroid.test.junit.stubs.PathStub;
import org.catrobat.paintroid.test.utils.PrivateAccess;
import org.catrobat.paintroid.test.utils.Utils;
import org.catrobat.paintroid.tools.ToolType;
import org.catrobat.paintroid.tools.implementation.DrawTool;
import org.catrobat.paintroid.ui.DrawingSurface;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Viktor on 20-May-15.
 */
public class ReplaceColorToolIntegrationTest extends BaseIntegrationTestClass {
    public ReplaceColorToolIntegrationTest() throws Exception {
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
        Thread.sleep(1500);
        super.tearDown();
        Thread.sleep(1000);
    }

    @Test
    public void testReplaceColorTool() throws InterruptedException,Exception {
        assertTrue("Waiting for DrawingSurface", mSolo.waitForView(DrawingSurface.class, 1, TIMEOUT));

        PointF screenPoint1 = new PointF(mScreenWidth / 2 - 20, mScreenHeight / 2);
        PointF screenPoint2 = new PointF(mScreenWidth / 2 + 20, mScreenHeight / 2);
        int replaceColor = Color.BLUE;
        int colorBeforeReplace =  Color.RED;
        PaintroidApplication.currentTool.changePaintColor(colorBeforeReplace);
        //#
        selectTool(ToolType.BRUSH);
        Thread.sleep(200);
        mSolo.clickOnScreen(screenPoint1.x, screenPoint1.y);
        Thread.sleep(200);;
        mSolo.clickOnScreen(screenPoint2.x,screenPoint2.y);
        //#
        selectTool(ToolType.REPLACECOLORTOOL);
        Thread.sleep(20);
        PaintroidApplication.currentTool.changePaintColor(replaceColor);
        Thread.sleep(200);
        mSolo.clickOnScreen(screenPoint1.x, screenPoint1.y);
        //#
        Thread.sleep(200);
        assertEquals("Not able to change color choice!",
                PaintroidApplication.currentTool.getDrawPaint().getColor(),
                replaceColor);
        assertEquals("Color replacement not successful!",
                PaintroidApplication.drawingSurface.getPixel(Utils.getCanvasPointFromScreenPoint(screenPoint2)),
                PaintroidApplication.drawingSurface.getPixel(Utils.getCanvasPointFromScreenPoint(screenPoint1)));
        //#
        Thread.sleep(20);
        mSolo.goBack();
    }

}
