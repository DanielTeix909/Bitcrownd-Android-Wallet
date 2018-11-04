/*
 * Copyright (c) 2014 Flavien Charlon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package bitcrownd.wallet.bitcrownd.utils;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Displays a stretching square image.
 */
public class StretchingImageView extends AppCompatImageView
{
    public StretchingImageView(Context context)
    {
        super(context);
    }

    public StretchingImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public StretchingImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (height > width)
            setMeasuredDimension(width, width);
        else
            setMeasuredDimension(height, height);
    }
}
