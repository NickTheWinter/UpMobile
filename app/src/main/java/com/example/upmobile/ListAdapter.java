package com.example.upmobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ListAdapter extends ArrayAdapter<Quote> {

    private List<Quote> myObjects;

    public ListAdapter(Context context, List<Quote> myObjects) {
        super(context, 0, myObjects);
        this.myObjects = myObjects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_item, parent, false);
        }

        Quote myObject = myObjects.get(position);

        TextView title = convertView.findViewById(R.id.title);
        TextView description = convertView.findViewById(R.id.description);
        ImageView image = convertView.findViewById(R.id.image);

        title.setText(myObject.title);
        description.setText(myObject.description);
        Picasso.get().load(myObject.image).into(image);

        return convertView;
    }
}

