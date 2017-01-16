package com.example.kamil.br.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kamil.br.R;
import com.example.kamil.br.database.controller.RoomsController;
import com.example.kamil.br.database.model.Rooms;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kamil on 2016-09-22.
 * Adapter do wyświetlania nazwy i typu pokoju na liście pokoi
 */
public class RoomViewerAdapter extends ArrayAdapter<Rooms>
{
    private Context context;
    private ArrayList<Rooms> roomList;


    public RoomViewerAdapter(Context context, ArrayList<Rooms> objects) {
        super(context, R.layout.activity_room_viewer_row, objects);
        this.context = context;
        this.roomList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        //pobranie jednego elementu
        Rooms room = roomList.get(position);

        //  Stwórz Inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Pobierz rowView z inflater
        View rowView = inflater.inflate(R.layout.activity_room_viewer_row, parent, false);

        // Pobierz textview
        TextView name = (TextView) rowView.findViewById(R.id.textViewName);

        // Ustaw text
        name.setText(room.getName());

        // Zwróć rowview
        return rowView;
    }


}
