/*
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

import org.catrobat.paintroid.PaintroidApplication;
import org.catrobat.paintroid.R;
import org.catrobat.paintroid.test.integration.BaseIntegrationTestClass;
import org.catrobat.paintroid.test.utils.PrivateAccess;
import org.catrobat.paintroid.tools.ToolType;
import org.catrobat.paintroid.ui.DrawingSurface;
import org.junit.After;
import org.junit.Before;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.view.View;
import android.widget.RelativeLayout;

public class BaseToolWithRectangleShapeToolTest extends BaseIntegrationTestClass {

	protected View mButtonOpenDialog;
	protected View mButtonRotateLeft;
	protected View mButtonRotateRights;

	public BaseToolWithRectangleShapeToolTest() throws Exception {
		super();
	}

	@Override
	@Before
	protected void setUp() {
		super.setUp();

		mButtonOpenDialog = getActivity().findViewById(R.id.rotation_btn_angle);
		mButtonRotateLeft = getActivity().findViewById(R.id.rotation_btn_left);
		mButtonRotateRights = getActivity().findViewById(R.id.rotation_btn_right);

	}

	@Override
	@After
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testRotationDialog() {

	}

	public void testRotateWithAngleBySelection() {

	}

	public void testRotateWithAngleBySeekBar() {

	}

	public void testRotateBySnapping() {
		selectTool(ToolType.RECT);
		assertFalse("Rotate Angle Button should be shown", (getActivity().findViewById(R.id.rotation_btn_angle) == null));
		assertFalse("Rotate Left Button should be shown", (getActivity().findViewById(R.id.rotation_btn_left) == null));
		assertFalse("Rotate Right Button should be shown", (getActivity().findViewById(R.id.rotation_btn_right) == null));

		mSolo.clickOnView(mButtonOpenDialog);
		mSolo.waitForDialogToOpen();



	}

	public void testRotationButtonsAreShownWithCorrectTools() {
		//mSolo.getView(R.id.rotation_btn_left)
		assertTrue("Rotate Angle Button should not be visible", (getActivity().findViewById(R.id.rotation_btn_angle) == null));
		assertTrue("Rotate Left Button should not be visible", (getActivity().findViewById(R.id.rotation_btn_left) == null));
		assertTrue("Rotate Right Button should not be visible", (getActivity().findViewById(R.id.rotation_btn_right) == null));

		selectTool(ToolType.ELLIPSE);
		assertFalse("Rotate Angle Button should be shown", (getActivity().findViewById(R.id.rotation_btn_angle) == null));
		assertFalse("Rotate Left Button should be shown", (getActivity().findViewById(R.id.rotation_btn_left) == null));
		assertFalse("Rotate Right Button should be shown", (getActivity().findViewById(R.id.rotation_btn_right) == null));

		selectTool(ToolType.ERASER);
		assertTrue("Rotate Angle Button should not be visible", (getActivity().findViewById(R.id.rotation_btn_angle) == null));
		assertTrue("Rotate Left Button should not be visible", (getActivity().findViewById(R.id.rotation_btn_left) == null));
		assertTrue("Rotate Right Button should not be visible", (getActivity().findViewById(R.id.rotation_btn_right) == null));

		selectTool(ToolType.RECT);
		assertFalse("Rotate Angle Button should be shown", (getActivity().findViewById(R.id.rotation_btn_angle) == null));
		assertFalse("Rotate Left Button should be shown", (getActivity().findViewById(R.id.rotation_btn_left) == null));
		assertFalse("Rotate Right Button should be shown", (getActivity().findViewById(R.id.rotation_btn_right) == null));

		selectTool(ToolType.FILL);
		assertTrue("Rotate Angle Button should not be visible", (getActivity().findViewById(R.id.rotation_btn_angle) == null));
		assertTrue("Rotate Left Button should not be visible", (getActivity().findViewById(R.id.rotation_btn_left) == null));
		assertTrue("Rotate Right Button should not be visible", (getActivity().findViewById(R.id.rotation_btn_right) == null));

		selectTool(ToolType.STAMP);
		assertFalse("Rotate Angle Button should be shown", (getActivity().findViewById(R.id.rotation_btn_angle) == null));
		assertFalse("Rotate Left Button should be shown", (getActivity().findViewById(R.id.rotation_btn_left) == null));
		assertFalse("Rotate Right Button should be shown", (getActivity().findViewById(R.id.rotation_btn_right) == null));

		selectTool(ToolType.CROP);
		assertTrue("Rotate Angle Button should not be visible", (getActivity().findViewById(R.id.rotation_btn_angle) == null));
		assertTrue("Rotate Left Button should not be visible", (getActivity().findViewById(R.id.rotation_btn_left) == null));
		assertTrue("Rotate Right Button should not be visible", (getActivity().findViewById(R.id.rotation_btn_right) == null));

	}

}
