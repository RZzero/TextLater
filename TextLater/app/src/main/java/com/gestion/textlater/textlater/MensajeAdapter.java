package com.gestion.textlater.textlater;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.R.id.list;
import static android.R.id.message;

/**
 * Created by bluedeep on 08/04/17.
 */

public class MensajeAdapter extends ArrayAdapter<Message> {


    public MensajeAdapter(Context context, ArrayList<Message> words) {
        super(context, 0, words);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_historial, parent, false);
        }
        Message message = getItem(position);
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.subject_TextView);
        titleTextView.setText(message.getSubject());

        // Display the earthquake date in the UI
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.some_text_TextView);
        dateTextView.setText(message.getContent());
        return listItemView;
    }
}
