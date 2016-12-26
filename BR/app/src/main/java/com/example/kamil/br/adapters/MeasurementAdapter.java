package com.example.kamil.br.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kamil.br.R;
import com.example.kamil.br.database.model.Measurements;

import java.util.ArrayList;

/**
 * Created by Kamil on 2016-10-10.
 * Adapter do wyświetlania daty pomiaru na liście
 */
public class MeasurementAdapter extends ArrayAdapter<Measurements>
{
    private Context context;
    private ArrayList<Measurements> measurementList;


    public MeasurementAdapter(Context context, ArrayList<Measurements> objects) {
        super(context, R.layout.activity_measurement_choose_row, objects);
        this.context = context;
        this.measurementList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        //pobranie jednego elementu
        Measurements measurement = measurementList.get(position);

        //  Stwórz Inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Pobierz rowView z inflater
        View rowView = inflater.inflate(R.layout.activity_bluetooth_results_row, parent, false);

        // Pobierz textview
        TextView date = (TextView) rowView.findViewById(R.id.textViewBluetoothResultsDate);


        // Ustaw text
        date.setText(measurement.getName());

        // Zwróć rowview
        return rowView;
    }
}
