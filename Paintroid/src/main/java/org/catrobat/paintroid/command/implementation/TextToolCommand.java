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

package org.catrobat.paintroid.command.implementation;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.util.Log;

import org.catrobat.paintroid.PaintroidApplication;
import org.catrobat.paintroid.R;

public class TextToolCommand extends BaseCommand {

    private static final float BITMAP_SCALE = 1f;
    private int textsize_;
    private String textcontent_;
    private PointF position_;
    boolean bold, italic, underlined;
    private String font_;

    public void setFont(String font){
        font_ = font;
    }

    public PointF getTextPosition() {
        return position_;
    }

    public String getTextContent(){
        return textcontent_;
    }

    public int getColor(){
        return mPaint.getColor();
    }

    public Paint getPaint(){
        return mPaint;
    }

    public int getTextSize() {
        return textsize_;
    }

    public void setTextPosition(PointF textPosition) {
        this.position_ = textPosition;
    }

    int textPosition;

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    public boolean isUnderlined() {
        return underlined;
    }

    public void setUnderlined(boolean underlined) {
        this.underlined = underlined;
    }

    public TextToolCommand(Paint currentPaint, PointF position) {
        super(currentPaint);
        textsize_= 0;
        textcontent_ = " ";
        position_ = position;
    }

    public void setPaint(Paint currentPaint) {
        mPaint = new Paint(currentPaint);
    }

    public void setTextSize(int textsize) {
        textsize_ = textsize;
    }

    public void setTextContent(String textcontent) {
        textcontent_ = textcontent;
    }

    @Override
    public void run(Canvas canvas, Bitmap bitmap) {

        notifyStatus(NOTIFY_STATES.COMMAND_STARTED);

        Paint textpaint;

        Typeface tf;
        if(font_.equals(R.string.font_monospace)) {
            tf = Typeface.MONOSPACE;
        }
        else if(font_.equals(R.string.font_sans_serif)){
            tf = Typeface.SANS_SERIF;
        }
        else if(font_.equals(R.string.font_serif)){
            tf = Typeface.SERIF;
        }
        else{
            tf = Typeface.DEFAULT;
        }

        textpaint = new Paint();
        textpaint.setTypeface(tf);
        textpaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textpaint.setColor(mPaint.getColor());
        textpaint.setTextSize(textsize_);
        textpaint.setUnderlineText(underlined);
        textpaint.setFakeBoldText(bold);
        if(italic)
            textpaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));

        canvas.drawText(textcontent_, position_.x, position_.y, textpaint);

        notifyStatus(NOTIFY_STATES.COMMAND_DONE);
    }
}