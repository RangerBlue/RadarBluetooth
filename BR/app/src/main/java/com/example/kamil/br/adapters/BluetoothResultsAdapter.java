package com.example.kamil.br.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kamil.br.R;
import com.example.kamil.br.database.controller.RoomsController;
import com.example.kamil.br.database.model.BluetoothResults;

import java.util.ArrayList;

/**
 * Created by Kamil on 2016-12-13.
 * Adapter do wyświetlania wyniów pomiaru bluetooth na liście rezultatów
 */

public class BluetoothResultsAdapter  extends ArrayAdapter<BluetoothResults> {

    private Context context;
    private ArrayList<BluetoothResults> bluetoothResultsList;
    private static int i = 0;


    public BluetoothResultsAdapter(Context context, ArrayList<BluetoothResults> objects) {
        super(context, R.layout.activity_measurement_choose_row, objects);
        this.context = context;
        this.bluetoothResultsList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {


        //pobranie jednego elementu
        BluetoothResults btResutlt = bluetoothResultsList.get(position);

        //  Stwórz Inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Pobierz rowView z inflater
        View rowView = inflater.inflate(R.layout.activity_measurement_choose_row, parent, false);

        // Pobierz textview
        TextView number = (TextView) rowView.findViewById(R.id.textMeasurementChooseNumber);
        TextView edge = (TextView) rowView.findViewById(R.id.textMeasurementChooseEdge);
        TextView name = (TextView) rowView.findViewById(R.id.textMeasurementChooseName);
        TextView rssi = (TextView) rowView.findViewById(R.id.textMeasurementChooseRssi);
        TextView time = (TextView) rowView.findViewById(R.id.textMeasurementChooseTime);
        TextView roomName = (TextView) rowView.findViewById(R.id.textMeasurementChooseRoomName);


        // Ustaw text
        number.setText(String.valueOf(position+1));
        edge.setText(String.valueOf(btResutlt.getEdgeNumber()));

        if(btResutlt.getName().equals("NULL"))
            name.setText("-");
        else
            name.setText(btResutlt.getName());

        if(btResutlt.getRssi()== 0)
            rssi.setText("-");
        else
            rssi.setText(String.valueOf(btResutlt.getRssi()));

        time.setText(String.valueOf(btResutlt.getTime()/1000.));

        roomName.setText(RoomsController.selectNameWhereId(getContext(), btResutlt.getIdRooms()));

        // Zwróć rowview
        return rowView;
    }
}
