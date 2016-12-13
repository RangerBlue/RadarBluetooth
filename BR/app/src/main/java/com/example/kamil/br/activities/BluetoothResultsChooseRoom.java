package com.example.kamil.br.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.kamil.br.R;
import com.example.kamil.br.adapters.RoomViewerAdapter;
import com.example.kamil.br.database.controller.BluetoothResultsController;
import com.example.kamil.br.database.controller.RoomsController;
import com.example.kamil.br.database.model.BluetoothResults;
import com.example.kamil.br.database.model.Rooms;

import java.util.ArrayList;
import java.util.List;

public class BluetoothResultsChooseRoom extends AppCompatActivity {

    private ListView list;
    private RoomViewerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_results_choose_room);
        list = (ListView) findViewById(R.id.listViewBluetoothResultChooseRoom);
        ArrayList<Rooms> all = (ArrayList<Rooms>) new RoomsController().selectAll(getApplicationContext());

        adapter = new RoomViewerAdapter(this, all);
        list.setAdapter(adapter);
        list.setClickable(true);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Rooms room = adapter.getItem(position);
                int idToPass = room.getIdRooms();
                Log.d("odbieranie id z listy", String.valueOf(idToPass));
                Intent intent = new Intent(BluetoothResultsChooseRoom.this, BluetoothResultsChooseOption.class);
                intent.putExtra("idRooms", idToPass);
                startActivity(intent);
            }
        });

       List<BluetoothResults> allTable = new BluetoothResultsController().selectAll(getApplicationContext());
    //   BluetoothResultsController.printAllTableToLog((ArrayList<BluetoothResults>) allTable);
    }


}
