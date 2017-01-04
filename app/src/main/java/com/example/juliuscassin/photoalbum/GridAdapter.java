package com.example.juliuscassin.photoalbum;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Julius Cassin on 5/2/2016.
 */
public class GridAdapter extends ArrayAdapter<Photo> {

    Context context;
    int layoutResourceId;
    ArrayList<Photo> data = null;

    public GridAdapter(Context context, int layoutResourceId, ArrayList<Photo> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridSpace = convertView;
        PhotoHolder holder = null;

        if(gridSpace == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            gridSpace = inflater.inflate(layoutResourceId, parent, false);

            holder = new PhotoHolder();
            holder.photoIcon = (ImageView)gridSpace.findViewById(R.id.imgIcon);

            gridSpace.setTag(holder);
        }
        else
        {
            holder = (PhotoHolder)gridSpace.getTag();
        }

        Photo photo = data.get(position);
        holder.photoIcon.setImageBitmap(photo.bitMap);

        return gridSpace;
    }

    static class PhotoHolder
    {
        ImageView photoIcon;
    }
}


