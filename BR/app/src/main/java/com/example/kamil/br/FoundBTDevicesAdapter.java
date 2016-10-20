package com.example.kamil.br;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class FoundBTDevicesAdapter extends ArrayAdapter<BluetoothObject>
{
    private Context context;
    private ArrayList<BluetoothObject> arrayFoundDevices;

    public FoundBTDevicesAdapter(Context context, ArrayList<BluetoothObject> arrayOfAlreadyPairedDevices)
    {
        super(context, R.layout.row_bt_scan_new_devices, arrayOfAlreadyPairedDevices);
        this.context = context;
        this.arrayFoundDevices = arrayOfAlreadyPairedDevices;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        BluetoothObject bluetoothObject = arrayFoundDevices.get(position);

        // 1. Create Inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.row_bt_scan_new_devices, parent, false);

        // 3. Get the widgets from the rowView
        TextView bt_name = (TextView) rowView.findViewById(R.id.textview_bt_scan_name);
        TextView bt_address = (TextView) rowView.findViewById(R.id.textview_bt_scan_address);
        TextView bt_signal_strength = (TextView) rowView.findViewById(R.id.textview_bt_scan_signal_strength);

        // 4. Set the text for each widget
        bt_name.setText(bluetoothObject.getBluetoothName());
        bt_address.setText("address: " + bluetoothObject.getBluetoothAddress());
        bt_signal_strength.setText("RSSI: " + bluetoothObject.getBluetoothRssi() + "dbm");



        // 5. return rowView
        return rowView;

    }//end getView()


}
