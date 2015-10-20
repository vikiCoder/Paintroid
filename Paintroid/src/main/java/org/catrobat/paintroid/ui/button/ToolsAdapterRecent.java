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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.catrobat.paintroid.R;
import org.catrobat.paintroid.tools.ToolType;

import java.util.LinkedList;

public class ToolsAdapterRecent extends BaseAdapter {

	private Context mContext;

	private LinkedList<ToolType> mRecentlyUsed;

	public ToolsAdapterRecent(Context context, boolean fromCatrobat) {
		this.mContext = context;
		mRecentlyUsed = new LinkedList<ToolType>();
	}

	@Override
	public int getCount() {
		System.out.println("Dtext: GetCount: "+mRecentlyUsed.size());
		return mRecentlyUsed.size();
	}

	@Override
	public Object getItem(int position) {
		System.out.println("Dtext: getItem at "+position+": "+mRecentlyUsed.get(position));
		return mRecentlyUsed.get(position);
	}

	@Override
	public long getItemId(int position) {
		System.out.println("Dtext: getItemId at "+position+": "+position);
		return position;
	}

	public ToolType getToolType(int position) {
		return mRecentlyUsed.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// # View rowView = convertView;

        View rowView = null;
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

        if (getCount() == 0) {
            rowView.setVisibility(View.GONE);
        } else {
            rowView.setVisibility(View.VISIBLE);
            imageView = (ImageView) rowView.findViewById(R.id.tool_button_image);
            textView = (TextView) rowView.findViewById(R.id.tool_button_text);
            imageView.setImageResource(mRecentlyUsed.get(position)
                    .getImageResource());
            textView.setText(mRecentlyUsed.get(position).getNameResource());
        }

		return rowView;
	}

    public void addRecent(ToolType tooltype) {

        if(!mRecentlyUsed.contains(tooltype)) {
            if(mRecentlyUsed.size() >= 4) {
                mRecentlyUsed.remove(3);
            }
            mRecentlyUsed.push(tooltype);
        }

    }

}
