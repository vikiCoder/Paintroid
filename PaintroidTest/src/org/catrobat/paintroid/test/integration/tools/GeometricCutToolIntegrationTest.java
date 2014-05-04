/**
 *  Paintroid: An image manipulation application for Android.
 *  Copyright (C) 2010-2013 The Catrobat Team
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

package org.catrobat.paintroid.test.integration.tools;

import java.lang.reflect.InvocationTargetException;

import org.catrobat.paintroid.PaintroidApplication;
import org.catrobat.paintroid.R;
import org.catrobat.paintroid.test.integration.BaseIntegrationTestClass;
import org.catrobat.paintroid.test.utils.PrivateAccess;
import org.catrobat.paintroid.test.utils.Utils;
import org.catrobat.paintroid.tools.ToolType;
import org.catrobat.paintroid.tools.implementation.BaseToolWithRectangleShape;
import org.catrobat.paintroid.tools.implementation.BaseToolWithShape;
import org.catrobat.paintroid.tools.implementation.GeometricCutTool;
import org.catrobat.paintroid.ui.DrawingSurface;
import org.catrobat.paintroid.ui.Perspective;
import org.catrobat.paintroid.ui.TopBar.ToolButtonIDs;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.graphics.PointF;

public class GeometricCutToolIntegrationTest extends BaseIntegrationTestClass {

	private static final int Y_CLICK_OFFSET = 25;
	private static final float SCALE_25 = 0.25f;
	private static final float STAMP_RESIZE_FACTOR = 1.5f;

	public GeometricCutToolIntegrationTest() throws Exception {
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
	public void testIconsInitial() {
		selectTool(ToolType.CUT);
		GeometricCutTool gCutTool = (GeometricCutTool) PaintroidApplication.currentTool;
		assertEquals("Wrong icon for parameter button 1", R.drawable.icon_menu_rectangle,
				gCutTool.getAttributeButtonResource(ToolButtonIDs.BUTTON_ID_PARAMETER_BOTTOM_1));
		assertEquals("Wrong icon for parameter button 2", R.drawable.icon_menu_ellipse,
				gCutTool.getAttributeButtonResource(ToolButtonIDs.BUTTON_ID_PARAMETER_BOTTOM_2));
	}

	@Test
	public void testIconsAfterChangeSelection() {
		selectTool(ToolType.CUT);
		GeometricCutTool gCutTool = (GeometricCutTool) PaintroidApplication.currentTool;
		mSolo.clickOnView(mMenuBottomParameter1);
		mSolo.waitForDialogToClose(TIMEOUT);
		assertEquals("Wrong icon for parameter button 1", R.drawable.icon_menu_rectangle,
				gCutTool.getAttributeButtonResource(ToolButtonIDs.BUTTON_ID_PARAMETER_BOTTOM_1));
		assertEquals("Wrong icon for parameter button 2", R.drawable.icon_menu_ellipse,
				gCutTool.getAttributeButtonResource(ToolButtonIDs.BUTTON_ID_PARAMETER_BOTTOM_2));

		mSolo.clickOnView(mMenuBottomParameter2);
		mSolo.waitForDialogToClose(TIMEOUT);

		assertEquals("Wrong icon for parameter button 1", R.drawable.icon_menu_rectangle,
				gCutTool.getAttributeButtonResource(ToolButtonIDs.BUTTON_ID_PARAMETER_BOTTOM_1));
		assertEquals("Wrong icon for parameter button 2", R.drawable.icon_menu_ellipse,
				gCutTool.getAttributeButtonResource(ToolButtonIDs.BUTTON_ID_PARAMETER_BOTTOM_2));
	}

	@Test
	public void testIconsAfterCutWithShape() {
		PointF surfaceCenterPoint = getScreenPointFromSurfaceCoordinates(getSurfaceCenterX(), getSurfaceCenterY());
		selectTool(ToolType.CUT);
		GeometricCutTool gCutTool = (GeometricCutTool) PaintroidApplication.currentTool;
		mSolo.clickOnView(mMenuBottomParameter1);
		mSolo.clickOnScreen(surfaceCenterPoint.x, surfaceCenterPoint.y);
		mSolo.waitForDialogToClose(TIMEOUT);

		assertEquals("Wrong icon for parameter button 1", R.drawable.icon_menu_rectangle,
				gCutTool.getAttributeButtonResource(ToolButtonIDs.BUTTON_ID_PARAMETER_BOTTOM_1));
		assertEquals("Wrong icon for parameter button 2", R.drawable.icon_menu_ellipse,
				gCutTool.getAttributeButtonResource(ToolButtonIDs.BUTTON_ID_PARAMETER_BOTTOM_2));

		mSolo.clickOnView(mMenuBottomParameter2);
		mSolo.clickOnScreen(surfaceCenterPoint.x, surfaceCenterPoint.y);
		mSolo.waitForDialogToClose(TIMEOUT);

		assertEquals("Wrong icon for parameter button 1", R.drawable.icon_menu_rectangle,
				gCutTool.getAttributeButtonResource(ToolButtonIDs.BUTTON_ID_PARAMETER_BOTTOM_1));
		assertEquals("Wrong icon for parameter button 2", R.drawable.icon_menu_ellipse,
				gCutTool.getAttributeButtonResource(ToolButtonIDs.BUTTON_ID_PARAMETER_BOTTOM_2));
	}

	@Test
	public void testCutPixel() throws SecurityException, IllegalArgumentException, NoSuchFieldException,
			IllegalAccessException {

		PointF surfaceCenterPoint = getScreenPointFromSurfaceCoordinates(getSurfaceCenterX(), getSurfaceCenterY());
		mSolo.clickOnScreen(surfaceCenterPoint.x, surfaceCenterPoint.y - Y_CLICK_OFFSET);

		selectTool(ToolType.CUT);

		GeometricCutTool gCutTool = (GeometricCutTool) PaintroidApplication.currentTool;
		PointF toolPosition = new PointF(surfaceCenterPoint.x, surfaceCenterPoint.y - Y_CLICK_OFFSET);
		PrivateAccess.setMemberValue(BaseToolWithShape.class, gCutTool, "mToolPosition", toolPosition);

		mSolo.clickOnScreen(surfaceCenterPoint.x, surfaceCenterPoint.y - Y_CLICK_OFFSET);
		assertTrue("Cutting timed out", hasProgressDialogFinished(LONG_WAIT_TRIES));

		PointF pixelCoordinateToControlColor = new PointF(surfaceCenterPoint.x, surfaceCenterPoint.y - Y_CLICK_OFFSET);
		PointF canvasPoint = Utils.convertFromScreenToSurface(pixelCoordinateToControlColor);
		int pixelToControl = PaintroidApplication.drawingSurface.getPixel(PaintroidApplication.perspective
				.getCanvasPointFromSurfacePoint(canvasPoint));

		assertEquals("First Pixel not Transparent after using Cut", Color.TRANSPARENT, pixelToControl);

		int moveOffset = 100;

		toolPosition.y = toolPosition.y - moveOffset;
		PrivateAccess.setMemberValue(BaseToolWithShape.class, gCutTool, "mToolPosition", toolPosition);

		mSolo.clickOnScreen(toolPosition.x, toolPosition.y);
		assertTrue("Cutting timed out", hasProgressDialogFinished(LONG_WAIT_TRIES));

		toolPosition.y = toolPosition.y - moveOffset;
		PrivateAccess.setMemberValue(BaseToolWithShape.class, gCutTool, "mToolPosition", toolPosition);

		pixelCoordinateToControlColor = new PointF(toolPosition.x, toolPosition.y + moveOffset + Y_CLICK_OFFSET);
		canvasPoint = Utils.convertFromScreenToSurface(pixelCoordinateToControlColor);
		pixelToControl = PaintroidApplication.drawingSurface.getPixel(PaintroidApplication.perspective
				.getCanvasPointFromSurfacePoint(canvasPoint));

		assertEquals("Second Pixel not Transparent after using Cut", Color.TRANSPARENT, pixelToControl);

	}

	@Test
	public void testCutPixelWithEllipse() throws SecurityException, IllegalArgumentException, NoSuchFieldException,
			IllegalAccessException {
		GeometricCutTool tool = new GeometricCutTool(getActivity(), ToolType.CUT);
		float offsetWidth = (Float) PrivateAccess.getMemberValue(BaseToolWithRectangleShape.class, tool, "mBoxWidth");
		float offsetHeight = (Float) PrivateAccess.getMemberValue(BaseToolWithRectangleShape.class, tool, "mBoxHeight");

		PointF surfaceCenterPoint = getScreenPointFromSurfaceCoordinates(getSurfaceCenterX(), getSurfaceCenterY());

		mSolo.clickOnScreen(surfaceCenterPoint.x, surfaceCenterPoint.y - Y_CLICK_OFFSET);
		assertTrue("Cutting timed out", hasProgressDialogFinished(LONG_WAIT_TRIES));

		mSolo.clickOnScreen(surfaceCenterPoint.x - (int) offsetWidth / 4, surfaceCenterPoint.y - Y_CLICK_OFFSET
				- (int) offsetHeight / 4);
		assertTrue("Cutting timed out", hasProgressDialogFinished(LONG_WAIT_TRIES));

		selectTool(ToolType.CUT);

		mSolo.waitForDialogToClose(TIMEOUT);
		mSolo.clickOnView(mMenuBottomParameter2);
		mSolo.waitForDialogToClose(TIMEOUT);

		GeometricCutTool gCutTool = (GeometricCutTool) PaintroidApplication.currentTool;
		PointF toolPosition = new PointF(surfaceCenterPoint.x, surfaceCenterPoint.y - Y_CLICK_OFFSET);
		PrivateAccess.setMemberValue(BaseToolWithShape.class, gCutTool, "mToolPosition", toolPosition);

		mSolo.clickOnScreen(surfaceCenterPoint.x, surfaceCenterPoint.y - Y_CLICK_OFFSET);
		assertTrue("Cutting timed out", hasProgressDialogFinished(LONG_WAIT_TRIES));

		PointF pixelCoordinateToControlColor = new PointF(surfaceCenterPoint.x, surfaceCenterPoint.y - Y_CLICK_OFFSET);
		PointF pixelCoordinateToControlColor2 = new PointF(surfaceCenterPoint.x - (int) offsetWidth / 4,
				surfaceCenterPoint.y - Y_CLICK_OFFSET - (int) offsetHeight / 4);

		PointF canvasPoint = Utils.convertFromScreenToSurface(pixelCoordinateToControlColor);
		PointF canvasPoint2 = Utils.convertFromScreenToSurface(pixelCoordinateToControlColor2);

		int pixelToControl = PaintroidApplication.drawingSurface.getPixel(PaintroidApplication.perspective
				.getCanvasPointFromSurfacePoint(canvasPoint));
		int pixelToControl2 = PaintroidApplication.drawingSurface.getPixel(PaintroidApplication.perspective
				.getCanvasPointFromSurfacePoint(canvasPoint2));

		assertEquals("First Pixel not Transparent after using Cut", Color.TRANSPARENT, pixelToControl);
		assertEquals("Second Pixel not Black after using Cut", Color.BLACK, pixelToControl2);

	}

	@Test
	public void testCutOutsideDrawingSurface() throws SecurityException, IllegalArgumentException,
			NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		assertTrue("Waiting for DrawingSurface", mSolo.waitForView(DrawingSurface.class, 1, TIMEOUT));

		mSolo.clickOnScreen(getSurfaceCenterX(), getSurfaceCenterY() + Utils.getActionbarHeight()
				+ getStatusbarHeight() - Y_CLICK_OFFSET);

		int screenWidth = PaintroidApplication.drawingSurface.getBitmapWidth();
		int screenHeight = PaintroidApplication.drawingSurface.getBitmapHeight();
		PrivateAccess.setMemberValue(Perspective.class, PaintroidApplication.perspective, "mSurfaceScale", SCALE_25);

		mSolo.sleep(500);

		selectTool(ToolType.CUT);

		GeometricCutTool gCutTool = (GeometricCutTool) PaintroidApplication.currentTool;
		PointF toolPosition = new PointF(getSurfaceCenterX(), getSurfaceCenterY());
		PrivateAccess.setMemberValue(BaseToolWithShape.class, gCutTool, "mToolPosition", toolPosition);
		PrivateAccess.setMemberValue(BaseToolWithRectangleShape.class, gCutTool, "mBoxWidth",
				(int) (screenWidth * STAMP_RESIZE_FACTOR));
		PrivateAccess.setMemberValue(BaseToolWithRectangleShape.class, gCutTool, "mBoxHeight",
				(int) (screenHeight * STAMP_RESIZE_FACTOR));

		mSolo.clickOnScreen(getSurfaceCenterX(), getSurfaceCenterY() + Utils.getActionbarHeight()
				+ getStatusbarHeight() - Y_CLICK_OFFSET);
		assertTrue("Cutting timed out", hasProgressDialogFinished(LONG_WAIT_TRIES));

		Bitmap drawingBitmap = ((Bitmap) PrivateAccess.getMemberValue(BaseToolWithRectangleShape.class, gCutTool,
				"mDrawingBitmap")).copy(Config.ARGB_8888, false);

		assertNotNull("After activating Cut, mDrawingBitmap should not be null anymore", drawingBitmap);

		drawingBitmap.recycle();
		drawingBitmap = null;

	}

}
