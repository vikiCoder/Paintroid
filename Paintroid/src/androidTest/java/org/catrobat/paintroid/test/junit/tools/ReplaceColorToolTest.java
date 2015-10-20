package org.catrobat.paintroid.test.junit.tools;

import android.graphics.Point;
import android.graphics.PointF;

import org.catrobat.paintroid.R;
import org.catrobat.paintroid.test.junit.stubs.PathStub;
import org.catrobat.paintroid.test.utils.PrivateAccess;
import org.catrobat.paintroid.tools.ToolType;
import org.catrobat.paintroid.tools.helper.floodfill.FloodFillRange;
import org.catrobat.paintroid.tools.helper.floodfill.FloodFillRangeQueue;
import org.catrobat.paintroid.tools.helper.floodfill.QueueLinearFloodFiller;
import org.catrobat.paintroid.tools.implementation.DrawTool;
import org.catrobat.paintroid.tools.implementation.FillTool;
import org.catrobat.paintroid.tools.implementation.ReplaceColorTool;
import org.catrobat.paintroid.ui.TopBar;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Viktor on 20-May-15.
 */
public class ReplaceColorToolTest extends BaseToolTest {
    protected PrivateAccess mPrivateAccess = new PrivateAccess();

        public ReplaceColorToolTest() {
            super();
        }

        @Override
        @Before
        protected void setUp() throws Exception {
            mToolToTest = new ReplaceColorTool(getActivity(), ToolType.REPLACECOLORTOOL);
            super.setUp();
        }

        @Test
        public void testShouldReturnCorrectToolType() {
            ToolType toolType = mToolToTest.getToolType();
            assertEquals(ToolType.REPLACECOLORTOOL, toolType);
        }

        @Test
        public void testShouldReturnCorrectResourceForBottomButtonOne() {
            int resource = mToolToTest.getAttributeButtonResource(TopBar.ToolButtonIDs.BUTTON_ID_PARAMETER_BOTTOM_1);
            assertEquals("Transparent should be displayed", R.drawable.icon_menu_no_icon, resource);
        }

        @Test
        public void testShouldReturnCorrectResourceForBottomButtonTwo() {
            int resource = mToolToTest.getAttributeButtonResource(TopBar.ToolButtonIDs.BUTTON_ID_PARAMETER_BOTTOM_2);
            assertEquals("Color picker should be displayed", R.drawable.icon_menu_color_palette, resource);
        }

        @Test
        public void testShouldReturnCorrectResourceForCurrentToolButton() {
            int resource = mToolToTest.getAttributeButtonResource(TopBar.ToolButtonIDs.BUTTON_ID_TOOL);
            assertEquals("Fill tool icon should be displayed", R.drawable.icon_menu_replacecolortool, resource);
        }
    /*
    *     @Test
    public void testReplaceColorTool() throws NoSuchFieldException, IllegalAccessException
    {
        PointF event1 = new PointF(0, 50);
        PointF event2 = new PointF(MOVE_TOLERANCE*-1, 50);
        PathStub pathStub = new PathStub();
        PrivateAccess.setMemberValue(DrawTool.class, mToolToTest, "pathToDraw", pathStub);
        mToolToTest.handleDown(event1);
        mToolToTest.handleMove(event2);
        boolean returnValue = mToolToTest.handleUp(event2);
        //#
        PointF event3 = new PointF(0,-50);
        PointF event4 = new PointF(MOVE_TOLERANCE, -50);
        pathStub = new PathStub();
        PrivateAccess.setMemberValue(DrawTool.class, mToolToTest, "pathToDraw", pathStub);
        mToolToTest.handleDown(event3);
        mToolToTest.handleMove(event4);
        returnValue = mToolToTest.handleUp(event4);
        //#
    }*/
    }