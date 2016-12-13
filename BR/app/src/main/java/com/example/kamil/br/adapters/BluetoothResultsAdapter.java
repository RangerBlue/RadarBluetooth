package com.example.kamil.br.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kamil.br.R;
import com.example.kamil.br.database.model.BluetoothResults;

import java.util.ArrayList;

/**
 * Created by Kamil on 2016-12-13.
 */

public class BluetoothResultsAdapter  extends ArrayAdapter<BluetoothResults> {

    private Context context;
    private ArrayList<BluetoothResults> bluetoothResutsList;


    public BluetoothResultsAdapter(Context context, ArrayList<BluetoothResults> objects) {
        super(context, R.layout.activity_measurement_choose_row, objects);
        this.context = context;
        this.bluetoothResutsList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        //pobranie jednego elementu
        BluetoothResults btResutlt = bluetoothResutsList.get(position);

        //  Stwórz Inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Pobierz rowView z inflater
        View rowView = inflater.inflate(R.layout.activity_measurement_choose_row, parent, false);

        // Pobierz textview
        TextView number = (TextView) rowView.findViewById(R.id.textMeasurementChooseNumber);
        TextView name = (TextView) rowView.findViewById(R.id.textMeasurementChooseName);


        // Ustaw text
        number.setText(String.valueOf(btResutlt.getIdBluetoothResults()));
        name.setText(btResutlt.getAddress()+", "+btResutlt.getRssi()+","+btResutlt.getTime()+", "+btResutlt.getEdgeNumber()+",idRoom"+btResutlt.getIdRooms());

        // Zwróć rowview
        return rowView;
    }
}
