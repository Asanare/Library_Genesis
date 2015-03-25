package com.jmrandombitz.librarygenesis;

/**
 * Created by Jamal on 21/03/2015.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

// We can create custom adapter
public class MyAdapter extends ArrayAdapter<Book> {
    public MyAdapter(Context context, ArrayList<Book> bookInfo) {
        super(context, 0, bookInfo);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Book book = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.book_list_layout, parent, false);
        }
        // Lookup view for data population
        TextView tvTitle = (TextView) convertView.findViewById(R.id.title);
        TextView tvDl = (TextView) convertView.findViewById(R.id.dlLink);
        // Populate the data into the template view using the data object
        tvTitle.setText(book.title);
        tvDl.setText(book.dlLink);
        // Return the completed view to render on screen
        return convertView;
    }
}



