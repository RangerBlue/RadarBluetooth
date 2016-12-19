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
        ImageView image = (ImageView) rowView.findViewById(R.id.imageViewRoomIcon);

        // Ustaw text
        name.setText(room.getName());
        setImage(image, room.getIdRooms());




        // Zwróć rowview
        return rowView;
    }

    private void setImage(ImageView image, int idRoom )
    {
        Float choice = RoomsController.selectTypeWhereId(getContext(), idRoom);

        if( choice == 1.6f )
                image.setImageResource(R.drawable.office_room_icon);
        if( choice == 2.7f )
                image.setImageResource(R.drawable.big_room_icon);
        if( choice == 2.0f )
                image.setImageResource(R.drawable.free_space_icon);

    }
}
