package org.catrobat.paintroid.test.integration.tools;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageButton;

import org.catrobat.paintroid.PaintroidApplication;
import org.catrobat.paintroid.R;
import org.catrobat.paintroid.test.junit.tools.BaseToolTest;
import org.catrobat.paintroid.tools.ToolType;
import org.catrobat.paintroid.tools.implementation.DrawTool;
import org.catrobat.paintroid.tools.implementation.StampTool;
import org.catrobat.paintroid.tools.implementation.TextTool;

import org.catrobat.paintroid.test.integration.BaseIntegrationTestClass;
import org.catrobat.paintroid.ui.TopBar;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Markus on 22.04.2015.
 */
public class TextToolIntegrationTest extends BaseIntegrationTestClass  {

    public TextToolIntegrationTest() throws Exception {
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
    public void testTextToolIcons() {
        selectTool(ToolType.TEXTTOOL);
        TextTool textTool = (TextTool) PaintroidApplication.currentTool;
        assertEquals("Wrong icon for parameter button 1", R.drawable.icon_menu_texttool,
                textTool.getAttributeButtonResource(TopBar.ToolButtonIDs.BUTTON_ID_PARAMETER_BOTTOM_1));
        assertEquals("Wrong icon for parameter button 2", R.drawable.icon_menu_color_palette,
                textTool.getAttributeButtonResource(TopBar.ToolButtonIDs.BUTTON_ID_PARAMETER_BOTTOM_2));
    }

    @Test
    public void testTextToolDialog() {
        selectTool(ToolType.TEXTTOOL);
        TextTool textTool = (TextTool) PaintroidApplication.currentTool;
        mSolo.clickOnView(mMenuBottomParameter1);
        mSolo.waitForDialogToOpen(500);

    }
}
