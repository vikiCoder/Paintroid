/**
 *  Paintroid: An image manipulation application for Android.
 *  Copyright (C) 2010-2015 The Catrobat Team
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

package org.catrobat.paintroid.dialog;

import org.catrobat.paintroid.MainActivity;
import org.catrobat.paintroid.PaintroidApplication;
import org.catrobat.paintroid.R;
import org.catrobat.paintroid.dialog.InfoDialog.DialogType;
import org.catrobat.paintroid.tools.ToolType;
import org.catrobat.paintroid.ui.button.ToolsAdapter;
import org.catrobat.paintroid.ui.button.ToolsAdapterRecent;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.TextView;

public class ToolsDialog extends BaseDialog implements OnItemClickListener,
		OnItemLongClickListener {

	private static final String NOT_INITIALIZED_ERROR_MESSAGE = "BrushPickerDialog has not been initialized. Call init() first!";
	public static final String FRAGMENT_TRANSACTION_TAG_HELP = "helpdialogfragmenttag";

	private static ToolsDialog instance;

	private ToolsAdapter mToolButtonAdapter;
    private ToolsAdapterRecent mToolRecentButtonAdapter;
	private MainActivity mParent;

    private GridView gridView;
    private GridView gridViewRecent;

	private ToolsDialog(Context context) {
		super(context);
		mParent = (MainActivity) context;
        mToolRecentButtonAdapter = new ToolsAdapterRecent(context,
                PaintroidApplication.openedFromCatroid);
		mToolButtonAdapter = new ToolsAdapter(context,
				PaintroidApplication.openedFromCatroid, mToolRecentButtonAdapter);

	}

	public static ToolsDialog getInstance() {
		if (instance == null) {
			throw new IllegalStateException(NOT_INITIALIZED_ERROR_MESSAGE);
		}
		return instance;
	}

	public static void init(MainActivity mainActivity) {
		instance = new ToolsDialog(mainActivity);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.tools_menu);
        setTitle(R.string.dialog_tools_title);
		setCanceledOnTouchOutside(true);
		gridView = (GridView) findViewById(R.id.gridview_tools_menu);
		gridView.setAdapter(mToolButtonAdapter);
		gridView.setOnItemClickListener(this);
		gridView.setOnItemLongClickListener(this);

        gridViewRecent = (GridView) findViewById(R.id.gridview_recent_tools_menu);
        gridViewRecent.setAdapter(mToolRecentButtonAdapter);
        gridViewRecent.setOnItemClickListener(this);
        gridViewRecent.setOnItemLongClickListener(this);

        ((TextView) findViewById(R.id.gridview_recent_title)).setVisibility(View.GONE);
        ((View) findViewById(R.id.gridview_recent_horizontal_line)).setVisibility(View.GONE);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View button,
			int position, long id) {

        ToolType toolType = null;
        if(adapterView == findViewById(R.id.gridview_tools_menu)) {
            toolType = mToolButtonAdapter.getToolType(position);
        } else if(adapterView == findViewById(R.id.gridview_recent_tools_menu)) {
            toolType = mToolRecentButtonAdapter.getToolType(position);
        }

        System.out.println("onItemClick: " + toolType);
		mParent.switchTool(toolType);

        if(mToolRecentButtonAdapter.getCount() != 0) {
            ((TextView) findViewById(R.id.gridview_recent_title)).setVisibility(View.VISIBLE);
            ((View) findViewById(R.id.gridview_recent_horizontal_line)).setVisibility(View.VISIBLE);
        }

		dismiss();
	}



	@Override
	public boolean onItemLongClick(AdapterView<?> adapterView, View button,
			int position, long id) {
		ToolType toolType = mToolButtonAdapter.getToolType(position);
		new InfoDialog(DialogType.INFO, toolType.getHelpTextResource(),
				toolType.getNameResource()).show(
                mParent.getSupportFragmentManager(),
                FRAGMENT_TRANSACTION_TAG_HELP);
		return true;
	}

	public ToolsAdapter getAdapter()
	{
		return mToolButtonAdapter;
	}

    public void refresh() {
        gridView.setAdapter(mToolButtonAdapter);
    }
}
