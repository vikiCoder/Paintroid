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
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class BaseToolWithRectangleShapeToolTest extends BaseIntegrationTestClass {

	protected View mButtonOpenDialog;
	protected View mButtonRotateLeft;
	protected View mButtonRotateRights;
	protected View mRadioButton30;
	protected View mRadioButton45;
	protected View mRadioButton90;
	protected View mAngleSelection;
	protected View mAngleSeekBar;
	protected TextView mAngleSeekBarText;
	protected View mSnapCheckBox;

	public BaseToolWithRectangleShapeToolTest() throws Exception {
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

	public void testRotationDialog() {
		selectTool(ToolType.RECT);

		mSolo.clickOnView(mButtonOpenDialog);
		assertTrue("Rotation dialog not shown", mSolo.waitForDialogToOpen());

		/*
		assertFalse("no radio button should be selected", mAngleSelection.isPressed());
		assertEquals("selected angle should be 0", "0", mAngleSeekBarText.getText().toString());
		assertFalse("snapping checkbox should not be activated", mSnapCheckBox.isPressed());

		mSolo.clickOnView(mRadioButton30);
		//assertTrue("Rotation dialog title not found", (String) mSolo.getText(R.string.dialog_rotation_settings_text).getText());
		*/
	}

	public void testRotateWithAngleBySelection() {

	}

	public void testRotateWithAngleBySeekBar() {

	}

	public void testRotateBySnapping() {

	}

	public void testRotationButtonsAreShownWithCorrectTools() {

	}



	@Override
	protected void selectTool(ToolType toolType) {
		int nameRessourceID = toolType.getNameResource();
		if (nameRessourceID == 0)
			return;
		String nameRessourceAsText = mSolo.getString(nameRessourceID);
		assertNotNull("Name Ressource is null", nameRessourceAsText);

		mSolo.clickOnView(mMenuBottomTool);
		Log.i(PaintroidApplication.TAG, "clicked on bottom button tool");
		assertTrue("Tools dialog not visible",
				mSolo.waitForText(mSolo.getString(R.string.dialog_tools_title), 1, TIMEOUT, true));
		mSolo.clickOnText(nameRessourceAsText);
		Log.i(PaintroidApplication.TAG, "clicked on text for tool " + nameRessourceAsText);
		waitForToolToSwitch(toolType);

		mButtonOpenDialog = getActivity().findViewById(R.id.rotation_btn_angle);
		mButtonRotateLeft = getActivity().findViewById(R.id.rotation_btn_left);
		mButtonRotateRights = getActivity().findViewById(R.id.rotation_btn_right);

		mAngleSelection = getActivity().findViewById(R.id.rotation_angle_selection);
		mRadioButton30 = getActivity().findViewById(R.id.rotation_rbtn_30);
		mRadioButton45 = getActivity().findViewById(R.id.rotation_rbtn_45);
		mRadioButton90 = getActivity().findViewById(R.id.rotation_rbtn_90);
		mAngleSeekBar = getActivity().findViewById(R.id.rotation_angle_seek_bar);
		mAngleSeekBarText = (TextView) getActivity().findViewById(R.id.rotation_angle_seek_bar_text);

		mSnapCheckBox = getActivity().findViewById(R.id.rotation_snap_checkbox);
	}
}