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

public class BluetoothFinderAdapter extends ArrayAdapter<BluetoothResults>
{
    private Context context;
    private ArrayList<BluetoothResults> arrayFoundDevices;

    public BluetoothFinderAdapter(Context context, ArrayList<BluetoothResults> arrayOfAlreadyPairedDevices)
    {
        super(context, R.layout.row_bt_scan_new_devices, arrayOfAlreadyPairedDevices);
        this.context = context;
        this.arrayFoundDevices = arrayOfAlreadyPairedDevices;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        BluetoothResults bluetoothResults = arrayFoundDevices.get(position);

        //  Stwórz Inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Pobierz rowView z inflater
        View rowView = inflater.inflate(R.layout.row_bt_scan_new_devices, parent, false);

        // Pobierz textview
        TextView bt_name = (TextView) rowView.findViewById(R.id.textview_bt_scan_name);
        TextView bt_address = (TextView) rowView.findViewById(R.id.textview_bt_scan_address);
        TextView bt_signal_strength = (TextView) rowView.findViewById(R.id.textview_bt_scan_signal_strength);

        // Ustaw text
        bt_name.setText(bluetoothResults.getName());
        bt_address.setText("address: " + bluetoothResults.getAddress());
        bt_signal_strength.setText("RSSI: " + bluetoothResults.getRssi() + "dbm");

        // Zwróć rowview
        return rowView;

    }


}
