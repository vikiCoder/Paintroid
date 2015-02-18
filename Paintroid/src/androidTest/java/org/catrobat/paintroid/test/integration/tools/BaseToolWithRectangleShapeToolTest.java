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

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.view.View;

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
		mButtonOpenDialog = getActivity().findViewById(R.id.rotation_btn_left);
		mButtonOpenDialog = getActivity().findViewById(R.id.rotation_btn_right);

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

	}

	public void testRotationButtonsAreShownWithCorrectTools() {

	}

}
