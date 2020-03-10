package com.Gem.gmll;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import java.util.ArrayList;


public class CustomAd extends BaseAdapter {

    private Context mContext;
   ArrayList<Bitmap> mImageIds = new ArrayList<Bitmap>();
    public CustomAd(Context context, ArrayList<Bitmap>mImageIds)
    {
        mContext = context;
       this.mImageIds = mImageIds;
    }

    public int getCount() {
        return mImageIds.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }


    // Override this method according to your need
    public View getView(int index, View view, ViewGroup viewGroup)
    {
        // TODO Auto-generated method stub
        ImageView i = new ImageView(mContext);

        i.setImageBitmap(mImageIds.get(index));
        i.setLayoutParams(new Gallery.LayoutParams(200, 200));
       // i.setScaleType(ImageView.ScaleType.FIT_XY);

        return i;
    }


}
