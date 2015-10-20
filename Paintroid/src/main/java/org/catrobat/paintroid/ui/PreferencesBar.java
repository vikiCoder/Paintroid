package org.catrobat.paintroid.ui;

import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.catrobat.paintroid.FileIO;
import org.catrobat.paintroid.MainActivity;
import org.catrobat.paintroid.PaintroidApplication;
import org.catrobat.paintroid.R;
import org.catrobat.paintroid.tools.Tool;
import org.catrobat.paintroid.tools.implementation.BaseToolWithRectangleShape;
import org.catrobat.paintroid.tools.implementation.BaseToolWithShape;

public class PreferencesBar {
	private TextView mCanvasRes, mImgRes, mName, mType, mXCoord, mYCoord;
	private LinearLayout xCoordLayout, yCoordLayout, imgResView, imgNameView, imgTypeView;
	private Tool currentTool;
	private MainActivity mMainActivity;
	Handler positionChangedHandler;

	public PreferencesBar(MainActivity mainActivity) {
		mMainActivity = mainActivity;

		mCanvasRes = (TextView) mainActivity
				.findViewById(R.id.tv_bottom_canvasres);

		mImgRes = (TextView) mainActivity
				.findViewById(R.id.tv_bottom_imgres);

		mName = (TextView) mainActivity
				.findViewById(R.id.tv_bottom_name);

		mType = (TextView) mainActivity
				.findViewById(R.id.tv_bottom_type);

		mXCoord = (TextView) mainActivity
				.findViewById(R.id.tv_bottom_xcoord);

		mYCoord = (TextView) mainActivity
				.findViewById(R.id.tv_bottom_ycoord);


		xCoordLayout = (LinearLayout) mainActivity.findViewById(R.id.tv_bottom_xcoordview);
		yCoordLayout = (LinearLayout) mainActivity.findViewById(R.id.tv_bottom_ycoordview);
		imgResView = (LinearLayout) mainActivity.findViewById(R.id.tv_bottom_imgresview);
		imgNameView = (LinearLayout) mainActivity.findViewById(R.id.tv_bottom_imgnameview);
		imgTypeView = (LinearLayout) mainActivity.findViewById(R.id.tv_bottom_imgtypeview);

		positionChangedHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				updateCoordinates();
			}
		};
	}

	public void init(Uri uri)
	{
		updateCanvasRes();
		updateImgRes();
		updateNameAndType(uri);
		updateCoordinates();
	}

	public void init()
	{
		init(PaintroidApplication.savedPictureUri);
	}

	public void updateCoordinates()
	{
		String x,y;
		if(BaseToolWithShape.class.isInstance(currentTool))
		{
			BaseToolWithShape tool = (BaseToolWithShape) currentTool;
			x = Math.round(tool.getXCoordinate())+"";
			y = Math.round(tool.getYCoordinate())+"";

			mXCoord.setText(x);
			mYCoord.setText(y);
		}
	}

	public void updateCanvasRes(){
		int x = PaintroidApplication.drawingSurface.getBitmapWidth();
		int y = PaintroidApplication.drawingSurface.getBitmapHeight();

		String resolution;
		if (x >= 0 && y >= 0) {
			resolution = x + " x " + y;
		} else {
			resolution = "-";
		}
		mCanvasRes.setText(resolution);
	}

	public void updateImgRes(){
		int x = PaintroidApplication.originalImageWidth;
		int y = PaintroidApplication.originalImageHeight;

		String resolution;
		if (x > 0 && y > 0) {
			resolution = x + " x " + y;
			mImgRes.setText(resolution);
			imgResView.setVisibility(View.VISIBLE);
		} else {
			imgResView.setVisibility(View.GONE);
		}

	}

	public void updateNameAndType(Uri uri) {
		String typeTitle;
		String nameTitle;
		String fileName = null;
		String fileType = null;

		if (uri != null) {
			fileName = getFileNameFromUri(uri);
		}

		if (fileName == null) {
			fileName = "";
		}
		String[] fileSubstrings = fileName.split("\\.");
		switch (fileSubstrings.length) {
			case 0:
				fileName = "";
				fileType = "";
				break;
			case 1:
				fileName = fileSubstrings[0];
				fileType = "";
				break;
			case 2:
				fileName = fileSubstrings[0];
				fileType = fileSubstrings[1];
				break;
			default:
				fileType = fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length());
				fileName = fileName.substring(0, fileName.lastIndexOf('.'));
				break;
		}
		if(!fileName.equals("")) {
			mName.setText(fileName);
			mType.setText(fileType.toUpperCase());
			imgNameView.setVisibility(View.VISIBLE);
			imgTypeView.setVisibility(View.VISIBLE);
		}
		else
		{
			imgNameView.setVisibility(View.GONE);
			imgTypeView.setVisibility(View.GONE);
		}
	}

	private String getFileNameFromUri(Uri uri) {
		if(uri == null)
		{
			return "";
		}

		String path = FileIO.getRealPathFromURI(PaintroidApplication.applicationContext, uri);
		if(path == null)
		{
			path="";
		}
		String[] dirs = path.split("/");
		return dirs[dirs.length - 1];
	}

	public void setTool(Tool tool) {
		currentTool = tool;
		if(BaseToolWithShape.class.isInstance(tool))
		{
			xCoordLayout.setVisibility(View.VISIBLE);
			yCoordLayout.setVisibility(View.VISIBLE);
			((BaseToolWithShape)tool).setPositionChangedHandler(positionChangedHandler);
		}
		else
		{
			xCoordLayout.setVisibility(View.GONE);
			yCoordLayout.setVisibility(View.GONE);
		}
		updateCoordinates();
	}
}
