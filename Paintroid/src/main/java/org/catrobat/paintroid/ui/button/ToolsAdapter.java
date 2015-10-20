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

package org.catrobat.paintroid.ui.button;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.catrobat.paintroid.PaintroidApplication;
import org.catrobat.paintroid.R;
import org.catrobat.paintroid.tools.ToolType;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.GridView;

public class ToolsAdapter extends BaseAdapter {

	private Context mContext;

	private LinkedList<ToolType> mButtonsList;
	private LinkedList<ToolType> mRecentlyUsed;
    private ToolsAdapterRecent mToolRecentButtonAdapter;

	public ToolsAdapter(Context context, boolean fromCatrobat, ToolsAdapterRecent mToolRecentButtonAdapter) {
		this.mContext = context;
		mRecentlyUsed = new LinkedList<ToolType>();
        this.mToolRecentButtonAdapter = mToolRecentButtonAdapter;
		initButtons(fromCatrobat);
	}

	private void initButtons(boolean fromCatrobat) {

		mButtonsList = new LinkedList<ToolType>();
		mButtonsList.add(ToolType.BRUSH);
		mButtonsList.add(ToolType.CURSOR);
		mButtonsList.add(ToolType.PIPETTE);
		mButtonsList.add(ToolType.FILL);
		mButtonsList.add(ToolType.STAMP);
		mButtonsList.add(ToolType.RECT);
		mButtonsList.add(ToolType.ELLIPSE);
		mButtonsList.add(ToolType.IMPORTPNG);
		mButtonsList.add(ToolType.CROP);
		mButtonsList.add(ToolType.ERASER);
		mButtonsList.add(ToolType.FLIP);
		mButtonsList.add(ToolType.MOVE);
		mButtonsList.add(ToolType.ZOOM);
		mButtonsList.add(ToolType.ROTATE);
		mButtonsList.add(ToolType.LINE);
        mButtonsList.add(ToolType.TEXTTOOL);
        mButtonsList.add(ToolType.BLUR);
        mButtonsList.add(ToolType.REPLACECOLORTOOL);
		// deactivateToolsFromPreferences();

	}

	public void setNewToolType(ToolType tooltype)
	{
        /*int indexOfSameType = -1;
		for(int i = 0; i < mRecentlyUsed.size(); i++)
        {
            if(mRecentlyUsed.get(i) == tooltype)
            {
                indexOfSameType = i;
            }
            mButtonsList.remove(0);
        }
        if(indexOfSameType >= 0)
        {
            mRecentlyUsed.remove(indexOfSameType);
        }
        mRecentlyUsed.offerFirst(tooltype);

        while(mRecentlyUsed.size() > 4)
        {
            mRecentlyUsed.remove(4);
        }
        for(int i = mRecentlyUsed.size()-1; i >= 0; i--)
        {
            mButtonsList.addFirst(mRecentlyUsed.get(i));
        }*/

        mToolRecentButtonAdapter.addRecent(tooltype);

        for(int i=0; i < mButtonsList.size(); i++)
        {
            System.out.println("ButtonsList: "+i+": "+ (mButtonsList.get(i)));
        }
	}

	@Override
	public int getCount() {
		System.out.println("Dtext: GetCount: "+mButtonsList.size());
		return mButtonsList.size();
	}

	@Override
	public Object getItem(int position) {
		System.out.println("Dtext: getItem at "+position+": "+mButtonsList.get(position));
		return mButtonsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		System.out.println("Dtext: getItemId at "+position+": "+position);
		return position;
	}

	public ToolType getToolType(int position) {
		return mButtonsList.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView;
        ImageView imageView;
        TextView textView;
        if (convertView == null) {
            rowView = new View(mContext);
            rowView = inflater.inflate(R.layout.tool_button, null);
            System.out.println("DText: "+position);
        }
        else
        {
            rowView = (View) convertView;
        }

        imageView = (ImageView) rowView.findViewById(R.id.tool_button_image);
        textView = (TextView) rowView.findViewById(R.id.tool_button_text);
        imageView.setImageResource(mButtonsList.get(position)
                .getImageResource());
        textView.setText(mButtonsList.get(position).getNameResource());
        return rowView;
	}

	/* EXCLUDE PREFERENCES FOR RELEASE */
	// private void deactivateToolsFromPreferences() {
	// SharedPreferences sharedPreferences = PreferenceManager
	// .getDefaultSharedPreferences(mContext);
	// for (int toolsIndex = 0; toolsIndex < mButtonsList.size(); toolsIndex++)
	// {
	// final String toolButtonText = mContext.getString(mButtonsList.get(
	// toolsIndex).getNameResource());
	// if (sharedPreferences.getBoolean(toolButtonText, false) == false) {
	// mButtonsList.remove(toolsIndex);
	// toolsIndex--;
	// }
	// }
	// }

}
