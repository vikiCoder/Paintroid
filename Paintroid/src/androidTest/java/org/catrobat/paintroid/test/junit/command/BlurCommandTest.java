package org.catrobat.paintroid.test.junit.command;

import android.graphics.Color;
import android.graphics.PointF;

import org.catrobat.paintroid.PaintroidApplication;
import org.catrobat.paintroid.command.implementation.BlurCommand;
import org.catrobat.paintroid.test.junit.stubs.DrawingSurfaceStub;

public class BlurCommandTest extends CommandTestSetup {

    protected void setUp() throws Exception {
        super.setUp();
        PaintroidApplication.drawingSurface = new DrawingSurfaceStub(getContext());
    }

    public void testBlurEffect() {
        int color = Color.RED;
        mCommandUnderTest = new BlurCommand(20);
        mBitmapUnderTest.setPixel(0,0,color);
        mCommandUnderTest.run(mCanvasUnderTest, mBitmapUnderTest);
        int pixel = PaintroidApplication.drawingSurface.getPixel(new PointF(0, 0));
        assertNotSame(color, pixel);
    }

}
