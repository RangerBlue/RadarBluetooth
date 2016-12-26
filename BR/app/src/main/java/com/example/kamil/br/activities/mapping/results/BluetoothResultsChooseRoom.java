package com.example.kamil.br.activities.mapping.results;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.kamil.br.R;
import com.example.kamil.br.adapters.RoomViewerAdapter;
import com.example.kamil.br.database.controller.RoomsController;
import com.example.kamil.br.database.model.Rooms;

import java.util.ArrayList;

/**
 * Aktywność służaca do wyboru, pokoju, w którym znaleziono urządzenie
 */
public class BluetoothResultsChooseRoom extends AppCompatActivity {

    /**
     * widok listowy, na którym wyświetlane są elementy
     */
    private ListView list;

    /**
     * adapter listy
     */
    private RoomViewerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_room);

        list = (ListView) findViewById(R.id.listViewMeasurementChooseRoom);
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
                Intent intent = new Intent(BluetoothResultsChooseRoom.this, BluetoothResultsMeasurementChooser.class);
                intent.putExtra("idRooms", idToPass);
                startActivity(intent);
            }
        });
    }
}
