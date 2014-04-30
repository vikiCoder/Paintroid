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

package org.catrobat.paintroid.test.junit.command;

import org.catrobat.paintroid.command.implementation.BaseCommand;
import org.catrobat.paintroid.command.implementation.CutCommand;
import org.catrobat.paintroid.test.utils.PaintroidAsserts;
import org.catrobat.paintroid.test.utils.PrivateAccess;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.graphics.Point;

public class CutCommandTest extends CommandTestSetup {

	protected Bitmap mCutBitmapUnderTest;

	@Override
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		mCutBitmapUnderTest = mCanvasBitmapUnderTest.copy(Config.ARGB_8888, true);
		mCutBitmapUnderTest.eraseColor(BITMAP_REPLACE_COLOR);
		mCommandUnderTest = new CutCommand(mCutBitmapUnderTest, new Point(mCanvasBitmapUnderTest.getWidth() / 2,
				mCanvasBitmapUnderTest.getHeight() / 2), mCanvasBitmapUnderTest.getWidth(),
				mCanvasBitmapUnderTest.getHeight(), 0, 0);
		mCommandUnderTestNull = new CutCommand(null, null, 0, 0, 0, 0);
	}

	@Override
	@After
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testRun() {
		mCommandUnderTest.run(mCanvasUnderTest, Bitmap.createBitmap(1, 1, Config.ARGB_8888));
		PaintroidAsserts.assertNotSame(mCutBitmapUnderTest, mCanvasBitmapUnderTest);

		try {
			assertNull("Cut bitmap not recycled.",
					PrivateAccess.getMemberValue(BaseCommand.class, mCommandUnderTest, "mBitmap"));
			assertNotNull("Bitmap not stored",
					PrivateAccess.getMemberValue(BaseCommand.class, mCommandUnderTest, "mFileToStoredBitmap"));
		} catch (Exception e) {
			fail("Failed with exception " + e.toString());
		}
		mCommandUnderTest.run(mCanvasUnderTest, Bitmap.createBitmap(10, 10, Config.ARGB_8888));
		PaintroidAsserts.assertNotSame(mCutBitmapUnderTest, mCanvasBitmapUnderTest);
	}

	@Test
	public void testRunRotateCut() {
		mCutBitmapUnderTest.setPixel(0, 0, Color.GREEN);
		mCommandUnderTest = new CutCommand(mCutBitmapUnderTest, new Point((int) mPointUnderTest.x,
				(int) mPointUnderTest.y), mCanvasBitmapUnderTest.getWidth(), mCanvasBitmapUnderTest.getHeight(), 180, 1);
		mCommandUnderTest.run(mCanvasUnderTest, null);
		mCutBitmapUnderTest.setPixel(0, 0, Color.CYAN);
		mCutBitmapUnderTest.setPixel(mCutBitmapUnderTest.getWidth() - 1, mCutBitmapUnderTest.getHeight() - 1,
				Color.GREEN);
		PaintroidAsserts.assertNotSame(mCutBitmapUnderTest, mCanvasBitmapUnderTest);
		try {
			assertNull("Cut bitmap not recycled.",
					PrivateAccess.getMemberValue(BaseCommand.class, mCommandUnderTest, "mBitmap"));
			assertNotNull("Bitmap not stored",
					PrivateAccess.getMemberValue(BaseCommand.class, mCommandUnderTest, "mFileToStoredBitmap"));
		} catch (Exception e) {
			fail("Failed with exception " + e.toString());
		}
	}
}
